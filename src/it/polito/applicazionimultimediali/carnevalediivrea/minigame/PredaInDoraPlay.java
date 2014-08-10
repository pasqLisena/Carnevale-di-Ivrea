package it.polito.applicazionimultimediali.carnevalediivrea.minigame;

import it.polito.applicazionimultimediali.carnevalediivrea.GlobalRes;
import it.polito.applicazionimultimediali.carnevalediivrea.R;

import java.util.ArrayList;
import java.util.Collections;

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.TextView;

public class PredaInDoraPlay extends Activity {
	private static ArrayList<Integer> orangeToWin;
	private static ArrayList<View> rocks;
	private static ArrayList<TextView> orangeText;
	private ImageButton rock1, rock2, rock3;
	private TextView orangeTex1, orangeTex2, orangeTex3;
	private View rock1After, rock2After, rock3After;
	private Integer gainedOranges;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.preda_in_dora);

		rock1 = (ImageButton) findViewById(R.id.rock1);
		rock2 = (ImageButton) findViewById(R.id.rock2);
		rock3 = (ImageButton) findViewById(R.id.rock3);

		rock1After = findViewById(R.id.rock1_after);
		rock2After = findViewById(R.id.rock2_after);
		rock3After = findViewById(R.id.rock3_after);

		orangeTex1 = (TextView) findViewById(R.id.orangeText1);
		orangeTex2 = (TextView) findViewById(R.id.orangeText2);
		orangeTex3 = (TextView) findViewById(R.id.orangeText3);

		rocks = new ArrayList<View>();
		rocks.add(rock1);
		rocks.add(rock2);
		rocks.add(rock3);

		orangeText = new ArrayList<TextView>();
		orangeText.add(orangeTex1);
		orangeText.add(orangeTex2);
		orangeText.add(orangeTex3);

		orangeToWin = new ArrayList<Integer>();
		orangeToWin.add(20);
		orangeToWin.add(40);
		orangeToWin.add(60);
		Collections.shuffle(orangeToWin);

		for (int i = 0; i < orangeText.size(); i++) {
			TextView tv = orangeText.get(i);
			tv.setText(orangeToWin.get(i).toString());
		}
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

		Animation myscrolldownAnimation = AnimationUtils.loadAnimation(this,
				R.anim.scroll_down);
		Animation myfadeoutAnimation = AnimationUtils.loadAnimation(this,
				R.anim.fadeout);

		gainedOranges = orangeToWin.get(index);

		GlobalRes.getCurrentPlayer().gainOranges(gainedOranges);

		for (int i = 0; i < rocks.size(); i++) {
			if (rocks.get(i) == v) {
				rocks.get(i).startAnimation(myscrolldownAnimation);
			} else {
				rocks.get(i).startAnimation(myfadeoutAnimation);

			}
			rocks.get(i).setClickable(false);
			rocks.get(i).setVisibility(View.GONE);
		}

		final Handler mHandler = new Handler();
		Runnable showAllOranges = new Runnable() {
			@Override
			public void run() {
				AlertDialog.Builder builder = new AlertDialog.Builder(
						PredaInDoraPlay.this);
				builder.setMessage("Hai vinto " + gainedOranges + " arance")
						.setPositiveButton("OK",
								new DialogInterface.OnClickListener() {
									@Override
									public void onClick(DialogInterface dialog,
											int id) {
										finish();
									}
								});

				// Create the AlertDialog object and return it
				builder.create().show();
			}
		};
		mHandler.postDelayed(showAllOranges, 2000);
	}

}
