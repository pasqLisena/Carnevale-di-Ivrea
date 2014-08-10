package it.polito.applicazionimultimediali.carnevalediivrea.battle;

import it.polito.applicazionimultimediali.carnevalediivrea.CurrentPlayer;
import it.polito.applicazionimultimediali.carnevalediivrea.GlobalRes;
import it.polito.applicazionimultimediali.carnevalediivrea.map.PlaceActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.unity3d.player.UnityPlayerActivity;


public class MyBattleActivity extends UnityPlayerActivity {

	private CurrentPlayer player;
	private SharedPreferences prefs;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		prefs = getSharedPreferences("it.polito.applicazionimultimediali.carnevalediivrea", MODE_PRIVATE);
		SharedPreferences.Editor editor = prefs.edit();
		player = GlobalRes.getCurrentPlayer();
		
		editor.putString("Battle_Piazza", "PiazzaDiCittà");//da sostituire con metodo place.name()
		editor.putString("Battle_Team", "morte");//da sostituire con metodo player.team()
		editor.putString("Battle_OpposingTeam", "picche");//da sostituire con metodo player.OpposingTeam()
		
		//una volta chiuso il gioco salvo il punteggio in SharedPreferences in una variabile di tipo int "Battle_Score"
		
		editor.commit();
		

		Log.d("MyBattleActivity", "onCreate called!");

	}
	public void goToPlaceActivity() {
		Intent intent = new Intent(this, PlaceActivity.class);
		// intent.putExtra(EXTRA_MESSAGE, message);
		startActivity(intent);
	}
}
