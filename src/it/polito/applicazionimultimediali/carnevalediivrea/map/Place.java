package it.polito.applicazionimultimediali.carnevalediivrea.map;

import it.polito.applicazionimultimediali.carnevalediivrea.Team;

import java.util.ArrayList;
import java.util.List;

import com.google.android.gms.maps.model.LatLng;

public class Place {
	private int id;
	private String name;
	private LatLng latLng;

	private List<Team> teamsList;

	private boolean locked;

	public Place(int id, String name, String latLng) {
		super();
		this.id = id;
		this.name = name;

		String[] temp = latLng.split(",");
		this.latLng = new LatLng(Double.parseDouble(temp[0]),
				Double.parseDouble(temp[1]));

		this.locked = true;
	}

	public int getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public LatLng getLatLng() {
		return latLng;
	}

	public boolean isLocked() {
		return locked;
	}
	
	public void addTeam(Team team) {
		if (teamsList == null) {
			teamsList = new ArrayList<Team>();
		}

		teamsList.add(team);
	}

	public boolean isBattlePlace(){
		return (teamsList!=null && teamsList.size() > 0);
	}
	
	public String getTeamsString() {
		String s = "";
		if (teamsList != null) {
			for(Team t: teamsList){
				if(s.length()>0){
					s+=" - ";
				}
				s += t.getShortName();
			}
		}

		return s;
	}
}
