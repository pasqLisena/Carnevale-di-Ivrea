package it.polito.applicazionimultimediali.carnevalediivrea.battle;


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
		

		Log.d("MyBattleActivity", "onCreate called!");

	}
	public void goToPlaceActivity(Context c) {
		Log.d("MainActivity", "next Android called");
		Intent intent = new Intent(c, ScoreUpdateActivity.class);
		isBack = false; 
		c.startActivity(intent);
	}
	public static void Launch(Activity activity)
	{
		Intent myIntent = new Intent();
		activity.startActivity(myIntent);
	}
	
	
	public static void next(Context c){
		Log.d("MainActivity", "next Android called");
		//Intent intent = new Intent(c, ScoreUpdateActivity.class);
		isBack = false; 
		
		((Activity) context).startActivityForResult(new Intent(c, ScoreUpdateActivity.class), 1);
		((Activity) context).finish();
		
	}
	
	public static void back(Context c){
		Log.d("MainActivity", "back Android called");
		isBack = true;
		//((Activity) context).finish();
		((Activity) context).startActivityForResult(new Intent(c, ExitActivity.class), 1);
	}


	//@Override
	//protected void onPause() {
	//	if(isBack)
	//		super.onDestroy();
	//	else
	//		super.onPause();
	//}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(requestCode == 1 && resultCode == RESULT_OK){
			((Activity) context).finish();
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	
}
