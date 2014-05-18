package it.polito.applicazionimultimediali.carnevalediivrea;

import android.content.SharedPreferences;

public class CurrentPlayer extends Player {
	private int oranges;
	private SharedPreferences playerData;

	public CurrentPlayer(SharedPreferences playerData) {
		super(playerData.getString("nickname", "Anonimo"));
		this.playerData = playerData;
		this.points = playerData.getInt("points", 0);
		this.oranges = playerData.getInt("oranges", 30);
//		this.team = playerData.getString("Team", null);
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

	public boolean playAGame() {
		if (getOranges() >= GlobalRes.orangesPerPlay) {
			this.oranges = getOranges() - GlobalRes.orangesPerPlay;
			updateLocalData("oranges", this.oranges);
			gainPoint(100);
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


}
