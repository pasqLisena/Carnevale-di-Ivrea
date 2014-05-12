package it.polito.applicazionimultimediali.carnevalediivrea;

public class Player {
	String nickname;
	Team team;
	int oranges, points;

	public Player(String nickname, Team team) {
		super();
		this.nickname = nickname;
		this.team = team;
		this.oranges = 30; // TODO change
		this.points = 2589; // TODO change
	}

	public Player(String nickname) {
		super();
		this.nickname = nickname;
		this.team = null;
		this.oranges = 30; // TODO change
		this.points = 2589; // TODO change
	}

	public void gainPoint(int points) {
		this.points += points;
	}

	public void lostPoint(int points) {
		this.points -= points;
	}

	public void gainOranges(int oranges){
		this.oranges = oranges;
	}

	public boolean playAGame() {
		if(oranges >= GlobalRes.orangesPerPlay){
			oranges-= GlobalRes.orangesPerPlay;
			gainPoint(100);
			return true;
		}
		return false;
	}
}
