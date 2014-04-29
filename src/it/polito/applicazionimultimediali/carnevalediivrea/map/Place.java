package it.polito.applicazionimultimediali.carnevalediivrea.map;

import it.polito.applicazionimultimediali.carnevalediivrea.Team;

import java.util.ArrayList;
import java.util.List;

import com.google.android.gms.maps.model.LatLng;

public class Place {
	private String id;
	private String name;
	private LatLng latLng;

	private List<Team> teamsList;

	private boolean locked;

	public Place(String id, String name, String latLng) {
		super();
		this.id = id;
		this.name = name;

		String[] temp = latLng.split(",");
		this.latLng = new LatLng(Double.parseDouble(temp[0]),
				Double.parseDouble(temp[1]));

		this.locked = true;
	}

	public String getId() {
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

	public void addTeam(String id) {
		if (teamsList == null) {
			teamsList = new ArrayList<Team>();
		}

		// TODO qui ci sarà una ricerca
		teamsList.add(new Team(id));
	}

	public String getTeamsString() {
		String s = "";
		if (teamsList != null) {
			for(Team t: teamsList){
				if(s.length()>0){
					s+="\n";
				}
				s += "Team " + t.getId();
			}
		}

		return s;
	}
}
