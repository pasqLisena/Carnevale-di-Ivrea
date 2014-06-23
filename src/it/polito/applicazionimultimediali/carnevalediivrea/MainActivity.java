package it.polito.applicazionimultimediali.carnevalediivrea;

import it.polito.applicazionimultimediali.carnevalediivrea.battle.BattleActivity;
import it.polito.applicazionimultimediali.carnevalediivrea.map.MapPane;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		GlobalRes.prepareResources(this);
		setContentView(R.layout.activity_main);
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

}
