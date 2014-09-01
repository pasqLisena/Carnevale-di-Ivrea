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
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.DecelerateInterpolator;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.games.Games;
import com.google.android.gms.games.multiplayer.turnbased.TurnBasedMatch;
import com.google.android.gms.games.multiplayer.turnbased.TurnBasedMultiplayer;
import com.google.android.gms.games.multiplayer.turnbased.TurnBasedMultiplayer.LoadMatchResult;
import com.google.android.gms.games.multiplayer.turnbased.TurnBasedMultiplayer.UpdateMatchResult;
import com.google.example.games.basegameutils.BaseGameActivity;

public class ScoreUpdateActivity extends BaseGameActivity {
	final static int MATCH_SINGLE = 0;
	final static int MATCH_WON = 1;
	final static int MATCH_LOST = 2;
	final static int MATCH_PAIR = 3;

	TextView newScoreView, totalScoreView, scoreToAddView, yourScoreView,
			waitYourOpponentView, youGainView;
	int newScore, oldScore, otherScore, numOranges;
	private String matchId;
	private TurnBasedMatch mMatch;
	private boolean updated;
	private boolean multiplayer;
	private int turn;
	private Map<String, Integer> scoreMap;
	private boolean resumed;
	private TextView yourScView;
	private TextView otherScView;
	private Animation fadeIn;
	private int realScore;
	private int incrScore, incrSpeed;
	private SharedPreferences prefs;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.score_update_activity);

		GlobalRes.prepareResources(getApplicationContext());

		newScoreView = (TextView) findViewById(R.id.newScore);
		totalScoreView = (TextView) findViewById(R.id.totalScore);
		scoreToAddView = (TextView) findViewById(R.id.scoreToAdd);
		yourScoreView = (TextView) findViewById(R.id.yourScore);
		waitYourOpponentView = (TextView) findViewById(R.id.waitYourOpponent);
		youGainView = (TextView) findViewById(R.id.youGain);

		if (savedInstanceState != null) {
			newScore = savedInstanceState.getInt("Battle_Score");
			realScore = savedInstanceState.getInt("Real_Score");
			updated = savedInstanceState.getBoolean("Score_Updated", false);
			matchId = savedInstanceState.getString("Battle_MatchId", null);
		} else {
			prefs = getSharedPreferences(
					"it.polito.applicazionimultimediali.carnevalediivrea",
					MODE_PRIVATE);

			newScore = prefs.getInt("Battle_Score", 0);
			numOranges = prefs.getInt("Battle_NumAranceRimaste", 0);
			GlobalRes.getCurrentPlayer().setOranges(numOranges);
			matchId = prefs.getString("Battle_MatchId", null);
			
			// remove from sharedPref for avoid duplicates and save memory
			SharedPreferences.Editor editor = prefs.edit();
			editor.remove("Battle_MatchId");
			editor.remove("Battle_Score");
			editor.remove("Battle_NumAranceRimaste");
			editor.commit();
		}

		newScoreView.setText(getResources().getQuantityString(R.plurals.point,
				newScore, newScore));

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
		realScore = newScore;

		// single game --> save score!
		updateUserScore(newScore);
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		outState.putInt("Battle_Score", newScore);
		outState.putInt("Real_Score", realScore);
		outState.putString("Battle_MatchId", matchId);
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

								if (turn == 2) {
									String myId = Games.Players
											.getCurrentPlayerId(getApiClient());
									otherScore = 0;
									for (String idString : scoreMap.keySet()) {
										if (idString.equals(myId))
											newScore = scoreMap.get(idString);
										else
											otherScore = scoreMap.get(idString);
									}

									realScore = 0;
									if (newScore > otherScore) {
										// you win
										realScore = 2 * newScore;
									} else if (otherScore > newScore) {
										// you loose
										realScore = -otherScore;
									} // else pair! nothing happens

									GlobalRes.getCurrentPlayer().gainPoint(
											realScore, getApiClient());

								} // else you must wait other player

								toggleSpinner(false);
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

		// FIXME se ho già eseguito le animazioni, torna subito alla mappa

		resumed = true;

		if (!multiplayer) {
			startSingleBattleAnimation(0);
			return;
		}

		if (turn < 1) {
			// multiplayer data not still available
			toggleSpinner(true);
		} else {
			startMultiplayerAnimation();
		}
	}

	private void startMultiplayerAnimation() {
		toggleSpinner(false);
		fadeIn = AnimationUtils.loadAnimation(this, R.anim.su_fadein);
		fadeIn.setFillAfter(true);
		final Handler mHandler = new Handler();

		switch (turn) {
		case 1:
			long animDuration = youGainAnim();
			waitYourOpponentView.setVisibility(View.VISIBLE);
			waitYourOpponentView.startAnimation(fadeIn);

			mHandler.postDelayed(new Runnable() {
				@Override
				public void run() {
					goToMap();
				}
			}, animDuration + 3000);

			break;
		default:
			findViewById(R.id.mpLayout).setVisibility(View.VISIBLE);
			yourScView = (TextView) findViewById(R.id.mpYourScoreValue);
			otherScView = (TextView) findViewById(R.id.mpOtherScoreValue);
			yourScView.setText(0 + "");
			otherScView.setText(0 + "");

			final Runnable increaseScoreMP = new Runnable() {
				int yI = 0, oI = 0;
				private int code;

				@Override
				public void run() {
					if (yI == newScore && oI == otherScore) {
						View toShowView = null;
						code = 0;
						if (newScore > otherScore) {
							toShowView = findViewById(R.id.youWin);
							code = MATCH_WON;
						} else if (newScore == otherScore) {
							toShowView = findViewById(R.id.pair);
							code = MATCH_PAIR;
						} else {
							toShowView = findViewById(R.id.youLoose);
							code = MATCH_LOST;
						}

						Animation in = new AlphaAnimation(0, 1);
						in.setInterpolator(new DecelerateInterpolator());

						Animation out = new AlphaAnimation(1, 0);
						out.setInterpolator(new AccelerateInterpolator());

						in.setStartOffset(500);
						in.setDuration(300);
						in.setFillAfter(true);

						out.setStartOffset(1500);
						out.setDuration(300);
						out.setFillAfter(true);

						toShowView.setVisibility(View.VISIBLE);
						toShowView.startAnimation(in);

						findViewById(R.id.mpLayout).startAnimation(out);

						mHandler.postDelayed(new Runnable() {
							@Override
							public void run() {
								startSingleBattleAnimation(code);
							}
						}, 1000);
						return;
					}

					if (yI < newScore)
						yI++;
					if (oI < otherScore)
						oI++;

					yourScView.setText(yI + "");
					otherScView.setText(oI + "");

					if (yI < newScore || oI < otherScore) {
						mHandler.postDelayed(this, 1 / 20); // 20fps
					} else {
						mHandler.postDelayed(this, 0);
					}
				}
			};
			mHandler.postDelayed(increaseScoreMP, 1000);

			break;
		}
	}

	private void startSingleBattleAnimation(final int code) {
		oldScore = GlobalRes.getCurrentPlayer().getPoints();

		newScoreView.setText(getResources().getQuantityString(R.plurals.point,
				newScore, newScore));
		totalScoreView.setText(oldScore + "");

		incrSpeed = 1;
		String scoreToAdd = "";
		switch (code) {
		case MATCH_SINGLE:
			incrScore = newScore;
			scoreToAdd = "+" + newScore;
			break;
		case MATCH_WON:
			incrScore = realScore;
			incrSpeed = 2;
			scoreToAdd = "+" + newScore + " X2";
			break;
		case MATCH_LOST:
			incrScore = realScore;
			incrSpeed = -1;
			scoreToAdd = realScore + "";
			break;
		case MATCH_PAIR:
			incrScore = 0;
			scoreToAdd = "0";
		}

		scoreToAddView.setText(scoreToAdd);

		toggleSpinner(false);

		long delay1 = 0;
		if (code == MATCH_SINGLE) {
			delay1 = youGainAnim();
			fadeIn = AnimationUtils.loadAnimation(this, R.anim.su_fadein);
		} else {
			fadeIn = new AlphaAnimation(0, 1);
			fadeIn.setInterpolator(new DecelerateInterpolator());

			fadeIn.setStartOffset(500);
			fadeIn.setDuration(300);
		}
		long delay2 = fadeIn.getDuration() + 500;

		yourScoreView.setVisibility(View.VISIBLE);
		totalScoreView.setVisibility(View.VISIBLE);
		scoreToAddView.setVisibility(View.VISIBLE);

		yourScoreView.startAnimation(fadeIn);
		totalScoreView.startAnimation(fadeIn);
		scoreToAddView.startAnimation(fadeIn);

		final Handler mHandler = new Handler();
		final Runnable increaseScore = new Runnable() {
			@Override
			public void run() {
				if (Math.abs(incrScore) == 0) {
					goToMap();
					return;
				}

				oldScore += incrSpeed;
				incrScore = incrScore - incrSpeed;

				totalScoreView.setText(oldScore + "");
				String scoreAdd = "";
				switch (code) {
				case MATCH_SINGLE:
					scoreAdd = "+" + incrScore;
					break;
				case MATCH_WON:
					scoreAdd = "+" + incrScore + " X2";
					break;
				case MATCH_LOST:
					scoreAdd = incrScore + "";
					break;
				case MATCH_PAIR:
					scoreAdd = "0";
				}
				scoreToAddView.setText(scoreAdd);

				if (Math.abs(incrScore) > 0) {
					mHandler.postDelayed(this, 1 / 20); // 20fps
				} else {
					mHandler.postDelayed(this, 1500);
				}
			}
		};
		mHandler.postDelayed(increaseScore, delay1 + delay2);
	}

	private long youGainAnim() {
		Animation goRightAnim = AnimationUtils.loadAnimation(this,
				R.anim.su_go_right);
		Animation goLeftAnim = AnimationUtils.loadAnimation(this,
				R.anim.su_go_left);

		goRightAnim.setFillAfter(true);
		goLeftAnim.setFillAfter(true);

		newScoreView.setVisibility(View.VISIBLE);
		youGainView.setVisibility(View.VISIBLE);
		newScoreView.startAnimation(goLeftAnim);
		youGainView.startAnimation(goRightAnim);

		return goLeftAnim.getDuration();
	}

	private void goToMap() {
		Intent intent = new Intent(this, MapPane.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(intent);
	}

	@Override
	public void onSignInFailed() {
		// TODO move string in xml
		Toast.makeText(this, "Problemi di connessione, riprova più tardi",
				Toast.LENGTH_SHORT).show();
		goToMap();
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
								turn = 1;
								if (scoreMap.size() >= 2) {
									turn = 2;
									endMatch();
									return;
								}
								scoreMap.put(playerId, newScore);

								if (scoreMap.size() >= 2) {
									turn = 2;
									Games.TurnBasedMultiplayer
											.finishMatch(getApiClient(),
													mMatch.getMatchId(),
													encode(scoreMap))
											.setResultCallback(
													new ResultCallback<TurnBasedMultiplayer.UpdateMatchResult>() {
														@Override
														public void onResult(
																UpdateMatchResult result) {
															mMatch = result
																	.getMatch();

															// It is my last
															// (only)
															// turn --> call
															// endMatch
															endMatch();
														}
													});
								} else
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

															// It is my last
															// (only)
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
