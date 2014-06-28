package it.polito.applicazionimultimediali.carnevalediivrea;

import it.polito.applicazionimultimediali.carnevalediivrea.R;
import android.app.Activity;
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

public class PlayerActivity extends BaseGameActivity implements OnImageLoadedListener, OnClickListener {

	private CurrentPlayer player;
	private View signoutBtn;
	private View signinBtn;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

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

		if (player.getIcoImgUri() != null
				&& player.getIcoImgUri().toString().length() > 0) {
			ImageManager.create(getApplicationContext()).loadImage(this, player.getIcoImgUri());
		}
	}

	@Override
	public void onImageLoaded(Uri uri, Drawable d, boolean isRequestedDrawable) {
		if (uri.equals(player.getIcoImgUri())) {
			ImageView profileImg = (ImageView) findViewById(R.id.profileImage);
			profileImg.setImageDrawable(d);
		}
	}
	
	public void showLeaderboard(View v){
		startActivityForResult(Games.Leaderboards.getLeaderboardIntent(getApiClient(), GlobalRes.getArancieriLeaderboard()), 1);
	}


	@Override
	public void onSignInFailed() {
		// show sign-out button, hide the sign-in button
		signoutBtn.setVisibility(View.GONE);
		signinBtn.setVisibility(View.VISIBLE);
	}

	@Override
	public void onSignInSucceeded() {
		// show sign-out button, hide the sign-in button
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
	}

	
	@Override
	public void onClick(View view) {
		if (view.getId() == R.id.sign_in_button) {
			// start the asynchronous sign in flow
			beginUserInitiatedSignIn();
		} else if (view.getId() == R.id.sign_out_button) {
			signOut();

			// show sign-in button, hide the sign-out button
			signinBtn.setVisibility(View.VISIBLE);
			signoutBtn.setVisibility(View.GONE);
		}
	}

}
