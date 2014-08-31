package it.polito.applicazionimultimediali.carnevalediivrea.battle;

import it.polito.applicazionimultimediali.carnevalediivrea.CurrentPlayer;
import it.polito.applicazionimultimediali.carnevalediivrea.GlobalRes;
import it.polito.applicazionimultimediali.carnevalediivrea.R;
import it.polito.applicazionimultimediali.carnevalediivrea.map.MapPane;

import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.games.Games;
import com.google.android.gms.games.GamesStatusCodes;
import com.google.android.gms.games.multiplayer.Invitation;
import com.google.android.gms.games.multiplayer.Multiplayer;
import com.google.android.gms.games.multiplayer.OnInvitationReceivedListener;
import com.google.android.gms.games.multiplayer.realtime.RoomConfig;
import com.google.android.gms.games.multiplayer.turnbased.OnTurnBasedMatchUpdateReceivedListener;
import com.google.android.gms.games.multiplayer.turnbased.TurnBasedMatch;
import com.google.android.gms.games.multiplayer.turnbased.TurnBasedMatchConfig;
import com.google.android.gms.games.multiplayer.turnbased.TurnBasedMultiplayer;
import com.google.android.gms.games.multiplayer.turnbased.TurnBasedMultiplayer.UpdateMatchResult;
import com.google.example.games.basegameutils.BaseGameActivity;

public class BattleActivity extends BaseGameActivity implements
		View.OnClickListener, OnInvitationReceivedListener,
		OnTurnBasedMatchUpdateReceivedListener {
	private boolean isAChallenge;
	private View signinPopup;
	private AlertDialog mAlertDialog;
	private TurnBasedMatch mMatch;
	private boolean isDoingTurn;
	private static String TAG = "BattleActivity";

	// For our intents
	final static int RC_SELECT_PLAYERS = 10000;
	final static int RC_LOOK_AT_MATCHES = 10001;
	private int TOAST_DELAY = 200;
	private boolean isNotification;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.battle_activity);
		getGameHelper().setMaxAutoSignInAttempts(0);

		signinPopup = findViewById(R.id.login_popup);
		findViewById(R.id.sign_in_button).setOnClickListener(this);

		isAChallenge = false;
		isNotification = false;
		Invitation inv = null;
		TurnBasedMatch tbm = null;

		if (savedInstanceState == null) {
			Bundle extras = getIntent().getExtras();
			if (extras != null) {
				inv = extras.getParcelable(Multiplayer.EXTRA_INVITATION);
				tbm = extras.getParcelable(Multiplayer.EXTRA_TURN_BASED_MATCH);
				isAChallenge = extras.getBoolean("Challenge");
			}
		} else {
			isAChallenge = savedInstanceState.getBoolean("challenge");
		}

		if (inv != null || tbm != null)
			isNotification = true;

		mMatch = tbm;

		// single battle
		if (!isAChallenge && !isNotification) {
			goToGame(null);
			return;
		}

		// if not single, wait for signin
		if (!isSignedIn()) {
			// ask for login
			signinPopup.setVisibility(View.VISIBLE);
		} else
			onSignInSucceeded();

	}

	private void startChallenge() {
		toggleSpinner(true);
		Intent intent = Games.TurnBasedMultiplayer.getSelectOpponentsIntent(
				getApiClient(), 1, 1, true);
		startActivityForResult(intent, RC_SELECT_PLAYERS);
		// after a UserSelectionUI, it runs onActivityResult
	};

	@Override
	public void onActivityResult(int request, int response, Intent data) {
		super.onActivityResult(request, response, data);

		if (request == RC_SELECT_PLAYERS) {
			if (response != Activity.RESULT_OK || data == null) {
				// user canceled
				goToMap(null);
				return;
			}

			// get the invitee list
			final ArrayList<String> invitees = data
					.getStringArrayListExtra(Games.EXTRA_PLAYER_IDS);

			// get auto-match criteria
			Bundle autoMatchCriteria = null;
			int minAutoMatchPlayers = data.getIntExtra(
					Multiplayer.EXTRA_MIN_AUTOMATCH_PLAYERS, 0);
			int maxAutoMatchPlayers = data.getIntExtra(
					Multiplayer.EXTRA_MAX_AUTOMATCH_PLAYERS, 0);
			if (minAutoMatchPlayers > 0) {
				autoMatchCriteria = RoomConfig.createAutoMatchCriteria(
						minAutoMatchPlayers, maxAutoMatchPlayers, 0);
			} else {
				autoMatchCriteria = null;
			}

			TurnBasedMatchConfig tbmc = TurnBasedMatchConfig.builder()
					.addInvitedPlayers(invitees)
					.setAutoMatchCriteria(autoMatchCriteria).build();

			// Start the match
			ResultCallback<TurnBasedMultiplayer.InitiateMatchResult> cb = new ResultCallback<TurnBasedMultiplayer.InitiateMatchResult>() {
				@Override
				public void onResult(
						TurnBasedMultiplayer.InitiateMatchResult result) {
					processResult(result);
				}
			};

			// kick the match off
			Games.TurnBasedMultiplayer.createMatch(getApiClient(), tbmc)
					.setResultCallback(cb);
		}
	}

	private void processResult(TurnBasedMultiplayer.CancelMatchResult result) {
		toggleSpinner(false);

		if (!checkStatusCode(null, result.getStatus().getStatusCode())) {
			return;
		}

		isDoingTurn = false;

		showWarning("Match",
				"This match is canceled.  All other players will have their game ended.");
	}

	private void processResult(TurnBasedMultiplayer.InitiateMatchResult result) {
		TurnBasedMatch match = result.getMatch();
		toggleSpinner(false);

		if (!checkStatusCode(match, result.getStatus().getStatusCode())) {
			return;
		}

		if (match.getData() != null) {
			// This is a game that has already started, so I'll just start
			updateMatch(match);
			return;
		}

		goToGame(match);
	}

	private void processResult(TurnBasedMultiplayer.LeaveMatchResult result) {
		TurnBasedMatch match = result.getMatch();
		toggleSpinner(false);
		if (!checkStatusCode(match, result.getStatus().getStatusCode())) {
			return;
		}
		isDoingTurn = (match.getTurnStatus() == TurnBasedMatch.MATCH_TURN_STATUS_MY_TURN);
		showWarning("Left", "You've left this match.");
	}

	public void processResult(TurnBasedMultiplayer.UpdateMatchResult result) {
		TurnBasedMatch match = result.getMatch();
		toggleSpinner(false);
		if (!checkStatusCode(match, result.getStatus().getStatusCode())) {
			return;
		}
		// FIXME
		// if (match.canRematch()) {
		// askForRematch();
		// }

		isDoingTurn = (match.getTurnStatus() == TurnBasedMatch.MATCH_TURN_STATUS_MY_TURN);

		if (isDoingTurn) {
			updateMatch(match);
			return;
		}

		// setViewVisibility();
	}

	// This is the main function that gets called when players choose a match
	// from the inbox, or else create a match and want to start it.
	public void updateMatch(TurnBasedMatch match) {
		mMatch = match;

		int status = match.getStatus();
		int turnStatus = match.getTurnStatus();

		switch (status) {
		case TurnBasedMatch.MATCH_STATUS_CANCELED:
			showWarning("Canceled!", "This game was canceled!");
			return;
		case TurnBasedMatch.MATCH_STATUS_EXPIRED:
			showWarning("Expired!", "This game is expired.  So sad!");
			return;
		case TurnBasedMatch.MATCH_STATUS_AUTO_MATCHING:
			showWarning("Waiting for auto-match...",
					"We're still waiting for an automatch partner.");
			return;
		case TurnBasedMatch.MATCH_STATUS_COMPLETE:
			if (turnStatus == TurnBasedMatch.MATCH_TURN_STATUS_COMPLETE) {
				showWarning(
						"Complete!",
						"This game is over; someone finished it, and so did you!  There is nothing to be done.");
				break;
			}

			// Note that in this state, you must still call "Finish" yourself,
			// so we allow this to continue.
			showWarning("Complete!",
					"This game is over; someone finished it!  You can only finish it now.");
		}

		// OK, it's active. Check on turn status.
		switch (turnStatus) {
		case TurnBasedMatch.MATCH_TURN_STATUS_MY_TURN:
			showWarning("My turn", "My turn.");
			goToGame(mMatch);
			return;
		case TurnBasedMatch.MATCH_TURN_STATUS_THEIR_TURN:
			// Should return results.
			showWarning("Alas...", "It's not your turn.");
			break;
		case TurnBasedMatch.MATCH_TURN_STATUS_INVITED:
			showWarning("Good inititative!",
					"Still waiting for invitations.\n\nBe patient!");
		}

		// mTurnData = null;

		// setViewVisibility();
	}

	public void showErrorMessage(TurnBasedMatch match, int statusCode,
			int stringId) {

		showWarning("Warning", getResources().getString(stringId));
	}

	// Generic warning/info dialog
	public void showWarning(String title, String message) {
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);

		// set title
		alertDialogBuilder.setTitle(title).setMessage(message);

		// set dialog message
		alertDialogBuilder.setCancelable(false).setNegativeButton("OK",
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int id) {
						// if this button is clicked, close
						// current activity
					}
				});

		// create alert dialog
		mAlertDialog = alertDialogBuilder.create();

		// show it
		mAlertDialog.show();
	}

	// Returns false if something went wrong, probably. This should handle
	// more cases, and probably report more accurate results.
	private boolean checkStatusCode(TurnBasedMatch match, int statusCode) {
		switch (statusCode) {
		case GamesStatusCodes.STATUS_OK:
			return true;
		case GamesStatusCodes.STATUS_NETWORK_ERROR_OPERATION_DEFERRED:
			// This is OK; the action is stored by Google Play Services and will
			// be dealt with later.
			return true;
		case GamesStatusCodes.STATUS_MULTIPLAYER_ERROR_NOT_TRUSTED_TESTER:
			showErrorMessage(match, statusCode,
					R.string.status_multiplayer_error_not_trusted_tester);
			break;
		case GamesStatusCodes.STATUS_MATCH_ERROR_ALREADY_REMATCHED:
			showErrorMessage(match, statusCode,
					R.string.match_error_already_rematched);
			break;
		case GamesStatusCodes.STATUS_NETWORK_ERROR_OPERATION_FAILED:
			showErrorMessage(match, statusCode,
					R.string.network_error_operation_failed);
			break;
		case GamesStatusCodes.STATUS_CLIENT_RECONNECT_REQUIRED:
			showErrorMessage(match, statusCode,
					R.string.client_reconnect_required);
			break;
		case GamesStatusCodes.STATUS_INTERNAL_ERROR:
			showErrorMessage(match, statusCode, R.string.internal_error);
			break;
		case GamesStatusCodes.STATUS_MATCH_ERROR_INACTIVE_MATCH:
			showErrorMessage(match, statusCode,
					R.string.match_error_inactive_match);
			break;
		case GamesStatusCodes.STATUS_MATCH_ERROR_LOCALLY_MODIFIED:
			showErrorMessage(match, statusCode,
					R.string.match_error_locally_modified);
			break;
		default:
			showErrorMessage(match, statusCode, R.string.unexpected_status);
			Log.d(TAG, "Did not have warning or string to deal with: "
					+ statusCode);
		}

		return false;
	}

	private void goToGame(TurnBasedMatch match) {
		mMatch = match;
		toggleSpinner(false);
		// TODO open unity activity
	};

	@Override
	public void onSaveInstanceState(Bundle savedInstanceState) {
		savedInstanceState.putBoolean("challenge", isAChallenge);
		super.onSaveInstanceState(savedInstanceState);
	}

	public void fakePlay(View v) {
		toggleSpinner(true);
		CurrentPlayer player = GlobalRes.getCurrentPlayer();
		if (isNotification && !isSignedIn()) {
			return;
		}

		if (player.getOranges() >= GlobalRes.orangesPerPlay) {

			Intent intent = new Intent(this, ScoreUpdateActivity.class);
			intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			intent.putExtra("Battle_Score",
					(int) Math.ceil(Math.random() * 100));
			if (mMatch != null)
				intent.putExtra("Battle_MatchId", mMatch.getMatchId());
			startActivity(intent);

			// String playerId =
			// Games.Players.getCurrentPlayerId(getApiClient());
			// String myParticipantId = mMatch.getParticipantId(playerId);
			//
			//
			// ArrayList<String> participantIds = mMatch.getParticipantIds();
			//
			// int desiredIndex = -1;
			//
			// for (int i = 0; i < participantIds.size(); i++) {
			// if (participantIds.get(i).equals(myParticipantId)) {
			// desiredIndex = i + 1;
			// }
			// }
			//
			// String nextParticipantId;
			// if (desiredIndex < participantIds.size()) {
			// nextParticipantId = participantIds.get(desiredIndex);
			// } else if (mMatch.getAvailableAutoMatchSlots() <= 0) {
			// // You've run out of automatch slots, so we start over.
			// nextParticipantId = participantIds.get(0);
			// } else {
			// // You have not yet fully automatched, so null will find a new
			// // person to play against.
			// nextParticipantId = null;
			// }
			//
			// byte[] oldData = mMatch.getData();
			// Map<String, Integer> scoreMap = decode(oldData);
			// if (scoreMap == null) {
			// scoreMap = new HashMap<String, Integer>();
			// }
			// scoreMap.put(playerId, (int) Math.ceil(Math.random() * 100));
			//
			// if (scoreMap.size() >= 2) {
			// endMatch();
			// return;
			// }
			//
			// Games.TurnBasedMultiplayer
			// .takeTurn(getApiClient(), mMatch.getMatchId(),
			// encode(scoreMap), nextParticipantId)
			// .setResultCallback(
			// new ResultCallback<TurnBasedMultiplayer.UpdateMatchResult>() {
			// @Override
			// public void onResult(UpdateMatchResult result) {
			// mMatch = result.getMatch();
			// endMatch();
			// }
			// });

		} else {
			// FIXME string as resources
			showWarning("Peccato!", "Non hai abbastanza arance");
		}

	}

	private void endMatch() {
		Games.TurnBasedMultiplayer.finishMatch(getApiClient(),
				mMatch.getMatchId()).setResultCallback(
				new ResultCallback<TurnBasedMultiplayer.UpdateMatchResult>() {
					@Override
					public void onResult(
							TurnBasedMultiplayer.UpdateMatchResult result) {
						Log.d("UPDATE_MATCH_RESULT", result.getStatus()
								.toString());

					}
				});
		Intent intent = new Intent(this, ScoreUpdateActivity.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		intent.putExtra("Battle_Score", (int) Math.ceil(Math.random() * 100));
		intent.putExtra("Battle_MatchId", mMatch.getMatchId());
		startActivity(intent);

	}

	// Convert score in byteArray
	private byte[] encode(Map<String, Integer> map) {
		JSONObject retVal = new JSONObject();

		for (String key : map.keySet()) {
			try {
				retVal.put(key, map.get(key));
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}

		String st = retVal.toString();

		Log.d(TAG, "==== ENCODING\n" + st);

		return st.getBytes(Charset.forName("UTF-16"));
	}

	private Map<String, Integer> decode(byte[] byteArray) {

		if (byteArray == null) {
			Log.d(TAG, "Empty array---possible bug.");
			return null;
		}

		String st = null;
		try {
			st = new String(byteArray, "UTF-16");
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
			return null;
		}

		Log.d(TAG, "====DECODING \n" + st);

		try {
			JSONObject jObject = new JSONObject(st);
			Iterator<?> keys = jObject.keys();

			Map<String, Integer> scoreMap = new HashMap<String, Integer>();
			while (keys.hasNext()) {
				String key = (String) keys.next();
				int value = jObject.getInt(key);
				scoreMap.put(key, value);
			}

			return scoreMap;
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public void onSignInFailed() {
		if (isAChallenge)
			signinPopup.setVisibility(View.VISIBLE);
	}

	@Override
	public void onSignInSucceeded() {
		signinPopup.setVisibility(View.INVISIBLE);
		GlobalRes.getCurrentPlayer().updateInfo(getApiClient());

		if (isAChallenge) {
			startChallenge();
		}

		if (isNotification) {
			Log.d(TAG, "bla bla");

			// If I came from a notification of my turn, I have mMatch
			if (mMatch != null) {
				updateMatch(mMatch);
				return;
			}
		}
	}

	public void goToMap(View view) {
		Intent intent = new Intent(this, MapPane.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(intent);
	}

	public void onClick(View view) {
		if (view.getId() == R.id.sign_in_button) {
			beginUserInitiatedSignIn();
		}
	}

	// Handle notification events.
	@Override
	public void onInvitationReceived(Invitation invitation) {
		Toast.makeText(
				this,
				"An invitation has arrived from "
						+ invitation.getInviter().getDisplayName(), TOAST_DELAY)
				.show();
	}

	@Override
	public void onInvitationRemoved(String invitationId) {
		Toast.makeText(this, "An invitation was removed.", TOAST_DELAY).show();
	}

	@Override
	public void onTurnBasedMatchReceived(TurnBasedMatch match) {
		Toast.makeText(this, "A match was updated.", TOAST_DELAY).show();
	}

	@Override
	public void onTurnBasedMatchRemoved(String matchId) {
		Toast.makeText(this, "A match was removed.", TOAST_DELAY).show();
	}

	public void toggleSpinner(boolean show) {
		int visibility = (show) ? View.VISIBLE : View.GONE;
		findViewById(R.id.progressLayout).setVisibility(visibility);
	}

}
