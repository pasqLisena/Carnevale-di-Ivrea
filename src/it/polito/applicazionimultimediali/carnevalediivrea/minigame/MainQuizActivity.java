package it.polito.applicazionimultimediali.carnevalediivrea.minigame;

import it.polito.applicazionimultimediali.carnevalediivrea.GlobalRes;
import it.polito.applicazionimultimediali.carnevalediivrea.R;
import it.polito.applicazionimultimediali.carnevalediivrea.map.Place;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainQuizActivity extends Activity {
		
	private Place place;
	private Button videoPlay;
	private Button gamePlay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_quiz); 
        
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
		}
		
        videoPlay = (Button) findViewById(R.id.videoPlay);
        
        videoPlay.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent i1 = new Intent(getBaseContext(), VideoActivity.class);
				i1.putExtra("place", place.getId() + "");
        		startActivity(i1);
				
			}
		});
        
        gamePlay = (Button) findViewById(R.id.gamePlay);
        gamePlay.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent i2 = new Intent(getBaseContext(), QuizActivity.class);
				i2.putExtra("place", place.getId() + "");
				startActivity(i2);
				
			}
		});
    }
    

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main_quiz, menu);
        return true;
    }

	
}
