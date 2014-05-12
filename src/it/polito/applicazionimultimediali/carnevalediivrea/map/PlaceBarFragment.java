package it.polito.applicazionimultimediali.carnevalediivrea.map;

import it.polito.applicazionimultimediali.carnevalediivrea.GlobalRes;
import it.polito.applicazionimultimediali.carnevalediivrea.Player;
import it.polito.applicazionimultimediali.carnevalediivrea.R;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

public class PlaceBarFragment extends Fragment {

	@SuppressWarnings("null")
	@Override
	public View onCreateView(LayoutInflater i, ViewGroup vg,
			Bundle savedInstanceState) {

		Bundle arguments = this.getArguments();
		int layout;

		if (arguments == null && arguments.get("place") == null)
			throw new RuntimeException("Undefined place");

		int place_id = (Integer) arguments.get("place");
		Place place = GlobalRes.placesList.get(place_id);
		if (place.isBattlePlace()) {
			layout = R.layout.battle_placebar;
		} else {
			layout = R.layout.general_placebar;
		}

		View v = i.inflate(layout, vg, false);
		TextView placeName = ((TextView) v
				.findViewById(R.id.placebar_place_name));
		placeName.setText(place.getName());

		return v;
	}

}
