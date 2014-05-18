package it.polito.applicazionimultimediali.carnevalediivrea;

public class Player {
	String nickname;
	Team team;
	int points;

	//FIXME maybe this is unuseful
	public Player(String nickname, Team team) {
		super();
		this.nickname = nickname;
		this.team = team;
		this.points = 2589; // TODO change
	}

	public Player(String nickname) {
		super();
		this.nickname = nickname;
		this.team = null;
		this.points = 2589; // TODO change
	}
}
