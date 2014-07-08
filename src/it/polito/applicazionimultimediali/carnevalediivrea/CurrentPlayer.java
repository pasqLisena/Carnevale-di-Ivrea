package it.polito.applicazionimultimediali.carnevalediivrea;

import android.content.SharedPreferences;
import android.net.Uri;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.games.Games;

public class CurrentPlayer extends Player {
	private int oranges;
	private SharedPreferences playerData;

	public CurrentPlayer(SharedPreferences playerData) {
		super(playerData.getString("nickname", "Anonimo"), Uri.parse(playerData
				.getString("icoImg", "")));
		this.playerData = playerData;
		this.points = playerData.getInt("points", 0);
		this.oranges = playerData.getInt("oranges", 30);
		// this.team = playerData.getString("Team", null);
	}

	public int getOranges() {
		return oranges;
	}

	public void gainOranges(int oranges) {
		this.oranges = oranges;
		updateLocalData("oranges", this.oranges);
	}

	private void updateLocalData(String key, int value) {
		SharedPreferences.Editor prefEditor = playerData.edit();
		prefEditor.putInt(key, value);
		prefEditor.commit();
	}
	
	
	public boolean playAGame(GoogleApiClient googleApiClient) {
		if (getOranges() >= GlobalRes.orangesPerPlay) {
			this.oranges = getOranges() - GlobalRes.orangesPerPlay;
			updateLocalData("oranges", this.oranges);
			gainPoint(100);

			if(googleApiClient != null){
				Games.Leaderboards.submitScore(googleApiClient, GlobalRes.getArancieriLeaderboard(), this.points);
			}
			return true;
		}
		return false;
	}

	public void gainPoint(int points) {
		this.points += points;
		updateLocalData("points", this.points);
	}

	public void lostPoint(int points) {
		int newPoints = 0 - points;
		gainPoint(newPoints);
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
}
