package it.polito.applicazionimultimediali.carnevalediivrea;

import it.polito.applicazionimultimediali.carnevalediivrea.battle.BattleActivity;
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
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.games.Games;
import com.google.android.gms.games.Player;
import com.google.android.gms.games.multiplayer.Invitation;
import com.google.android.gms.games.multiplayer.Multiplayer;
import com.google.android.gms.games.multiplayer.turnbased.TurnBasedMatch;
import com.google.example.games.basegameutils.BaseGameActivity;

public class MainActivity extends BaseGameActivity implements
		View.OnClickListener, GoogleApiClient.ConnectionCallbacks {

	private static final String TAG = "Main Activity";
	private View signinBtn, loginPopup;
	private Handler mHandler;
	private boolean activityStarted;

	private Runnable goToMap = new Runnable() {
		@Override
		public void run() {
			mHandler.removeCallbacks(showLoginPopup);
			goToMap(null);
		}
	};
	private Runnable showLoginPopup = new Runnable() {
		@Override
		public void run() {
			mHandler.removeCallbacks(goToMap);
			if (!isSignedIn()) {
				showLoginPopup();
			} else
				goToMap(null);
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		activityStarted = false;
		setContentView(R.layout.activity_main);
		getGameHelper().setMaxAutoSignInAttempts(0);
		getApiClient().registerConnectionCallbacks(this);
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
		if (activityStarted) // this happen on notifications
			return;

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

		GlobalRes.getCurrentPlayer().updateInfo(getApiClient());
		mHandler.postDelayed(goToMap, 3000);
	}

	@Override
	public void onClick(View view) {
		if (view.getId() == R.id.sign_in_button) {
			beginUserInitiatedSignIn();
		}
	}

	@Override
	public void onConnected(Bundle bundle) {
		Log.v(TAG, "connect");

		if (bundle == null)
			return;

		Intent intent = new Intent(this, BattleActivity.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

		TurnBasedMatch tbm = bundle
				.getParcelable(Multiplayer.EXTRA_TURN_BASED_MATCH);
		Invitation inv = bundle.getParcelable(Multiplayer.EXTRA_INVITATION);

		if (tbm != null) {
			intent.putExtra(Multiplayer.EXTRA_TURN_BASED_MATCH, tbm);
			startActivity(intent);
		} else if (inv != null) {
			intent.putExtra(Multiplayer.EXTRA_INVITATION, inv);
		}
		startActivity(intent);
		activityStarted = true;
	}

	@Override
	public void onConnectionSuspended(int arg0) {
		// TODO Auto-generated method stub

	}
}
