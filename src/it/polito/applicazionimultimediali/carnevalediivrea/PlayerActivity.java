package it.polito.applicazionimultimediali.carnevalediivrea;

import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.images.ImageManager;
import com.google.android.gms.common.images.ImageManager.OnImageLoadedListener;
import com.google.android.gms.games.Games;
import com.google.android.gms.games.Player;
import com.google.example.games.basegameutils.BaseGameActivity;

public class PlayerActivity extends BaseGameActivity implements
		OnImageLoadedListener, OnClickListener {

	private CurrentPlayer player;
	private View signoutBtn;
	private View signinBtn;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getGameHelper().setMaxAutoSignInAttempts(0);

		setContentView(R.layout.player_activity);
		signinBtn = findViewById(R.id.sign_in_button);
		signoutBtn = findViewById(R.id.sign_out_button);
		signoutBtn.setOnClickListener(this);
		signinBtn.setOnClickListener(this);
		((SignInButton) signinBtn).setSize(SignInButton.SIZE_WIDE);

		player = GlobalRes.getCurrentPlayer();
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

		if (isSignedIn()) 
			onSignInSucceeded();
	}

	@Override
	public void onImageLoaded(Uri uri, Drawable d, boolean isRequestedDrawable) {
		if (uri.equals(player.getIcoImgUri())) {
			ImageView profileImg = (ImageView) findViewById(R.id.profileImage);
			profileImg.setImageDrawable(d);
		}
	}

	
	@Override
	public void onSignInFailed() {
		signoutBtn.setVisibility(View.GONE);
		signinBtn.setVisibility(View.VISIBLE);
	}

	@Override
	public void onSignInSucceeded() {
		GlobalRes.getCurrentPlayer().updateInfo(getApiClient());

		signinBtn.setVisibility(View.GONE);
		signoutBtn.setVisibility(View.VISIBLE);

		Player p = Games.Players.getCurrentPlayer(getApiClient());
		if (p == null) {
			Log.w("PlayerActivity", "mGamesClient.getCurrentPlayer() is NULL!");
			return;
		}

		String nickname = p.getDisplayName();
		Uri icoImg = null;
		if (p.hasIconImage()) {
			icoImg = p.getIconImageUri();
		}

		GlobalRes.getCurrentPlayer().updateInfo(nickname, icoImg);

		if (player.getIcoImgUri() != null
				&& player.getIcoImgUri().toString().length() > 0) {
			ImageManager.create(getApplicationContext()).loadImage(this,
					player.getIcoImgUri());
		}

		// TODO fare un submit del punteggio?

	}

	@Override
	public void onClick(View view) {
		if (view.getId() == R.id.sign_in_button) {
			// start the asynchronous sign in flow
			beginUserInitiatedSignIn();
		} else if (view.getId() == R.id.sign_out_button) {
			signOut();

			signinBtn.setVisibility(View.VISIBLE);
			signoutBtn.setVisibility(View.GONE);
		}
	}

}
