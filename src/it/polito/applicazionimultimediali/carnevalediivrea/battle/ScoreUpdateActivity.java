package it.polito.applicazionimultimediali.carnevalediivrea.battle;

import it.polito.applicazionimultimediali.carnevalediivrea.GlobalRes;
import it.polito.applicazionimultimediali.carnevalediivrea.R;
import it.polito.applicazionimultimediali.carnevalediivrea.map.MapPane;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

public class ScoreUpdateActivity extends Activity {
	TextView newScoreView, totalScoreView, scoreToAddView;
	int newScore, oldScore;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.score_update_activity);

		newScore = 100;
		 GlobalRes.getCurrentPlayer().gainPoint(newScore);
		oldScore = GlobalRes.getCurrentPlayer().getPoints();

		newScoreView = (TextView) findViewById(R.id.newScore);
		newScoreView.setText(getResources().getQuantityString(R.plurals.point,
				newScore, newScore));

		totalScoreView = (TextView) findViewById(R.id.totalScore);
		totalScoreView.setText(oldScore + "");
		scoreToAddView = (TextView) findViewById(R.id.scoreToAdd);
		scoreToAddView.setText("+" + newScore);
	}

	@Override
	protected void onResume() {
		super.onResume();

		Animation goRightAnim = AnimationUtils.loadAnimation(this,
				R.anim.su_go_right);
		Animation goLeftAnim = AnimationUtils.loadAnimation(this,
				R.anim.su_go_left);

		newScoreView.startAnimation(goLeftAnim);
		findViewById(R.id.youGain).startAnimation(goRightAnim);

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
				if(newScore<=0){
					goToMap();
					return;
				}
				
				oldScore++;
				newScore--;
				
				totalScoreView.setText(oldScore + "");
				scoreToAddView.setText("+" + newScore);
				
				if (newScore > 0) {
					mHandler.postDelayed(this, 1 / 20); // 20fps
				}else{
					mHandler.postDelayed(this, 1500); 
				}
			}
		};
		mHandler.postDelayed(increaseScore, animDuration1+animDuration2);
	}
	
	
	private void goToMap(){
		Intent intent = new Intent(this, MapPane.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(intent);
	}
}
