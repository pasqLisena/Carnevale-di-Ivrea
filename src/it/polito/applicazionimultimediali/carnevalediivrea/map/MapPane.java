package it.polito.applicazionimultimediali.carnevalediivrea.map;

import java.util.HashMap;
import java.util.Map;

import it.polito.applicazionimultimediali.carnevalediivrea.GlobalRes;
import it.polito.applicazionimultimediali.carnevalediivrea.R;
import it.polito.applicazionimultimediali.carnevalediivrea.battle.BattleActivity;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapPane extends Activity implements OnMarkerClickListener {

	private static final String DEBUG_TAG = "Map Pane";
	private Map<Marker, Place> markerMap;
	private Place selectedPlace;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.map_activity);

		markerMap = new HashMap<Marker, Place>();
		// Get a handle to the Map Fragment
		GoogleMap map = ((MapFragment) getFragmentManager().findFragmentById(
				R.id.map)).getMap();
		map.setOnMarkerClickListener(this);

		map.setMyLocationEnabled(true);
		// TODO modificare il centro
		map.moveCamera(CameraUpdateFactory.newLatLngZoom(GlobalRes.placesList
				.get(1).getLatLng(), 16));

		for (int i = 0, nsize = GlobalRes.placesList.size(); i < nsize; i++) {
			Place p = GlobalRes.placesList.valueAt(i);

			BitmapDescriptor marker_icon;

			if (p.isBattlePlace()) {
				marker_icon = BitmapDescriptorFactory
						.fromResource(R.drawable.orange_marker);
			} else if (p.hasMinigame()) {
				marker_icon = BitmapDescriptorFactory
						.fromResource(R.drawable.carnival_marker);
			} else {
				marker_icon = BitmapDescriptorFactory
						.fromResource(R.drawable.historical_marker);
			}

			Marker m = map.addMarker(new MarkerOptions().title(p.getName())
					.snippet(p.getTeamsString()).position(p.getLatLng())
					.icon(marker_icon).anchor(0.5f, 1.0f));

			markerMap.put(m, p);
		}

	}

	@Override
	public boolean onMarkerClick(Marker m) {
		Place p = markerMap.get(m);

		Fragment placeBarFrag = new PlaceBarFragment();
		Bundle data = new Bundle();
		data.putInt("place", p.getId());
		placeBarFrag.setArguments(data);
		if (selectedPlace == null) {
			((ViewGroup) this.findViewById(R.id.placebar_container))
					.removeAllViews();
			selectedPlace = p;
		}

		FragmentTransaction transaction = getFragmentManager()
				.beginTransaction();
		transaction.replace(R.id.placebar_container, placeBarFrag);
		// Commit the transaction
		transaction.commit();

		return true;
	}

	public void goToBattle(View view) {
		Intent intent = new Intent(this, BattleActivity.class);
		// intent.putExtra(EXTRA_MESSAGE, message);
		startActivity(intent);
	}

	public void goToMinigame(View view) {
		Intent intent = new Intent(this, selectedPlace.getMinigame());
		// intent.putExtra(EXTRA_MESSAGE, message);
		startActivity(intent);
	}

}
