package it.polito.applicazionimultimediali.carnevalediivrea.battle;


import it.polito.applicazionimultimediali.carnevalediivrea.map.MapPane;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import com.unity3d.player.UnityPlayerActivity;


public class MyBattleActivity extends UnityPlayerActivity {

	private SharedPreferences prefs;
	static Context context ;
	static boolean isBack = false;
	private Intent myIntent;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		context = this;
		prefs = getSharedPreferences("it.polito.applicazionimultimediali.carnevalediivrea", MODE_PRIVATE);
		SharedPreferences.Editor editor = prefs.edit();
		
		editor.putString("Battle_Piazza", "PiazzaDiCittà");//da sostituire con metodo place.name()
		editor.putString("Battle_Team", "morte");//da sostituire con metodo player.team()
		editor.putString("Battle_OpposingTeam", "picche");//da sostituire con metodo player.OpposingTeam()
		
		//una volta chiuso il gioco salvo il punteggio in SharedPreferences in una variabile di tipo int "Battle_Score"
		
		editor.commit();
		
		 myIntent = new Intent();
	     myIntent.setAction(Intent.ACTION_VIEW);
	     myIntent.setData(android.provider.MediaStore.Images.Media.INTERNAL_CONTENT_URI);

		Log.d("MyBattleActivity", "onCreate called!");

	}
		
	public static void mostraPunteggio(Context c){
		Log.d("MyBattleActivity", "mostraPunteggio Android called");
		Intent intent = new Intent(c, ScoreUpdateActivity.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		isBack = true;
		//((Activity) context).finish();
		((Activity) context).startActivityForResult(intent, 1);
	}
	
	public static void tornaAllaMappa(Context c){
		Log.d("MyBattleActivity", "tornaAllaMappa Android called");
		Intent intent = new Intent(c, MapPane.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		isBack = true;
		//((Activity) context).finish();
		((Activity) context).startActivityForResult(intent, 1);
	}

	@Override
	protected void onPause() {
		if(isBack)
			super.onDestroy();
		else
			super.onPause();
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(requestCode == 1 && resultCode == RESULT_OK){
			((Activity) context).finish();
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	
}
