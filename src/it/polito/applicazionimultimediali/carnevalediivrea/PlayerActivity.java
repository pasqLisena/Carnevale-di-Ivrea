package it.polito.applicazionimultimediali.carnevalediivrea;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

public class PlayerActivity extends Activity {

	private CurrentPlayer player;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		player = GlobalRes.getCurrentPlayer();
		setContentView(R.layout.player_activity);

		String playerName = player.nickname;
		TextView playerNameTV = (TextView) findViewById(R.id.playerName);
		playerNameTV.setText(playerName);

		int orangesNum = player.getOranges();
		TextView orangesCounter = (TextView) findViewById(R.id.oranges_counter);
		orangesCounter.setText(getResources().getQuantityString(
				R.plurals.orange, orangesNum, orangesNum));
		
		int pointsNum = player.points;
		TextView pointsCounter = (TextView) findViewById(R.id.points_counter);
		pointsCounter.setText(getResources().getQuantityString(
				R.plurals.point, pointsNum, pointsNum));

		
	}

}
