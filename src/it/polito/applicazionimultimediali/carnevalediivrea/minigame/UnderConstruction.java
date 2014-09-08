package it.polito.applicazionimultimediali.carnevalediivrea.minigame;

import it.polito.applicazionimultimediali.carnevalediivrea.R;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class UnderConstruction extends Minigame {
	private static final String TAG = "UnderConstruction";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		findViewById(R.id.character).setVisibility(View.GONE);
		findViewById(R.id.descrScrollView).setVisibility(View.GONE);
		findViewById(R.id.startMinigameFullLayout).setBackgroundResource(R.drawable.place_under_construction_bg);
		((Button) findViewById(R.id.btnStartMinigame)).setText(R.string.button_back);
	}
	
	@Override
	public void play(View v) {
		finish();
	}

	
}
