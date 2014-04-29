package it.polito.applicazionimultimediali.carnevalediivrea;

import it.polito.applicazionimultimediali.carnevalediivrea.map.Place;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.xmlpull.v1.XmlPullParserException;

import android.content.Context;
import android.content.res.XmlResourceParser;
import android.util.Log;

/**
 * Questa classe racchiude tutti gli oggetti ritenuti
 * necessarie globalmente a livello di applicazione
 * (e non di singola activity)
 *
 */
public class GlobalRes {
	private static final String DEBUG_TAG = "Global Res";
	public static List<Place> placesList;
	public static List<Team> teamsList;
	private static Context ctx;

	static void prepareResources(Context context) {
		ctx = context;

		if (GlobalRes.placesList == null) {
			try {
				GlobalRes.placesList = new ArrayList<Place>();
				parsePlaces();
			} catch (XmlPullParserException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
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

	}

	private static void parseTeams() throws XmlPullParserException, IOException {
		XmlResourceParser xmlParser = ctx.getResources().getXml(R.xml.teams);

		int eventType = -1;
		while (eventType != XmlResourceParser.END_DOCUMENT) {
			if (eventType == XmlResourceParser.START_DOCUMENT) {
				Log.v(DEBUG_TAG, "Document Start");
			} else if (eventType == XmlResourceParser.START_TAG) {
				String parserName = xmlParser.getName();
				if (parserName.equals("team")) {
					teamsList.add(new Team(xmlParser.getAttributeValue(null, "id")));
				}
			}
			eventType = xmlParser.next();
		}
		Log.v(DEBUG_TAG, "Document End");
	}

	private static void parsePlaces() throws XmlPullParserException,
			IOException {
		XmlResourceParser xmlParser = ctx.getResources().getXml(R.xml.places);

		int eventType = -1;
		Place p = null;
		while (eventType != XmlResourceParser.END_DOCUMENT) {
			if (eventType == XmlResourceParser.START_DOCUMENT) {
				Log.v(DEBUG_TAG, "Document Start");
			} else if (eventType == XmlResourceParser.START_TAG) {
				String parserName = xmlParser.getName();
				if (parserName.equals("place")) {
					p = new Place(xmlParser.getAttributeValue(null, "id"),
							xmlParser.getAttributeValue(null, "name"),
							xmlParser.getAttributeValue(null, "latlng"));
					placesList.add(p);
				} else if (parserName.equals("team")) {
					p.addTeam(xmlParser.getAttributeValue(null, "id"));
				}
			}
			eventType = xmlParser.next();
		}
		Log.v(DEBUG_TAG, "Document End");
	}
}
