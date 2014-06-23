package it.polito.applicazionimultimediali.carnevalediivrea;

import it.polito.applicazionimultimediali.carnevalediivrea.battle.BattleActivity;
import it.polito.applicazionimultimediali.carnevalediivrea.map.MapPane;
<<<<<<< HEAD

import android.app.Activity;
=======
>>>>>>> origin/master
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.google.android.gms.games.Games;
import com.google.android.gms.games.Player;
import com.google.example.games.basegameutils.BaseGameActivity;

public class MainActivity extends BaseGameActivity implements
		View.OnClickListener {

	private static final String TAG = "Main Activity";
	private View signinBtn, signoutBtn;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		signinBtn = findViewById(R.id.sign_in_button);
		signoutBtn = findViewById(R.id.sign_out_button);
		signoutBtn.setOnClickListener(this);
		signinBtn.setOnClickListener(this);

		if (isSignedIn()) {
			signinBtn.setVisibility(View.GONE);
			signoutBtn.setVisibility(View.VISIBLE);
		}

		GlobalRes.prepareResources(this);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle presses on the action bar items
		int itemID = item.getItemId();
		if (itemID == R.id.player) {
			Intent intent = new Intent(this, PlayerActivity.class);
			// intent.putExtra(EXTRA_MESSAGE, message);
			startActivity(intent);
		} else {
			return super.onOptionsItemSelected(item);
		}
		return false;
	}

	public void goToMap(View view) {
		Intent intent = new Intent(this, MapPane.class);
		// intent.putExtra(EXTRA_MESSAGE, message);
		startActivity(intent);
	}

	// TODO remove when no more needed
	public void goToBattle(View view) {
		Intent intent = new Intent(this, BattleActivity.class);
		// intent.putExtra(EXTRA_MESSAGE, message);
		startActivity(intent);
	}

	public void logout(View view) {
		Intent intent = new Intent(this, BattleActivity.class);
		// intent.putExtra(EXTRA_MESSAGE, message);
		startActivity(intent);
	}

	@Override
	public void onSignInFailed() {
		// show sign-out button, hide the sign-in button
		signoutBtn.setVisibility(View.GONE);
		signinBtn.setVisibility(View.VISIBLE);
	}

	@Override
	public void onSignInSucceeded() {
		// show sign-out button, hide the sign-in button
		signinBtn.setVisibility(View.GONE);
		signoutBtn.setVisibility(View.VISIBLE);

		Player p = Games.Players.getCurrentPlayer(getApiClient());
		if (p == null) {
			Log.w(TAG, "mGamesClient.getCurrentPlayer() is NULL!");
			return;
		}

		String nickname = p.getDisplayName();
		Uri icoImg = null;
		if (p.hasIconImage()) {
			icoImg = p.getIconImageUri();
		}

		GlobalRes.getCurrentPlayer().updateInfo(nickname, icoImg);
	}

	@Override
	public void onClick(View view) {
		if (view.getId() == R.id.sign_in_button) {
			// start the asynchronous sign in flow
			beginUserInitiatedSignIn();
		} else if (view.getId() == R.id.sign_out_button) {
			signOut();

			// show sign-in button, hide the sign-out button
			signinBtn.setVisibility(View.VISIBLE);
			signoutBtn.setVisibility(View.GONE);
		}
	}

}
