package it.polito.applicazionimultimediali.carnevalediivrea.map;

import it.polito.applicazionimultimediali.carnevalediivrea.GlobalRes;
import it.polito.applicazionimultimediali.carnevalediivrea.R;
import it.polito.applicazionimultimediali.carnevalediivrea.battle.BattleActivity;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class PlaceActivity extends Activity {

	private Place place;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.place_activity);

		String placeId = null;
		if (savedInstanceState == null) {
			Bundle extras = getIntent().getExtras();
			if (extras != null) {
				placeId = extras.getString("place");
			}
		} else {
			placeId = (String) savedInstanceState.getSerializable("place");
		}

		if (placeId != null) {
			place = GlobalRes.placesList.get(Integer.parseInt(placeId));

			setTitle(place.getName());
		}
	}

	public void goToBattle(View view) {
		Intent intent = new Intent(this, BattleActivity.class);
		// intent.putExtra(EXTRA_MESSAGE, message);
		startActivity(intent);
	}

	public void goToMinigame(View view) {
		 Intent intent = new Intent(this, place.getMinigame());
		// // intent.putExtra(EXTRA_MESSAGE, message);
		 startActivity(intent);
	}

}
