package it.polito.applicazionimultimediali.carnevalediivrea;

import it.polito.applicazionimultimediali.carnevalediivrea.map.Place;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.XmlResourceParser;
import android.util.Log;
import android.util.SparseArray;

/**
 * Questa classe racchiude tutti gli oggetti ritenuti necessari globalmente a
 * livello di applicazione (e non di singola activity)
 * 
 */
public class GlobalRes {
	private static final String DEBUG_TAG = "Global Res";

	public static int orangesPerPlay = 15;

	public static SparseArray<Place> placesList;
	public static List<Team> teamsList;
	private static Context ctx;
	private static CurrentPlayer currentPlayer;

	private static SharedPreferences playerData;

	static void prepareResources(Context context) {
		ctx = context;

		// Load places
		if (GlobalRes.placesList == null) {
			try {
				GlobalRes.placesList = new SparseArray<Place>();
				parsePlaces();
			} catch (XmlPullParserException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		// Load teams
		if (GlobalRes.teamsList == null) {
			try {
				GlobalRes.teamsList = new ArrayList<Team>();
				parseTeams();
			} catch (XmlPullParserException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		// TODO Load current player
		playerData = ctx.getSharedPreferences("CurrentPlayer",
				Context.MODE_PRIVATE);
		currentPlayer = new CurrentPlayer(playerData);
	}

	private static void parseTeams() throws XmlPullParserException, IOException {
		XmlResourceParser xml = ctx.getResources().getXml(R.xml.teams);

		int eventType = -1;
		while (eventType != XmlPullParser.END_DOCUMENT) {
			if (eventType == XmlPullParser.START_DOCUMENT) {
				Log.v(DEBUG_TAG, "Teams Document Start");
			} else if (eventType == XmlPullParser.START_TAG) {
				String parserName = xml.getName();
				if (parserName.equals("team")) {
					int squareId = Integer.parseInt(xml.getAttributeValue(null,
							"square"));
					teamsList.add(new Team(xml.getAttributeValue(null, "id"),
							xml.getAttributeValue(null, "name"), xml
									.getAttributeValue(null, "shortname"),
							placesList.get(squareId)));
				}
			}
			eventType = xml.next();
		}
		Log.v(DEBUG_TAG, "Teams Document End");
	}

	private static void parsePlaces() throws XmlPullParserException,
			IOException {
		XmlResourceParser xml = ctx.getResources().getXml(R.xml.places);

		int eventType = -1;
		while (eventType != XmlPullParser.END_DOCUMENT) {
			if (eventType == XmlPullParser.START_DOCUMENT) {
				Log.v(DEBUG_TAG, "Places Document Start");
			} else if (eventType == XmlPullParser.START_TAG) {
				String parserName = xml.getName();
				if (parserName.equals("place")) {
					int id = Integer
							.parseInt(xml.getAttributeValue(null, "id"));
					String name = xml.getAttributeValue(null, "name");
					String latLng = xml.getAttributeValue(null, "latlng");
					String bg = xml.getAttributeValue(null, "bg");
					String minigame_class = xml.getAttributeValue(null,
							"minigame");
					

					Place p = null;
					if (minigame_class != null) {
						String minigame_mask = xml.getAttributeValue(null, "minigame_mask");
						String minigame_descr = xml.getAttributeValue(null,
								"minigame_descr");
						String minigame_bg = xml.getAttributeValue(null,
								"minigame_bg");
						p = new Place(id, name, latLng, bg, minigame_class,
								minigame_descr, minigame_bg, minigame_mask);
					} else {
						p = new Place(id, name, latLng, bg);
					}
					boolean quiz = "true".equals(xml.getAttributeValue(null,"quiz"));
					p.setQuiz(quiz);
					placesList.put(id, p);
				}
			}
			eventType = xml.next();
		}
		Log.v(DEBUG_TAG, "Places Document End");
	}

	public static CurrentPlayer getCurrentPlayer() {
		return currentPlayer;
	}

	public static String getArancieriLeaderboard() {
		return ctx.getString(R.string.aranceri_leaderboards);
	}
}
