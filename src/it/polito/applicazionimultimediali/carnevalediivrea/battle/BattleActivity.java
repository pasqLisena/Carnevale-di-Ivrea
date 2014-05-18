package it.polito.applicazionimultimediali.carnevalediivrea.battle;

import it.polito.applicazionimultimediali.carnevalediivrea.CurrentPlayer;
import it.polito.applicazionimultimediali.carnevalediivrea.GlobalRes;
import it.polito.applicazionimultimediali.carnevalediivrea.Player;
import it.polito.applicazionimultimediali.carnevalediivrea.R;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class BattleActivity extends Activity {


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.battle_activity);
		
	}

	
	public void fakePlay(View v) {
		CurrentPlayer player = GlobalRes.getCurrentPlayer();
		String text;

		if (player.playAGame()) {
			text = "Partita effettuata";
		} else {
			text = "Non hai abbastanza arance";
		}

		Toast toast = Toast.makeText(this, text, Toast.LENGTH_SHORT);
		toast.show();

	}


}
