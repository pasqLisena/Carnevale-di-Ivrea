package it.polito.applicazionimultimediali.carnevalediivrea.minigame;

import android.content.Intent;
import android.view.View;
import it.polito.applicazionimultimediali.carnevalediivrea.R;

public class PredaInDora extends Minigame {
	
	public PredaInDora(){
		descr = "Questa � la storia del Podest�. Per piacere, finiscimi!";
		descrImg = R.drawable.mg_podesta;
	}
	
	@Override
	public void play(View v) {
		//TODO controlla se ho gi� giocato oggi
		Intent intent = new Intent(this, PredaInDoraPlay.class);
		// intent.putExtra(EXTRA_MESSAGE, message);
		startActivity(intent);
	}

}
