package it.polito.applicazionimultimediali.carnevalediivrea.minigame;

import it.polito.applicazionimultimediali.carnevalediivrea.R;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public abstract class Minigame extends Activity {
	static String descr;
	static int descrImg;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.start_minigame_activity);

		String mg_bg = null;
		String mg_descr = null;
		if (savedInstanceState == null) {
			Bundle extras = getIntent().getExtras();
			if (extras != null) {
				mg_bg = extras.getString("mg_bg");
				mg_descr = extras.getString("mg_descr");
			}
		} else {
			mg_bg = (String) savedInstanceState.getSerializable("mg_bg");
			mg_descr = (String) savedInstanceState.getSerializable("mg_descr");
		}

		int mgBg = getResources().getIdentifier(mg_bg, "drawable", null);

		
		findViewById(R.id.startMinigameFullLayout).setBackgroundResource(mgBg);

		((TextView) findViewById(R.id.descrStartMinigame)).setText(mg_descr);
//		((ImageView)findViewById(R.id.bgImageMinigame)).setImageResource(descrImg);
	}

	abstract public void play(View v);
}
