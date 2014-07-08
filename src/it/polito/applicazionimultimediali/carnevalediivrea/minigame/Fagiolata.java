package it.polito.applicazionimultimediali.carnevalediivrea.minigame;

import it.polito.applicazionimultimediali.carnevalediivrea.R;

import com.google.android.gms.games.Games;
import com.google.android.gms.games.GamesActivityResultCodes;
import com.google.android.gms.games.request.GameRequest;
import com.google.example.games.basegameutils.GameHelper;
import com.google.example.games.basegameutils.GameHelper.GameHelperListener;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

public class Fagiolata extends Minigame {
	private static final int SEND_GIFT_CODE = 2;

	private GameHelper mHelper;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// create game helper with all APIs (Games, Plus, AppState):
		mHelper = new GameHelper(this, GameHelper.CLIENT_GAMES);

		GameHelperListener listener = new GameHelper.GameHelperListener() {
			@Override
			public void onSignInSucceeded() {
				// handle sign-in succeess
			}

			@Override
			public void onSignInFailed() {
				// handle sign-in failure (e.g. show Sign In button)
			}

		};
		Log.d("Fagiolata",(listener!=null)+"");
		mHelper.setup(listener);
	}

	@Override
	protected void onStart() {
		super.onStart();
		mHelper.onStart(this);
	}

	@Override
	protected void onStop() {
		super.onStop();
		mHelper.onStop();
	}

	@Override
	protected void onActivityResult(int request, int response, Intent data) {
		super.onActivityResult(request, response, data);
		mHelper.onActivityResult(request, response, data);
		if (response == GamesActivityResultCodes.RESULT_SEND_REQUEST_FAILED) {
			Toast.makeText(this, "FAILED TO SEND GIFT!", Toast.LENGTH_LONG)
					.show();
		}

	}

	@Override
	public void play(View v) {
		Intent intent = new Intent(this, FagiolataPlay.class);
		startActivity(intent);
		overridePendingTransition(R.anim.fadein_fagiolata, R.anim.zoom_in_fagiolata);
	}

}
