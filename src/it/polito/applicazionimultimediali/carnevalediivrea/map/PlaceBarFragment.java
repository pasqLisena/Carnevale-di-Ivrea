package it.polito.applicazionimultimediali.carnevalediivrea.map;

import it.polito.applicazionimultimediali.carnevalediivrea.GlobalRes;
import it.polito.applicazionimultimediali.carnevalediivrea.R;
import it.polito.applicazionimultimediali.carnevalediivrea.Team;
import android.annotation.SuppressLint;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class PlaceBarFragment extends Fragment {

	private ViewGroup iconCont;

	@SuppressWarnings("null")
	@SuppressLint("DefaultLocale")
	@Override
	public View onCreateView(LayoutInflater i, ViewGroup vg,
			Bundle savedInstanceState) {

		Bundle arguments = this.getArguments();

		if (arguments == null && arguments.get("place") == null)
			throw new RuntimeException("Undefined place");

		int place_id = (Integer) arguments.get("place");
		Place place = GlobalRes.placesList.get(place_id);

		View v = i.inflate(R.layout.general_placebar, vg, false);
		iconCont = (ViewGroup) v.findViewById(R.id.overview_icon_cont);

		if (place.isBattlePlace()) {
			for (Team t : place.getTeamsList()) {
				ImageView badge = new ImageView(getActivity());
				badge.setImageResource(getActivity().getResources()
						.getIdentifier("badge_" + t.getId().toLowerCase(), "drawable",
								getActivity().getPackageName()));
				iconCont.addView(badge, 0, new ViewGroup.LayoutParams(
						ViewGroup.LayoutParams.WRAP_CONTENT,
						ViewGroup.LayoutParams.WRAP_CONTENT));

			}
		}
		
		if (place.hasMinigame()) {
			v.findViewById(R.id.minigame_ico).setVisibility(View.VISIBLE);
		}
		if (place.isQuiz()) {
			v.findViewById(R.id.history_ico).setVisibility(View.VISIBLE);
		}

		TextView placeName = ((TextView) v
				.findViewById(R.id.placebar_place_name));
		placeName.setText(place.getName());

		return v;
	}

}
