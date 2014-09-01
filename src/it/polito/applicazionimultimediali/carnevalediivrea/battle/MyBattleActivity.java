package it.polito.applicazionimultimediali.carnevalediivrea.battle;

import it.polito.applicazionimultimediali.carnevalediivrea.GlobalRes;
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
	static Context context;
	static boolean isBack = false;
	private Intent myIntent;
	private Place place;
	private String matchId;

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
				matchId = extras.getString("Battle_MatchId");
			}
		} else {
			placeId = savedInstanceState.getString("place");
			oppTeamName = savedInstanceState.getString("oppteam");
			matchId = savedInstanceState.getString("Battle_MatchId");
		}

		GlobalRes.prepareResources(getApplicationContext());

		prefs = getSharedPreferences(
				"it.polito.applicazionimultimediali.carnevalediivrea",
				MODE_PRIVATE);
		SharedPreferences.Editor editor = prefs.edit();

		editor.putInt("Battle_NumArance", GlobalRes.getCurrentPlayer()
				.getOranges());
		if (placeId != null) {
			place = GlobalRes.placesList.get(Integer.parseInt(placeId));
			editor.putString("Battle_Piazza", place.getName());
		}
		if (oppTeamName != null)
			editor.putString("Battle_OpposingTeam", oppTeamName.toString());
		editor.putString("Battle_FirstBattle",
				prefs.getString("Battle_FirstBattle", "true"));
		editor.putString("Battle_MatchId", matchId);
		editor.commit();

		myIntent = new Intent();
		myIntent.setAction(Intent.ACTION_VIEW);
		myIntent.setData(android.provider.MediaStore.Images.Media.INTERNAL_CONTENT_URI);

	}

	public static void mostraPunteggio(Context c) {
		Log.d("MyBattleActivity", "mostraPunteggio Android called");

		Intent intent = new Intent(c, ScoreUpdateActivity.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		isBack = true;
		((Activity) context).startActivityForResult(intent, 1);
	}

	public static void tornaAllaMappa(Context c) {
		Log.d("MyBattleActivity", "tornaAllaMappa Android called");

		Intent intent = new Intent(c, MapPane.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		isBack = true;
		((Activity) context).startActivityForResult(intent, 1);
	}

	@Override
	protected void onPause() {
		if (isBack)
			super.onDestroy();
		else
			super.onPause();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == 1 && resultCode == RESULT_OK) {
			((Activity) context).finish();
		}
		super.onActivityResult(requestCode, resultCode, data);
	}
}
