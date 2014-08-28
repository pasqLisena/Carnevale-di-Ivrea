package it.polito.applicazionimultimediali.carnevalediivrea.minigame;

import it.polito.applicazionimultimediali.carnevalediivrea.GlobalRes;
import it.polito.applicazionimultimediali.carnevalediivrea.R;
import it.polito.applicazionimultimediali.carnevalediivrea.map.Place;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.widget.MediaController;
import android.widget.VideoView;

public class VideoActivity extends Activity {
	
	//debug
	int idPosto;
	private Place place;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);
        
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
		
		idPosto=place.getId();
			
               
        
		VideoView vd = (VideoView) findViewById(R.id.videoQuiz);
		//inserire switch qui per modificare uri in base a luogo
		Uri uri=null;
		
		
		
		switch (idPosto){
			case 9:
				uri = Uri.parse("android.resource://it.polito.applicazionimultimediali.carnevalediivrea/"+R.raw.castello);
				break;
			case 8:
				uri = Uri.parse("android.resource://it.polito.applicazionimultimediali.carnevalediivrea/"+R.raw.duomo);
				break;
			
		}
		
		MediaController mc = new MediaController(this);
		vd.setMediaController(mc);
		vd.setVideoURI(uri);
		vd.start();
		
		vd.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
			
			@Override
			public void onCompletion(MediaPlayer mp) {
				// TODO Auto-generated method stub
				Intent homeQuiz = new Intent(getBaseContext(), MainQuizActivity.class);
				startActivity(homeQuiz);			
			}
		});
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_video, menu);
        return true;
    }
}

