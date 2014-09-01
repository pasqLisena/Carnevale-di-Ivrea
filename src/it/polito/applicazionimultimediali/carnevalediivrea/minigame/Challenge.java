package it.polito.applicazionimultimediali.carnevalediivrea.minigame;

import it.polito.applicazionimultimediali.carnevalediivrea.battle.BattleActivity;
import android.content.Intent;
import android.view.View;

public class Challenge extends Minigame {
	private static final String TAG = "Challenge";

	@Override
	public void play(View v) {
		Intent intent = new Intent(this, BattleActivity.class);
		intent.putExtra("Challenge", true);
		startActivity(intent);
	}
}
