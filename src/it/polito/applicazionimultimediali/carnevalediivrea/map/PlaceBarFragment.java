package it.polito.applicazionimultimediali.carnevalediivrea.map;

import it.polito.applicazionimultimediali.carnevalediivrea.GlobalRes;
import it.polito.applicazionimultimediali.carnevalediivrea.R;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class PlaceBarFragment extends Fragment {

	@SuppressWarnings("null")
	@Override
	public View onCreateView(LayoutInflater i, ViewGroup vg,
			Bundle savedInstanceState) {

		Bundle arguments = this.getArguments();

		if (arguments == null && arguments.get("place") == null)
			throw new RuntimeException("Undefined place");

		int place_id = (Integer) arguments.get("place");
		Place place = GlobalRes.placesList.get(place_id);

		View v = i.inflate(R.layout.general_placebar, vg, false);
		if (place.isBattlePlace()) {
			v.findViewById(R.id.battle_btn).setVisibility(View.VISIBLE);
		}
		if (place.hasMinigame()) {
			v.findViewById(R.id.minigame_btn).setVisibility(View.VISIBLE);
		}

		TextView placeName = ((TextView) v
				.findViewById(R.id.placebar_place_name));
		placeName.setText(place.getName());

		return v;
	}

}
