package it.polito.applicazionimultimediali.carnevalediivrea;

import com.google.android.gms.*;
import com.google.example.games.basegameutils.BaseGameActivity;

import it.polito.applicazionimultimediali.carnevalediivrea.battle.BattleActivity;
import it.polito.applicazionimultimediali.carnevalediivrea.map.MapPane;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

public class MainActivity extends BaseGameActivity implements
		View.OnClickListener {

	private View signinBtn, signoutBtn;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		GlobalRes.prepareResources(this);
		setContentView(R.layout.activity_main);

		signinBtn = findViewById(R.id.sign_in_button);
		signoutBtn = findViewById(R.id.sign_out_button);
		signoutBtn.setOnClickListener(this);
		signinBtn.setOnClickListener(this);

		if (isSignedIn()) {
			signinBtn.setVisibility(View.GONE);
			signoutBtn.setVisibility(View.VISIBLE);
		}
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
		findViewById(R.id.sign_in_button).setVisibility(View.GONE);
		findViewById(R.id.sign_out_button).setVisibility(View.VISIBLE);

		// (your code here: update UI, enable functionality that depends on sign
		// in, etc)
	}

	@Override
	public void onSignInSucceeded() {
		// show sign-out button, hide the sign-in button
		findViewById(R.id.sign_in_button).setVisibility(View.GONE);
		findViewById(R.id.sign_out_button).setVisibility(View.VISIBLE);

		// (your code here: update UI, enable functionality that depends on sign
		// in, etc)
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
