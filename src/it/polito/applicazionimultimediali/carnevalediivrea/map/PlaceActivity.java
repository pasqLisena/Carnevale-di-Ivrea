package it.polito.applicazionimultimediali.carnevalediivrea.map;

import it.polito.applicazionimultimediali.carnevalediivrea.GlobalRes;
import it.polito.applicazionimultimediali.carnevalediivrea.R;
import it.polito.applicazionimultimediali.carnevalediivrea.Team;
import it.polito.applicazionimultimediali.carnevalediivrea.battle.BattleActivity;
import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

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
			String bg_place;
			if (place.getBg() == null || place.getBg() == "")
				bg_place = "place_piazzadicitta_bg";
			else
				bg_place = place.getBg();

			int bg = getResources().getIdentifier(bg_place, "drawable",
					getPackageName());
			if (bg != 0)
				((ImageView) findViewById(R.id.bgPlace)).setImageResource(bg);
			
			
			int count = 0;
			if (place.getTeamsList() != null)
				for (Team t : place.getTeamsList()) {
					++count;

					int idRes = getResources().getIdentifier("LLteam" + count,
							"id", getPackageName());
					findViewById(idRes).setVisibility(View.VISIBLE);

					String idTeam = t.getId();
					int team_mask = getResources().getIdentifier(
							idTeam.toLowerCase(), "drawable", getPackageName());
					int idTeamButton = getResources().getIdentifier("ButtonTeam" + count,
							"id", getPackageName());
					if (team_mask != 0)
						findViewById(idTeamButton).setBackgroundResource(team_mask);
					
				}

			if (place.hasMinigame()) {
				findViewById(R.id.LLminigame).setVisibility(View.VISIBLE);
				int mg_mask = getResources().getIdentifier(
						place.getMinigameMask(), "drawable", getPackageName());
				if (mg_mask != 0)
					findViewById(R.id.ButtonMiniGame).setBackgroundResource(
							mg_mask);
			}
		}
	}

	public void goToBattle(View view) {
		Intent intent = new Intent(this, BattleActivity.class);
		// intent.putExtra(EXTRA_MESSAGE, message);
		startActivity(intent);
	}

	public void goToMinigame(View view) {
		Class<?> mg = place.getMinigame();
		if (mg == null)
			return;
		Intent intent = new Intent(this, mg);
		intent.putExtra("mg_bg", place.getMinigameBg());
		intent.putExtra("mg_descr", place.getMinigameDescr());
		intent.putExtra("mg_mask", place.getMinigameMask());
		startActivity(intent);
	}

}
