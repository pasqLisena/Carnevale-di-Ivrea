package it.polito.applicazionimultimediali.carnevalediivrea;

import it.polito.applicazionimultimediali.carnevalediivrea.map.MapPane;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.google.android.gms.common.SignInButton;
import com.google.android.gms.games.Games;
import com.google.android.gms.games.Player;
import com.google.example.games.basegameutils.BaseGameActivity;

public class MainActivity extends BaseGameActivity implements
		View.OnClickListener {

	private static final String TAG = "Main Activity";
	private View signinBtn, loginPopup;
	private Handler mHandler;
	private Runnable goToMap = new Runnable() {
		@Override
		public void run() {
			goToMap(null);
		}
	};
	private Runnable showLoginPopup = new Runnable() {
		@Override
		public void run() {
			if (!isSignedIn()) {
				showLoginPopup();
			} else
				goToMap(null);
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		getGameHelper().setMaxAutoSignInAttempts(0);

		mHandler = new Handler();

		loginPopup = findViewById(R.id.login_popup);
		signinBtn = findViewById(R.id.sign_in_button);
		signinBtn.setOnClickListener(this);
		((SignInButton) signinBtn).setSize(SignInButton.SIZE_WIDE);

		if (!isSignedIn()) {
			signinBtn.setOnClickListener(this);
			((SignInButton) signinBtn).setSize(SignInButton.SIZE_WIDE);

			mHandler.postDelayed(showLoginPopup, 3000);
		}

		GlobalRes.prepareResources(this);
	}

	@Override
	public void onStop() {
		super.onStop();
		mHandler.removeCallbacks(showLoginPopup);
	};

	public void goToMap(View view) {
		Intent intent = new Intent(this, MapPane.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(intent);
	}

	public void showLoginPopup() {
		 loginPopup.setVisibility(View.VISIBLE);
		Animation myFadeInAnimation = AnimationUtils.loadAnimation(this,
				R.anim.fadein);
		loginPopup.startAnimation(myFadeInAnimation);
	}

	@Override
	public void onSignInFailed() {
		// do nothing
	}

	@Override
	public void onSignInSucceeded() {
		loginPopup.setVisibility(View.GONE);

		Player p = Games.Players.getCurrentPlayer(getApiClient());
		if (p == null) {
			Log.w(TAG, "mGamesClient.getCurrentPlayer() is NULL!");
		} else {
			String nickname = p.getDisplayName();
			Uri icoImg = null;
			if (p.hasIconImage())
				icoImg = p.getIconImageUri();

			GlobalRes.getCurrentPlayer().updateInfo(nickname, icoImg);
		}

		mHandler.postDelayed(goToMap, 3000);
	}

	@Override
	public void onClick(View view) {
		if (view.getId() == R.id.sign_in_button) {
			beginUserInitiatedSignIn();
		}
	}
}
