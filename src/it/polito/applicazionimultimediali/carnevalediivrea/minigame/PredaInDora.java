package it.polito.applicazionimultimediali.carnevalediivrea.minigame;

import android.content.Intent;
import android.view.View;
import it.polito.applicazionimultimediali.carnevalediivrea.R;

public class PredaInDora extends Minigame {
	
	public PredaInDora(){
		descr = "Questa è la storia del Podestà. Per piacere, finiscimi!";
		descrImg = R.drawable.mg_podesta;
	}
	
	@Override
	public void play(View v) {
		//TODO controlla se ho già giocato oggi
		Intent intent = new Intent(this, PredaInDoraPlay.class);
		// intent.putExtra(EXTRA_MESSAGE, message);
		startActivity(intent);
	}

}
