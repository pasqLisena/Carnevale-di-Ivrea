package it.polito.applicazionimultimediali.carnevalediivrea.minigame;

import it.polito.applicazionimultimediali.carnevalediivrea.R;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public abstract class Minigame extends Activity {
	protected String descr, mg_bg, mg_mask;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.start_minigame_activity);

		if (savedInstanceState == null) {
			Bundle extras = getIntent().getExtras();
			if (extras != null) {
				mg_bg = extras.getString("mg_bg");
				descr = extras.getString("mg_descr");
				mg_mask = extras.getString("mg_mask");
			}
		} else {
			mg_bg = (String) savedInstanceState.getSerializable("mg_bg");
			descr = (String) savedInstanceState.getSerializable("mg_descr");
			mg_mask = (String) savedInstanceState.getSerializable("mg_mask");
		}

		if (mg_bg != null) {
			int bg = getResources().getIdentifier(mg_bg, "drawable", null);
			if (bg != 0)
				findViewById(R.id.startMinigameFullLayout)
						.setBackgroundResource(bg);
		}
		if (mg_mask != null) {

			int mask = getResources().getIdentifier(mg_mask, "drawable",
					getPackageName());
			if (mask != 0) {
				((ImageView) findViewById(R.id.character))
						.setImageResource(mask);
				((ImageView) findViewById(R.id.character2))
						.setImageResource(mask);
			}
		}

		((TextView) findViewById(R.id.descrStartMinigame)).setText(descr);
	}

	@Override
	public void onSaveInstanceState(Bundle savedInstanceState) {
		savedInstanceState.putString("mg_bg", mg_bg);
		savedInstanceState.putString("mg_descr", descr);
		savedInstanceState.putString("mg_mask", mg_mask);
		super.onSaveInstanceState(savedInstanceState);
	}

	abstract public void play(View v);
}
