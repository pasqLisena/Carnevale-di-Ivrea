package it.polito.applicazionimultimediali.carnevalediivrea.battle;


import it.polito.applicazionimultimediali.carnevalediivrea.GlobalRes;
import it.polito.applicazionimultimediali.carnevalediivrea.Team;
import it.polito.applicazionimultimediali.carnevalediivrea.map.MapPane;
import it.polito.applicazionimultimediali.carnevalediivrea.map.Place;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import com.unity3d.player.UnityPlayerActivity;


public class MyBattleActivity extends UnityPlayerActivity {

	private static SharedPreferences prefs;
	static Context context ;
	static boolean isBack = false;
	private Intent myIntent;
	private String firstTime;
	private Place place;
	private Team oppTeam;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		context = this;
		String placeId = null;
		String oppTeamName = null;
		if (savedInstanceState == null) {
			Bundle extras = getIntent().getExtras();
			if (extras != null) {
				placeId = extras.getString("place");
				oppTeamName = extras.getString("oppteam");
			}
		} else {
			placeId = (String) savedInstanceState.getSerializable("place");
			oppTeamName = (String) savedInstanceState.getSerializable("oppteam");
		}
		
		place = GlobalRes.placesList.get(Integer.parseInt(placeId));

		
		GlobalRes.prepareResources(getApplicationContext());
		
		prefs = getSharedPreferences("it.polito.applicazionimultimediali.carnevalediivrea", MODE_PRIVATE);
		SharedPreferences.Editor editor = prefs.edit();
		
		Log.d("MyBattleActivity", "oppTeamName: " + oppTeamName);
		
		
		editor.putInt("Battle_NumArance", GlobalRes.getCurrentPlayer().getOranges());
		editor.putString("Battle_Piazza", place.getName());
		editor.putString("Battle_Team", "Morte");
		editor.putString("Battle_OpposingTeam", oppTeamName.toString());
		
		
		editor.putString("Battle_FirstBattle", prefs.getString("Battle_FirstBattle", "true"));
		
		
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
		((Activity) context).startActivityForResult(intent, 1);
	}
	
	public static void tornaAllaMappa(Context c){
		Log.d("MyBattleActivity", "tornaAllaMappa Android called");
		
		Intent intent = new Intent(c, MapPane.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		isBack = true;
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
