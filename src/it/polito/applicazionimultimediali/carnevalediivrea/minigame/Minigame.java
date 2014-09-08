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
			mg_bg = savedInstanceState.getString("mg_bg");
			descr = savedInstanceState.getString("mg_descr");
			mg_mask = savedInstanceState.getString("mg_mask");
		}

		renderView();
	}

	@Override
	public void onRestoreInstanceState(Bundle savedInstanceState) {
		super.onRestoreInstanceState(savedInstanceState);
		mg_bg = savedInstanceState.getString("mg_bg");
		descr = savedInstanceState.getString("mg_descr");
		mg_mask = savedInstanceState.getString("mg_mask");

		renderView();
	}

	private void renderView() {
		if (mg_mask == null) {
			finish();
			return;
		}
		int mask = getResources().getIdentifier(mg_mask, "drawable",
				getPackageName());
		if (mask != 0) {
			((ImageView) findViewById(R.id.character)).setImageResource(mask);
			((ImageView) findViewById(R.id.character2)).setImageResource(mask);
		}
		if (mg_bg != null) {
			int bg = getResources().getIdentifier(mg_bg, "drawable", getPackageName());
			if (bg != 0)
				findViewById(R.id.startMinigameFullLayout)
						.setBackgroundResource(bg);
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
