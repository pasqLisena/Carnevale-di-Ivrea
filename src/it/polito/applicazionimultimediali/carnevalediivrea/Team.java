package it.polito.applicazionimultimediali.carnevalediivrea;

import it.polito.applicazionimultimediali.carnevalediivrea.map.Place;

public class Team {
	private String id, name, shortname;
	private Place square;
	private int points;

	public Team(String id, String name, String shortname, Place square) {
		super();
		this.id = id;
		this.name = name;

		if (shortname == null)
			this.shortname = name;
		else
			this.shortname = shortname;

		if (square == null) {
			throw new RuntimeException("Undefined square for team " + this.name);
		}

		this.square = square;
		square.addTeam(this);
	}

	public String getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public String getShortName() {
		return shortname;
	}

	

}
