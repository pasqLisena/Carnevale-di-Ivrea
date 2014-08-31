package it.polito.applicazionimultimediali.carnevalediivrea;

import android.content.SharedPreferences;
import android.net.Uri;
import android.util.Log;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.games.Games;

public class CurrentPlayer extends Player {
	private int oranges;
	private SharedPreferences playerData;
	private boolean isSignedIn;
	private String TAG = "CurrentPlayer";

	public CurrentPlayer(SharedPreferences playerData) {
		super(playerData.getString("nickname", "Anonimo"), Uri.parse(playerData
				.getString("icoImg", "")));
		this.playerData = playerData;
		this.points = playerData.getInt("points", 0);
		this.oranges = playerData.getInt("oranges", 30);
		setSignedIn(false);
		// this.team = playerData.getString("Team", null);
	}

	public int getOranges() {
		return oranges;
	}

	public int getPoints() {
		return points;
	}

	public void gainOranges(int oranges) {
		this.oranges += oranges;
		updateLocalData("oranges", this.oranges);
	}

	private void updateLocalData(String key, int value) {
		SharedPreferences.Editor prefEditor = playerData.edit();
		prefEditor.putInt(key, value);
		prefEditor.commit();
	}

	public void gainPoint(int points, GoogleApiClient googleApiClient) {
		this.points += points;
		updateLocalData("points", this.points);
		if (googleApiClient != null) {
			Games.Leaderboards.submitScore(googleApiClient,
					GlobalRes.getArancieriLeaderboard(), this.points);
		}
	}

	public void lostPoint(int points, GoogleApiClient googleApiClient) {
		int newPoints = 0 - points;
		gainPoint(newPoints, googleApiClient);
	}

	public void updateInfo(String nickname, Uri icoImg) {
		this.nickname = nickname;
		this.icoImgUri = icoImg;

		SharedPreferences.Editor prefEditor = playerData.edit();
		prefEditor.putString("nickname", nickname);
		if (icoImg != null) {
			prefEditor.putString("icoImg", icoImg.toString());
		}
		prefEditor.commit();
	}

	public void updateInfo(GoogleApiClient googleApiClient) {
		com.google.android.gms.games.Player p = Games.Players.getCurrentPlayer(googleApiClient);
		if (p == null) {
			Log.w(TAG, "mGamesClient.getCurrentPlayer() is NULL!");
		} else {
			setSignedIn(true);
			String nickname = p.getDisplayName();
			Uri icoImg = null;
			if (p.hasIconImage())
				icoImg = p.getIconImageUri();

			this.updateInfo(nickname, icoImg);
		}
	}

	public boolean isSignedIn() {
		return isSignedIn;
	}

	public void setSignedIn(boolean isSignedIn) {
		this.isSignedIn = isSignedIn;
	}
}
