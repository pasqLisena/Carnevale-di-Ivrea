package it.polito.applicazionimultimediali.carnevalediivrea.minigame;

import it.polito.applicazionimultimediali.carnevalediivrea.R;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public abstract class Minigame extends Activity {
	static String descr;
	static int descrImg;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.start_minigame_activity);

		((TextView) findViewById(R.id.descrStartMinigame)).setText(descr);
		((ImageView)findViewById(R.id.bgImageMinigame)).setImageResource(descrImg);
	}

	abstract public void play(View v);
}
