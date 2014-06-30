package it.polito.applicazionimultimediali.carnevalediivrea.minigame;

import it.polito.applicazionimultimediali.carnevalediivrea.GlobalRes;
import it.polito.applicazionimultimediali.carnevalediivrea.R;

import java.util.ArrayList;
import java.util.Collections;

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

public class PredaInDoraPlay extends Activity {
	private static ArrayList<Integer> orangeToWin;
	private static ArrayList<View> rocks;
	private View rock1, rock2, rock3;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.preda_in_dora);

		rock1 = findViewById(R.id.rock1);
		rock2 = findViewById(R.id.rock2);
		rock3 = findViewById(R.id.rock3);

		rocks = new ArrayList<View>();
		rocks.add(rock1);
		rocks.add(rock2);
		rocks.add(rock3);

		orangeToWin = new ArrayList<Integer>();
		orangeToWin.add(20);
		orangeToWin.add(40);
		orangeToWin.add(60);
		Collections.shuffle(orangeToWin);

		// pidOrangeCont = (ViewGroup) findViewById(R.id.pidOrangeCont);
		// for (int i = 0; i < pidOrangeCont.getChildCount(); i++) {
		// TextView orangeText = (TextView) pidOrangeCont.getChildAt(i);
		// orangeText.setText(orangeToWin.get(i).toString());
		// }
	}

	public void breakRock(View v) {

		int index = 0;
		if (v == rock1)
			index = 1;
		else if (v == rock2)
			index = 2;
		else if (v == rock3)
			index = 3;

		if (index == 0) {
			Log.e("PredaInDoraPlay", "Click on a unrecognized obj");
			// TODO
			return;
		}

		GlobalRes.getCurrentPlayer().gainOranges(orangeToWin.get(index));

		for (int i = 0; i < rocks.size(); i++) {
			rocks.get(i).setClickable(false);
		}


		// final Handler mHandler = new Handler();
		// Runnable showAllOranges = new Runnable() {
		// @Override
		// public void run() {
		// for (int i = 0; i < pidOrangeCont.getChildCount(); i++) {
		// TextView orangeText = (TextView) pidOrangeCont
		// .getChildAt(i);
		// orangeText.setAlpha(1);
		// }
		// }
		// };
		// mHandler.postDelayed(showAllOranges, 2000);
	}

}
