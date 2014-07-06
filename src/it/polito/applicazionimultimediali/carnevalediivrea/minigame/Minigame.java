package it.polito.applicazionimultimediali.carnevalediivrea.minigame;

import it.polito.applicazionimultimediali.carnevalediivrea.R;
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

public abstract class Minigame extends Activity {
	protected String descr;
	protected int bg;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.start_minigame_activity);

		String mg_bg = null;
		String descr = null;
		if (savedInstanceState == null) {
			Bundle extras = getIntent().getExtras();
			if (extras != null) {
				mg_bg = extras.getString("mg_bg");
				descr = extras.getString("mg_descr");
			}
		} else {
			mg_bg = (String) savedInstanceState.getSerializable("mg_bg");
			descr = (String) savedInstanceState.getSerializable("mg_descr");
		}

		if (mg_bg != null) {
			int bg = getResources().getIdentifier(mg_bg, "drawable", null);
			if (bg != 0)
				findViewById(R.id.startMinigameFullLayout)
						.setBackgroundResource(bg);
		}

		((TextView) findViewById(R.id.descrStartMinigame)).setText(descr);
		// ((ImageView)findViewById(R.id.bgImageMinigame)).setImageResource(descrImg);
	}

	abstract public void play(View v);
}
