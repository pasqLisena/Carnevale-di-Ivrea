package it.polito.applicazionimultimediali.carnevalediivrea.minigame;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import it.polito.applicazionimultimediali.carnevalediivrea.R;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

public class PredaInDora extends Minigame {
	private View btnStartMinigame, mgTimerTextA;
	private ProgressBar mgTimerBar;
	private TextView mgTimerText;

//	private final int hour = 60 * 60 * 1000;
	private final int hour = 1;
	private long lastPlay;
	private Date nextPlay;

	public PredaInDora() {
		descr = "Colpisci una pietra del Castellazzo per vincere le arance!";
		descrImg = R.drawable.predaindora_bg;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.start_minigame_activity);
		
		btnStartMinigame = findViewById(R.id.btnStartMinigame);
		mgTimerBar = (ProgressBar) findViewById(R.id.mgTimerBar);
		mgTimerText = (TextView) findViewById(R.id.mgTimerText);
		mgTimerTextA = findViewById(R.id.mgTimerTextA);

		lastPlay = getPreferences(Context.MODE_PRIVATE).getLong("lastPlay", 30);
		nextPlay = new Date(lastPlay + 4 * hour);
		mgTimerBar.setMax(4 * hour);

		if (new Date().before(nextPlay)) {
			btnStartMinigame.setVisibility(View.INVISIBLE);
			startTimer();
		} else {
			mgTimerBar.setVisibility(View.INVISIBLE);
			mgTimerText.setVisibility(View.INVISIBLE);
			mgTimerTextA.setVisibility(View.INVISIBLE);
		}
	}

	@Override
	public void play(View v) {
		Intent intent = new Intent(this, PredaInDoraPlay.class);
		// intent.putExtra(EXTRA_MESSAGE, message);
		startActivity(intent);

		SharedPreferences.Editor prefEditor = getPreferences(
				Context.MODE_PRIVATE).edit();

		lastPlay = new Date().getTime();
		prefEditor.putLong("lastPlay", lastPlay);
		prefEditor.commit();
		nextPlay = new Date(lastPlay + 4 * hour);

		btnStartMinigame.setVisibility(View.INVISIBLE);
		mgTimerBar.setVisibility(View.VISIBLE);
		mgTimerText.setVisibility(View.VISIBLE);
		mgTimerTextA.setVisibility(View.VISIBLE);

		startTimer();
	}

	private void startTimer() {
		CountDownTimer timer = new CountDownTimer(nextPlay.getTime()
				- new Date().getTime(), 1000) {

			@Override
			public void onTick(long millisUntilFinished) {
				SimpleDateFormat df = new SimpleDateFormat("HH:mm:ss",
						Locale.US);
				df.setTimeZone(TimeZone.getTimeZone("GMT"));
				mgTimerText.setText(df.format(new Date(millisUntilFinished)));
				mgTimerBar
						.setProgress((int) (mgTimerBar.getMax() - millisUntilFinished));
			}

			@Override
			public void onFinish() {
				btnStartMinigame.setVisibility(View.VISIBLE);
				mgTimerBar.setVisibility(View.INVISIBLE);
				mgTimerText.setVisibility(View.INVISIBLE);
				mgTimerTextA.setVisibility(View.INVISIBLE);

			}
		};

		timer.start();
	}

}
