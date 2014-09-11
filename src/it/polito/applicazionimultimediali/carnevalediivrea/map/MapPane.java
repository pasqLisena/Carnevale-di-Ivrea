package it.polito.applicazionimultimediali.carnevalediivrea.map;

import it.polito.applicazionimultimediali.carnevalediivrea.GlobalRes;
import it.polito.applicazionimultimediali.carnevalediivrea.PlayerActivity;
import it.polito.applicazionimultimediali.carnevalediivrea.R;
import java.util.HashMap;
import java.util.Map;

import android.app.Activity;
import android.app.Dialog;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapPane extends Activity implements OnMarkerClickListener {

	private static final String DEBUG_TAG = "Map Pane";
	private Map<Marker, Place> markerMap;
	private Place selectedPlace;
	private View openPlace;
	private boolean firstTime;

	private static SharedPreferences prefs;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.map_activity);

		GlobalRes.prepareResources(getApplicationContext());

		markerMap = new HashMap<Marker, Place>();
		// Get a handle to the Map Fragment
		GoogleMap map = ((MapFragment) getFragmentManager().findFragmentById(
				R.id.map)).getMap();
		map.setOnMarkerClickListener(this);

		UiSettings mapSettings = map.getUiSettings();
		mapSettings.setMyLocationButtonEnabled(false);
		mapSettings.setCompassEnabled(false);
		mapSettings.setRotateGesturesEnabled(false);
		mapSettings.setTiltGesturesEnabled(false);

		CameraPosition cameraPosition = new CameraPosition.Builder()
		.target(GlobalRes.placesList.get(8).getLatLng()).zoom(16)
		.tilt(45).build();
		map.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

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

		openPlace = findViewById(R.id.open_place);

		prefs = getSharedPreferences(
				"it.polito.applicazionimultimediali.carnevalediivrea",
				MODE_PRIVATE);
		firstTime = prefs.getBoolean("FirstTime", true);

		ShowFirstDialog();

	}
	public void ShowFirstDialog(){
		if(firstTime){

			final Dialog dialog1 = new Dialog(this);
			dialog1.requestWindowFeature(Window.FEATURE_NO_TITLE);
			dialog1.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
			dialog1.setContentView(R.layout.popup1);
			dialog1.setCancelable(false);

			Button dialogButton = (Button) dialog1.findViewById(R.id.dialog1ButtonOK);
			// if button is clicked, close the custom dialog
			dialogButton.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					dialog1.dismiss();
				}
			});

			dialog1.show();
		}
	}
	public void ShowSecondDialog(){
		if(firstTime){
			
			final Dialog dialog2 = new Dialog(this);
			dialog2.requestWindowFeature(Window.FEATURE_NO_TITLE);
			dialog2.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
			dialog2.setContentView(R.layout.popup2);
			dialog2.setCancelable(false);

			Button dialogButton = (Button) dialog2.findViewById(R.id.dialog2ButtonOK);
			// if button is clicked, close the custom dialog
			dialogButton.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					dialog2.dismiss();
					firstTime = false;
					SharedPreferences.Editor editor = prefs.edit();
					editor.putBoolean("FirstTime", firstTime );
					editor.commit();
				}
			});

			dialog2.show();
		}
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle presses on the action bar items
		int itemID = item.getItemId();
		if (itemID == R.id.player) {
			Intent intent = new Intent(this, PlayerActivity.class);
			// intent.putExtra(EXTRA_MESSAGE, message);
			startActivity(intent);
		} else {
			return super.onOptionsItemSelected(item);
		}
		return false;
	}

	@Override
	public boolean onMarkerClick(Marker m) {

		ShowSecondDialog();

		Place p = markerMap.get(m);

		Fragment placeBarFrag = new PlaceBarFragment();
		Bundle data = new Bundle();
		data.putInt("place", p.getId());
		placeBarFrag.setArguments(data);
		if (selectedPlace == null) {
			((ViewGroup) this.findViewById(R.id.placebar_container))
			.removeAllViews();
		}
		selectedPlace = p;

		FragmentTransaction transaction = getFragmentManager()
				.beginTransaction();
		transaction.replace(R.id.placebar_container, placeBarFrag);
		// Commit the transaction
		transaction.commit();

		openPlace.setVisibility(View.VISIBLE);

		return true;
	}

	public void openPlace(View view) {
		Intent intent = new Intent(this, PlaceActivity.class);
		intent.putExtra("place", selectedPlace.getId() + "");
		startActivity(intent);
		overridePendingTransition(R.anim.curtainup, R.anim.curtainup_over);
	}
}
