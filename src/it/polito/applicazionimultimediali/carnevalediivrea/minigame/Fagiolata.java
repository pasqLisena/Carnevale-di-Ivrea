package it.polito.applicazionimultimediali.carnevalediivrea.minigame;

import com.google.example.games.basegameutils.GameHelper;
import com.google.example.games.basegameutils.GameHelper.GameHelperListener;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class Fagiolata extends Minigame {

	private GameHelper mHelper;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// create game helper with all APIs (Games, Plus, AppState):
		mHelper = new GameHelper(this, GameHelper.CLIENT_ALL);

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
	}

	@Override
	public void play(View v) {
		// TODO Auto-generated method stub

	}

}
