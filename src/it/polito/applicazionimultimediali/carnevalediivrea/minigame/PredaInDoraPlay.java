package it.polito.applicazionimultimediali.carnevalediivrea.minigame;

import it.polito.applicazionimultimediali.carnevalediivrea.GlobalRes;

import java.util.ArrayList;
import java.util.Collections;

import it.polito.applicazionimultimediali.carnevalediivrea.R;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

public class PredaInDoraPlay extends Activity {
	private static ArrayList<Integer> orangeToWin;
	private ViewGroup rockCont, pidOrangeCont;
	private ImageButton rock1, rock2, rock3;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.preda_in_dora);
		
		rock1.findViewById(R.id.rock1);
		rock2.findViewById(R.id.rock2);
		rock3.findViewById(R.id.rock3);
		
		
//		android.view.Display display = ((android.view.WindowManager)getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay(); 
//		rock1.setX((int)(display.getRealSize(null))*0.70);

		orangeToWin = new ArrayList<Integer>();
		orangeToWin.add(20);
		orangeToWin.add(40);
		orangeToWin.add(60);
		Collections.shuffle(orangeToWin);
		

//		pidOrangeCont = (ViewGroup) findViewById(R.id.pidOrangeCont);
//		for (int i = 0; i < pidOrangeCont.getChildCount(); i++) {
//			TextView orangeText = (TextView) pidOrangeCont.getChildAt(i);
//			orangeText.setText(orangeToWin.get(i).toString());
//		}
	}
	public void breakRock(View v) {
		int index = rockCont.indexOfChild(v);
		GlobalRes.getCurrentPlayer().gainOranges(orangeToWin.get(index));
		
		pidOrangeCont.getChildAt(index).setAlpha(1);

		for (int i = 0; i < rockCont.getChildCount(); i++) {
			rockCont.getChildAt(i).setClickable(false);
		}

		final Handler mHandler = new Handler();
		Runnable showAllOranges = new Runnable() {
			@Override
			public void run() {
				for (int i = 0; i < pidOrangeCont.getChildCount(); i++) {
					TextView orangeText = (TextView) pidOrangeCont
							.getChildAt(i);
					orangeText.setAlpha(1);
				}
			}
		};
		mHandler.postDelayed(showAllOranges, 2000);
	}

}
