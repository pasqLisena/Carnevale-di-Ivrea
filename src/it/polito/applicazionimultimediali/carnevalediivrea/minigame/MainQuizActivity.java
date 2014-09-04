package it.polito.applicazionimultimediali.carnevalediivrea.minigame;

import it.polito.applicazionimultimediali.carnevalediivrea.GlobalRes;
import it.polito.applicazionimultimediali.carnevalediivrea.R;
import it.polito.applicazionimultimediali.carnevalediivrea.map.Place;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

public class MainQuizActivity extends Activity {

	private Place place;
	private Button videoPlay;
	private Button gamePlay;

	private View mgTimerTextA;
	private ProgressBar mgTimerBar;
	private TextView mgTimerText;

	private final int hour = 60 * 60 * 1000;
	private long lastPlay;
	private Date nextPlay;
	private String placeId;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main_quiz);

		// recupero id Luogo
		if (savedInstanceState == null) {
			Bundle extras = getIntent().getExtras();
			if (extras != null) {
				placeId = extras.getString("place");
			}
		} else {
			placeId = (String) savedInstanceState.getSerializable("place");
		}

		if (placeId != null) {
			place = GlobalRes.placesList.get(Integer.parseInt(placeId));

			// setto immagine sfondo secondo id luogo
			String bg_place;
			if (place.getBg() == null || place.getBg() == "")
				bg_place = "place_piazzadicitta_bg";
			else
				bg_place = place.getBg();

			int bg = getResources().getIdentifier(bg_place, "drawable",
					getPackageName());
			if (bg != 0)
				((ImageView) findViewById(R.id.bgQuizPlace))
						.setImageResource(bg);

		}

		// bottone video
		videoPlay = (Button) findViewById(R.id.videoPlay);

		videoPlay.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Intent i1 = new Intent(getBaseContext(), VideoActivity.class);
				i1.putExtra("place", place.getId() + "");
				startActivity(i1);

			}
		});

		mgTimerBar = (ProgressBar) findViewById(R.id.mgTimerBarQuiz);
		mgTimerText = (TextView) findViewById(R.id.mgTimerTextQuiz);
		mgTimerTextA = findViewById(R.id.mgTimerTextAQuiz);

		lastPlay = getPreferences(Context.MODE_PRIVATE).getLong(
				"lastPlayQuiz" + placeId, 30);
		nextPlay = new Date(lastPlay + 4 * hour);
		mgTimerBar.setMax(4 * hour);

		// bottone gioca
		gamePlay = (Button) findViewById(R.id.gamePlay);

		if (new Date().before(nextPlay)) {
			gamePlay.setVisibility(View.INVISIBLE);
			mgTimerBar.setVisibility(View.VISIBLE);
			mgTimerText.setVisibility(View.VISIBLE);
			mgTimerTextA.setVisibility(View.VISIBLE);

			startTimer();
		}

		gamePlay.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				play();
				Intent i2 = new Intent(getBaseContext(), QuizActivity.class);
				i2.putExtra("place", place.getId() + "");
				startActivity(i2);
			}
		});
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		outState.putString("place", placeId);
		super.onSaveInstanceState(outState);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_main_quiz, menu);
		return true;
	}

	public void play() {
		SharedPreferences.Editor prefEditor = getPreferences(
				Context.MODE_PRIVATE).edit();

		lastPlay = new Date().getTime();
		prefEditor.putLong("lastPlayQuiz" + place.getId(), lastPlay);
		prefEditor.commit();
		nextPlay = new Date(lastPlay + 4 * hour);

		gamePlay.setVisibility(View.INVISIBLE);
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
				gamePlay.setVisibility(View.VISIBLE);
				mgTimerBar.setVisibility(View.INVISIBLE);
				mgTimerText.setVisibility(View.INVISIBLE);
				mgTimerTextA.setVisibility(View.INVISIBLE);

			}
		};

		timer.start();
	}

}
