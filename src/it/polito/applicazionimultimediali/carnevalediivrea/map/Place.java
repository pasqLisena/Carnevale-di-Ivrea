package it.polito.applicazionimultimediali.carnevalediivrea.map;

import it.polito.applicazionimultimediali.carnevalediivrea.Team;

import java.util.ArrayList;
import java.util.List;

import android.util.Log;

import com.google.android.gms.maps.model.LatLng;

public class Place {
	protected final String mgPackageName = "it.polito.applicazionimultimediali.carnevalediivrea.minigame.";

	private int id;
	private String name, bg;
	private LatLng latLng;

	private List<Team> teamsList;

	private boolean locked;
	private Class<?> minigame;
	private String minigameDescr, minigameBg, minigameMask;
	private boolean quiz;

	public Place(int id, String name, String latLng, String bg,
			String minigame_class, String minigame_descr, String minigame_bg,
			String minigame_mask) {
		this(id, name, latLng, bg);
		if (minigame_class != null && minigame_class.length() > 0) {
			try {
				Log.d("PLACE", minigame_class);
				this.minigame = Class.forName(mgPackageName + minigame_class);
				Log.d("PLACE", this.minigame.toString());

				this.minigameDescr = minigame_descr;
				if (minigame_bg == null) {
					this.minigameBg = minigame_bg;
				} else {
					this.minigameBg = bg;
				}

				Log.w("PLACE", this.minigameBg);
				Log.w("PLACE", this.bg);

				this.minigameMask = minigame_mask;

			} catch (ClassNotFoundException e) {
				// do nothing
				e.printStackTrace();
			}
		}
		if (id == 9 || id == 8) {
			quiz = true;
		}
	}

	public Place(int id, String name, String latLng, String bg) {
		super();
		this.id = id;
		this.name = name;
		this.bg = bg;

		String[] temp = latLng.split(",");
		this.latLng = new LatLng(Double.parseDouble(temp[0]),
				Double.parseDouble(temp[1]));

		this.locked = true;
		this.minigame = null;
		quiz = false;
	}

	public int getId() {
		return id;
	}

	public boolean hasMinigame() {
		return minigame != null;
	}

	public String getName() {
		return name;
	}

	public LatLng getLatLng() {
		return latLng;
	}

	public Class<?> getMinigame() {
		return minigame;
	}

	public String getBg() {
		return bg;
	}

	public String getMinigameDescr() {
		return minigameDescr;
	}

	public String getMinigameBg() {
		return minigameBg;
	}

	public boolean isLocked() {
		return locked;
	}

	public boolean isQuiz() {
		return quiz;
	}

	public void setQuiz(boolean value) {
		quiz = value;
	}

	public void addTeam(Team team) {
		if (teamsList == null) {
			teamsList = new ArrayList<Team>();
		}

		teamsList.add(team);
	}

	public boolean isBattlePlace() {
		return (teamsList != null && teamsList.size() > 0);
	}

	public String getTeamsString() {
		String s = "";
		if (teamsList != null) {
			for (Team t : teamsList) {
				if (s.length() > 0) {
					s += " - ";
				}
				s += t.getShortName();
			}
		}

		return s;
	}

	public List<Team> getTeamsList() {
		return teamsList;
	}

	public String getMinigameMask() {
		return minigameMask;
	}

}
