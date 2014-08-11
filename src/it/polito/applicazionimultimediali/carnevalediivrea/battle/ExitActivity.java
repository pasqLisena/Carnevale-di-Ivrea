package it.polito.applicazionimultimediali.carnevalediivrea.battle;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

public class ExitActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		Log.d("ExitActivity", "onCreate Finish");
		Intent myIntent = new Intent(this, ScoreUpdateActivity.class);
		this.startActivity(myIntent);
		//finish();
	}

}
