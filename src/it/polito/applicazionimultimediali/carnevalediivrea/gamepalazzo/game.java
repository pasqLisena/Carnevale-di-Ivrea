package it.polito.applicazionimultimediali.carnevalediivrea.gamepalazzo;

import android.os.Bundle;
import android.util.Log;

import com.unity3d.player.UnityPlayerActivity;

public class game extends UnityPlayerActivity {
	protected void onCreate(Bundle savedInstanceState) {


		// call UnityPlayerActivity.onCreate()
		super.onCreate(savedInstanceState);

		// print debug message to logcat
		Log.d("OverrideActivity", "onCreate called!");
		
	}

	public void onBackPressed()
	{
		// instead of calling UnityPlayerActivity.onBackPressed() we just ignore the back button event
		// super.onBackPressed();
	}

}
