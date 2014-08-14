package it.polito.applicazionimultimediali.carnevalediivrea.minigame;

import it.polito.applicazionimultimediali.carnevalediivrea.R;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.widget.MediaController;
import android.widget.VideoView;

public class VideoActivity extends Activity {
	
	//debug
	int idPosto = 0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);
        
		VideoView vd = (VideoView) findViewById(R.id.videoQuiz);
		//inserire switch qui per modificare uri in base a luogo
		Uri uri=null;
		
		
		switch (idPosto){
			case 0:
				uri = Uri.parse("android.resource://it.polito.applicazionimultimediali.carnevalediivrea/"+R.raw.castello);
				break;
			case 1:
				uri = Uri.parse("android.resource://it.polito.applicazionimultimediali.carnevalediivrea/"+R.raw.castello);
				break;
			case 2:
				uri = Uri.parse("android.resource://it.polito.applicazionimultimediali.carnevalediivrea/"+R.raw.castello);
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

