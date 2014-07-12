package it.polito.applicazionimultimediali.carnevalediivrea.minigame;

import it.polito.applicazionimultimediali.carnevalediivrea.R;
import android.content.Intent;
import android.view.View;

public class Fagiolata extends Minigame {
	private static final String TAG = "Fagiolata";

	@Override
	public void play(View v) {
		Intent intent = new Intent(this, FagiolataPlay.class);
		startActivity(intent);
		overridePendingTransition(R.anim.fadein_fagiolata,
				R.anim.zoom_in_fagiolata);

	}
}
