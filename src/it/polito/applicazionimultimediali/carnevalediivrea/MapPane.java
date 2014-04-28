package it.polito.applicazionimultimediali.carnevalediivrea;

import com.google.android.gms.maps.*;
import com.google.android.gms.maps.model.*;
import android.app.Activity;
import android.os.Bundle;

public class MapPane extends Activity {
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.map_activity);

        // Get a handle to the Map Fragment
        GoogleMap map = ((MapFragment) getFragmentManager()
                .findFragmentById(R.id.map)).getMap();

        LatLng castellazzo = new LatLng(45.465541, 7.872252);

        map.setMyLocationEnabled(true);
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(castellazzo, 16));

        map.addMarker(new MarkerOptions()
                .title("Castellazzo")
                .snippet("Qui si fa la preda in dora.")
                .position(castellazzo));
	}

}
