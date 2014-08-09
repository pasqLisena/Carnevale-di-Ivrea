package it.polito.applicazionimultimediali.carnevalediivrea.battle;

import it.polito.applicazionimultimediali.carnevalediivrea.CurrentPlayer;
import it.polito.applicazionimultimediali.carnevalediivrea.GlobalRes;
import it.polito.applicazionimultimediali.carnevalediivrea.R;
import it.polito.applicazionimultimediali.carnevalediivrea.map.MapPane;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.example.games.basegameutils.BaseGameActivity;

public class BattleActivity extends BaseGameActivity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.battle_activity);
		
	}

	
	public void fakePlay(View v) {
		CurrentPlayer player = GlobalRes.getCurrentPlayer();
		String text;
		
		GoogleApiClient googleApiClient = null;
		if(isSignedIn()){
			Log.w("QQQQ", "Signed IN ");

			googleApiClient = getApiClient();
		}

		if (player.getOranges() >= GlobalRes.orangesPerPlay) {
			Intent intent = new Intent(this, ScoreUpdateActivity.class);
			intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent);
		} else {
			text = "Non hai abbastanza arance";
			Toast toast = Toast.makeText(this, text, Toast.LENGTH_SHORT);
			toast.show();
		}
	}


	@Override
	public void onSignInFailed() {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void onSignInSucceeded() {
		// TODO Auto-generated method stub
		
	}


}
