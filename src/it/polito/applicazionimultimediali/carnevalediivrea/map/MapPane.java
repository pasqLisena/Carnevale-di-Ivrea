package it.polito.applicazionimultimediali.carnevalediivrea.map;

import it.polito.applicazionimultimediali.carnevalediivrea.GlobalRes;
import it.polito.applicazionimultimediali.carnevalediivrea.R;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.xmlpull.v1.XmlPullParserException;

import android.app.Activity;
import android.content.res.XmlResourceParser;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapPane extends Activity {

	private static final String DEBUG_TAG = "Map Pane";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.map_activity);

		// Get a handle to the Map Fragment
		GoogleMap map = ((MapFragment) getFragmentManager().findFragmentById(
				R.id.map)).getMap();

		map.setMyLocationEnabled(true);
		//TODO modificare il centro
		map.moveCamera(CameraUpdateFactory.newLatLngZoom(GlobalRes.placesList.get(0)
				.getLatLng(), 16));

		for (Place p : GlobalRes.placesList) {
			map.addMarker(new MarkerOptions().title(p.getName())
					.snippet(p.getTeamsString()).position(p.getLatLng()).icon(BitmapDescriptorFactory.fromResource(R.drawable.orange_marker))
	                .anchor(0.5f, 1.0f) // Anchors the marker on the bottom left
	                );
		}
		
		

	}

}
