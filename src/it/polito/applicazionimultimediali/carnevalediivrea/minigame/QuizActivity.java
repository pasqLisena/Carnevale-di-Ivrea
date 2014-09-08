package it.polito.applicazionimultimediali.carnevalediivrea.minigame;

import it.polito.applicazionimultimediali.carnevalediivrea.GlobalRes;
import it.polito.applicazionimultimediali.carnevalediivrea.R;
import it.polito.applicazionimultimediali.carnevalediivrea.map.Place;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Random;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

public class QuizActivity extends Activity {
	private TextView domandaText;
	private RadioButton r0, r1, r2;
	private RadioGroup rg;
	private Place place;

	private int idPosto; // id del luogo
	private String jName; // nome del file json
	private int domCount = 0;
	private int ndomanda = 0;
	ArrayList<Integer> ndomandauscita = new ArrayList<Integer>();

	int orangeWin = 0;

	// Creo un array per salvarmi tutte le domande che leggero' dal json del
	// luogo selezionato
	final ArrayList<Domanda> domande = new ArrayList<Domanda>();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_quiz);

		String placeId = null;
		if (savedInstanceState == null) {
			Bundle extras = getIntent().getExtras();
			if (extras != null) {
				placeId = extras.getString("place");
			}
		} else {
			placeId = (String) savedInstanceState.getSerializable("place");
		}

		if (placeId != null) {
			place = GlobalRes.placesList.get(Integer.parseInt(placeId));

			String bg_place;
			if (place.getBg() == null || place.getBg() == "")
				bg_place = "place_piazzadicitta_bg";
			else
				bg_place = place.getBg();

			int bg = getResources().getIdentifier(bg_place, "drawable",
					getPackageName());
			if (bg != 0)
				((ImageView) findViewById(R.id.bgQuizPlace))
						.setImageResource(bg);

		}

		idPosto = place.getId();

		domandaText = (TextView) findViewById(R.id.domandaText);
		rg = (RadioGroup) findViewById(R.id.radioGroup1);
		r0 = (RadioButton) findViewById(R.id.radio0);
		r1 = (RadioButton) findViewById(R.id.radio1);
		r2 = (RadioButton) findViewById(R.id.radio2);

		jName = "quiz" + idPosto + ".json";

		// Asset manager serve per gestire ed accedere ai file nella cartella
		// assets
		AssetManager am = getApplicationContext().getAssets();
		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(
					am.open("QuizData/" + jName)));
			StringBuilder sb = new StringBuilder();
			String line = null;

			while ((line = br.readLine()) != null) {
				sb.append(line);
			}
			String s = sb.toString();
			JSONArray jsArr = new JSONArray(s);
			for (int i = 0; i < jsArr.length(); i++) {
				JSONObject jsOb = jsArr.getJSONObject(i);
				String testo = jsOb.getString("Domanda");
				String rispGiusta = jsOb.getString("rispGiusta");

				// recupero l'array di risposte con un ciclo. Memorizzo la
				// risposta che leggo e l'aggiungo all'array di risposte
				JSONArray rispJson = jsOb.getJSONArray("risposte");
				ArrayList<String> risposte = new ArrayList<String>();

				for (int j = 0; j < rispJson.length(); j++) {
					String rispAttuale = rispJson.getString(j);
					risposte.add(rispAttuale);
				}

				// Creo un nuovo oggetto Domanda e lo aggiungo all'array di
				// domande
				Domanda d = new Domanda(testo, risposte, rispGiusta);
				domande.add(d);
			}

		} catch (IOException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		}

		ndomanda = generaNDomanda();

		textLayout(ndomanda);

		// Gestire toast e listener delle risposte
		r0.setOnClickListener(radioClickListener);
		r1.setOnClickListener(radioClickListener);
		r2.setOnClickListener(radioClickListener);
	}

	OnClickListener radioClickListener = new View.OnClickListener() {
		public void onClick(View v) {
			if (isCorretta(ndomanda, ((TextView) v).getText().toString())) {
				corretta();
			} else {
				sbagliata();
			}
		}
	};

	// Creo un metodo (per ottimizzare codice) per settare il testo della
	// domanda e delle risposte.
	public void textLayout(int i) {

		domandaText.setText(domande.get(i).getTesto());
		r0.setText(domande.get(i).getRisposte().get(0));
		r1.setText(domande.get(i).getRisposte().get(1));
		r2.setText(domande.get(i).getRisposte().get(2));
		rg.clearCheck();

	}

	public int generaNDomanda() {
		int n = 0;
		boolean trovato = true;
		while (trovato == true) {
			trovato = false;
			n = new Random().nextInt(10);
			for (int j = 0; j < ndomandauscita.size(); j++) {
				if (n == ndomandauscita.get(j))
					trovato = true;
			}
		}
		ndomandauscita.add(n);
		return n;
	}

	// Metodo per controllare quale e' il radiobutton con la risposta corretta.
	// j e' indice per scorrere nell'array di domande.
	public boolean isCorretta(int j, String s) {
		boolean corretta = false;

		if (domande.get(j).getRispGiusta().equals(s)) {
			corretta = true;
			orangeWin = orangeWin + 10;
			Toast.makeText(getApplicationContext(), "+10 arance",
					Toast.LENGTH_SHORT).show();
		}
		return corretta;
	}

	public void corretta() {
		if (domCount == 9) {

			GlobalRes.getCurrentPlayer().gainOranges(orangeWin);

			AlertDialog.Builder builder = new AlertDialog.Builder(
					QuizActivity.this);
			builder.setTitle("Livello completato");
			builder.setMessage(
					"Complimenti. Hai vinto " + orangeWin + "/100 arance.")
					.setPositiveButton("OK",
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog,
										int id) {
									finish();
								}
							});
			builder.create().show();

		} else {
			domCount++;
			ndomanda = generaNDomanda();
			textLayout(ndomanda);
			// il domCount verra' incrementato in ogni caso, bisogna solo
			// memorizzare il punteggio
		}
	}

	public void sbagliata() {
		GlobalRes.getCurrentPlayer().gainOranges(orangeWin);
		Toast.makeText(getApplicationContext(), "Non e' la risposta giusta",
				Toast.LENGTH_SHORT).show();

		AlertDialog.Builder builder = new AlertDialog.Builder(QuizActivity.this);
		builder.setTitle("Partita completata");
		builder.setMessage(
				"Risposta sbagliata, non puoi proseguire. Hai vinto "
						+ orangeWin
						+ "/100 arance. Torna a giocare per vincere altre arance.")
				.setPositiveButton("OK", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int id) {
						finish();
					}
				});
		builder.create().show();

	}
}
