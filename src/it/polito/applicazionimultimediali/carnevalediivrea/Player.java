package it.polito.applicazionimultimediali.carnevalediivrea;

import android.net.Uri;

public class Player {
	protected String nickname;
	Uri icoImgUri;
	protected Team team;
	protected int points;

	// FIXME maybe this is unuseful
	public Player(String nickname, Uri icoImg, Team team) {
		super();
		this.nickname = nickname;
		this.team = team;
		this.points = 2589; // TODO change
		this.icoImgUri = icoImg;
	}

	public Player(String nickname, Uri icoImg) {
		super();
		this.nickname = nickname;
		this.team = null;
		this.points = 2589; // TODO change
		this.icoImgUri = icoImg;
	}
	
	public Uri getIcoImgUri() {
		return icoImgUri;
	}
}
