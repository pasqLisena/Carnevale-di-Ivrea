package it.polito.applicazionimultimediali.carnevalediivrea.map;

import it.polito.applicazionimultimediali.carnevalediivrea.GlobalRes;
import it.polito.applicazionimultimediali.carnevalediivrea.R;
import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class PlaceBarFragment extends Fragment {

	@Override
	public View onCreateView(LayoutInflater i, ViewGroup vg,
			Bundle savedInstanceState) {

		View v = i.inflate(R.layout.placebar, vg, false);
		Bundle arguments = this.getArguments();
		TextView placeName = ((TextView) v
				.findViewById(R.id.placebar_place_name));

		if (arguments != null && arguments.get("place") != null) {
			int place_id = (Integer) arguments.get("place");
			Place place = GlobalRes.placesList.get(place_id);
			placeName.setText(place.getName());
			if (place.isBattlePlace()) {
				ViewGroup btnCont = (ViewGroup) v
						.findViewById(R.id.placebar_btn_cont);
				Button battleBtn = new Button(getActivity());
				battleBtn.setText(R.string.ivrea_map_button_battle);
				btnCont.addView(battleBtn);
			}
		}
		return v;
	}

}
