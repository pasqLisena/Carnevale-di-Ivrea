package it.polito.applicazionimultimediali.carnevalediivrea.battle;

import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import it.polito.applicazionimultimediali.carnevalediivrea.GlobalRes;
import it.polito.applicazionimultimediali.carnevalediivrea.R;
import it.polito.applicazionimultimediali.carnevalediivrea.map.MapPane;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.games.Games;
import com.google.android.gms.games.multiplayer.turnbased.TurnBasedMatch;
import com.google.android.gms.games.multiplayer.turnbased.TurnBasedMultiplayer;
import com.google.android.gms.games.multiplayer.turnbased.TurnBasedMultiplayer.LoadMatchResult;
import com.google.android.gms.games.multiplayer.turnbased.TurnBasedMultiplayer.UpdateMatchResult;
import com.google.example.games.basegameutils.BaseGameActivity;

public class ScoreUpdateActivity<V> extends BaseGameActivity {
	TextView newScoreView, totalScoreView, scoreToAddView;
	int newScore, oldScore;
	private String matchId;
	private TurnBasedMatch mMatch;
	private boolean updated;
	private boolean multiplayer;
	private int turn;
	private Map<String, Integer> scoreMap;
	private boolean resumed;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.score_update_activity);

		if (savedInstanceState != null) {
			newScore = savedInstanceState.getInt("Battle_Score");
			updated = savedInstanceState.getBoolean("Score_Updated", false);
		} else {
			// FIXME maybe it has to be moved to sharedPref
			Bundle extras = getIntent().getExtras();
			if (extras != null) {
				newScore = extras.getInt("Battle_Score");
				matchId = extras.getString("Battle_MatchId", null);
			}
		}

		getGameHelper().setMaxAutoSignInAttempts(0);
		if (matchId != null) {
			multiplayer = true;
			turn = 0;
			if (!isSignedIn())
				beginUserInitiatedSignIn();
			else
				onSignInSucceeded();
			return;
		}
		multiplayer = false;

		// single game --> save score!
		oldScore = GlobalRes.getCurrentPlayer().getPoints();

		newScoreView = (TextView) findViewById(R.id.newScore);
		newScoreView.setText(getResources().getQuantityString(R.plurals.point,
				newScore, newScore));

		totalScoreView = (TextView) findViewById(R.id.totalScore);
		totalScoreView.setText(oldScore + "");
		scoreToAddView = (TextView) findViewById(R.id.scoreToAdd);
		scoreToAddView.setText("+" + newScore);

		updateUserScore(newScore);
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		outState.putInt("Battle_Score", newScore);
		outState.putBoolean("Score_Updated", updated);
		super.onSaveInstanceState(outState);
	}

	private void updateUserScore(int newScore) {
		if (GlobalRes.getCurrentPlayer().isSignedIn() && !isSignedIn()) {
			// wait for signin
			return;
		}
		if (!updated) {
			if (getApiClient().isConnected())
				GlobalRes.getCurrentPlayer()
						.gainPoint(newScore, getApiClient());
			else
				GlobalRes.getCurrentPlayer().gainPoint(newScore, null);
			updated = true;
		}
	}

	private void endMatch() {
		Games.TurnBasedMultiplayer
				.finishMatch(getApiClient(), matchId)
				.setResultCallback(
						new ResultCallback<TurnBasedMultiplayer.UpdateMatchResult>() {
							@Override
							public void onResult(
									TurnBasedMultiplayer.UpdateMatchResult result) {
								Log.d("UPDATE_MATCH_RESULT", result.getStatus()
										.toString());

								// TODO remove spinner
								if (resumed) {
									// start animation
									startMultiplayerAnimation();
								} // else wait onResume
							}
						});

	}

	@Override
	protected void onResume() {
		super.onResume();

		resumed = true;

		if (!multiplayer) {
			startSingleBattleAnimation();
			return;
		}

		if (turn < 1) {
			// multiplayer data not still available
			// TODO spinner
		} else {
			startMultiplayerAnimation();
		}
	}

	private void startMultiplayerAnimation() {
		// TODO remove spinner
	}

	private void startSingleBattleAnimation() {
		Animation goRightAnim = AnimationUtils.loadAnimation(this,
				R.anim.su_go_right);
		Animation goLeftAnim = AnimationUtils.loadAnimation(this,
				R.anim.su_go_left);

		goRightAnim.setFillAfter(true);
		goLeftAnim.setFillAfter(true);

		View youGain = findViewById(R.id.youGain);
		newScoreView.startAnimation(goLeftAnim);
		youGain.startAnimation(goRightAnim);

		Animation fadeIn = AnimationUtils.loadAnimation(this, R.anim.su_fadein);
		View yourScoreView = findViewById(R.id.yourScore);
		long animDuration1 = goLeftAnim.getDuration() + 500;
		long animDuration2 = fadeIn.getDuration() + 500;

		yourScoreView.startAnimation(fadeIn);
		totalScoreView.startAnimation(fadeIn);
		scoreToAddView.startAnimation(fadeIn);

		final Handler mHandler = new Handler();
		final Runnable increaseScore = new Runnable() {
			@Override
			public void run() {
				if (newScore <= 0) {
					goToMap();
					return;
				}

				oldScore++;
				newScore--;

				totalScoreView.setText(oldScore + "");
				scoreToAddView.setText("+" + newScore);

				if (newScore > 0) {
					mHandler.postDelayed(this, 1 / 20); // 20fps
				} else {
					mHandler.postDelayed(this, 1500);
				}
			}
		};
		mHandler.postDelayed(increaseScore, animDuration1 + animDuration2);
	}

	private void goToMap() {
		Intent intent = new Intent(this, MapPane.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(intent);
	}

	@Override
	public void onSignInFailed() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onSignInSucceeded() {
		GlobalRes.getCurrentPlayer().updateInfo(getApiClient());

		if (updated)
			return;

		if (!multiplayer) {
			updateUserScore(newScore);
			return;
		}

		Games.TurnBasedMultiplayer
				.loadMatch(getApiClient(), matchId)
				.setResultCallback(
						new ResultCallback<TurnBasedMultiplayer.LoadMatchResult>() {

							@Override
							public void onResult(LoadMatchResult result) {
								mMatch = result.getMatch();

								String playerId = Games.Players
										.getCurrentPlayerId(getApiClient());
								String myParticipantId = mMatch
										.getParticipantId(playerId);

								ArrayList<String> participantIds = mMatch
										.getParticipantIds();

								int desiredIndex = -1;

								for (int i = 0; i < participantIds.size(); i++) {
									if (participantIds.get(i).equals(
											myParticipantId)) {
										desiredIndex = i + 1;
									}
								}

								String nextParticipantId;
								if (desiredIndex < participantIds.size()) {
									nextParticipantId = participantIds
											.get(desiredIndex);
								} else if (mMatch.getAvailableAutoMatchSlots() <= 0) {
									// You've run out of automatch slots, so we
									// start over.
									nextParticipantId = participantIds.get(0);
								} else {
									// You have not yet fully automatched, so
									// null will find a new
									// person to play against.
									nextParticipantId = null;
								}

								byte[] oldData = mMatch.getData();
								scoreMap = decode(oldData);
								if (scoreMap == null) {
									scoreMap = new HashMap<String, Integer>();
								}
								scoreMap.put(playerId,
										(int) Math.ceil(Math.random() * 100));

								turn = 1;
								if (scoreMap.size() >= 2) {
									turn = 2;
									endMatch();
									return;
								}

								Games.TurnBasedMultiplayer
										.takeTurn(getApiClient(),
												mMatch.getMatchId(),
												encode(scoreMap),
												nextParticipantId)
										.setResultCallback(
												new ResultCallback<TurnBasedMultiplayer.UpdateMatchResult>() {
													@Override
													public void onResult(
															UpdateMatchResult result) {
														mMatch = result
																.getMatch();
														// It is my last (only)
														// turn --> call
														// endMatch
														endMatch();
													}
												});

							}

						});

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

		Log.d("ENCODING", "==== ENCODING\n" + st);

		return st.getBytes(Charset.forName("UTF-16"));
	}

	private Map<String, Integer> decode(byte[] byteArray) {

		if (byteArray == null) {
			Log.d("DECODING", "Empty array.");
			return null;
		}

		String st = null;
		try {
			st = new String(byteArray, "UTF-16");
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
			return null;
		}

		Log.d("DECODING", "====DECODING \n" + st);

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
	public void toggleSpinner(boolean show) {
		int visibility = (show) ? View.VISIBLE : View.GONE;
		findViewById(R.id.progressLayout).setVisibility(visibility);
	}

}

