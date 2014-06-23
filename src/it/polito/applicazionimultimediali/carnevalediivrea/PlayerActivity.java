package it.polito.applicazionimultimediali.carnevalediivrea;

import it.polito.applicazionimultimediali.carnevalediivrea.R;
import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.common.images.ImageManager;
import com.google.android.gms.common.images.ImageManager.OnImageLoadedListener;

public class PlayerActivity extends Activity implements OnImageLoadedListener {

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
		pointsCounter.setText(getResources().getQuantityString(R.plurals.point,
				pointsNum, pointsNum));

		if (player.getIcoImgUri() != null
				&& player.getIcoImgUri().toString().length() > 0) {
			ImageManager.create(getApplicationContext()).loadImage(this, player.getIcoImgUri());
		}
	}

	@Override
	public void onImageLoaded(Uri uri, Drawable d, boolean isRequestedDrawable) {
		Log.w("QQQQ", "Uri " + uri.toString());
		if (uri.equals(player.getIcoImgUri())) {
			ImageView profileImg = (ImageView) findViewById(R.id.profileImage);
			profileImg.setImageDrawable(d);
		}
	}

}
