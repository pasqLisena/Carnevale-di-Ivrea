package it.polito.applicazionimultimediali.carnevalediivrea.minigame;

import com.google.android.gms.games.Games;
import com.google.example.games.basegameutils.GameHelper;
import com.google.example.games.basegameutils.GameHelper.GameHelperListener;

import it.polito.applicazionimultimediali.carnevalediivrea.GlobalRes;
import it.polito.applicazionimultimediali.carnevalediivrea.R;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

public class LibroVerbali extends Minigame implements OnClickListener{
	private static final String TAG = "LibroVerbali";

	private GameHelper mHelper;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mHelper = new GameHelper(this, GameHelper.CLIENT_GAMES);
		mHelper.setMaxAutoSignInAttempts(0);

		GameHelperListener listener = new GameHelper.GameHelperListener() {
			@Override
			public void onSignInSucceeded() {
				GlobalRes.getCurrentPlayer().updateInfo(mHelper.getApiClient());

				findViewById(R.id.sign_in_button).setVisibility(View.GONE);
				findViewById(R.id.btnStartMinigame).setVisibility(View.VISIBLE);
			}

			@Override
			public void onSignInFailed() {
				findViewById(R.id.sign_in_button).setVisibility(View.VISIBLE);
				findViewById(R.id.btnStartMinigame).setVisibility(View.GONE);
			}

		};
		mHelper.setup(listener);
		
		if(!mHelper.isSignedIn()){
			findViewById(R.id.sign_in_button).setVisibility(View.VISIBLE);
			findViewById(R.id.sign_in_button).setOnClickListener(this);
			findViewById(R.id.btnStartMinigame).setVisibility(View.GONE);
		}
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
		startActivityForResult(Games.Leaderboards.getLeaderboardIntent(
				mHelper.getApiClient(), GlobalRes.getArancieriLeaderboard()), 1);
	}

	@Override
	public void onClick(View v) {
		if(v.getId()==R.id.sign_in_button)
			mHelper.beginUserInitiatedSignIn();
		
	}
	
}
