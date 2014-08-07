package it.polito.applicazionimultimediali.carnevalediivrea.minigame;

import java.util.ArrayList;

public class Domanda {

	private String testo;
	private ArrayList<String> risposte;
	private String rispGiusta;
	
	public Domanda(String t, ArrayList<String> r, String rG){
		testo = t;
		risposte = r;
		rispGiusta = rG;
	}

	public String getTesto() {
		return testo;
	}

	public void setTesto(String testo) {
		this.testo = testo;
	}

	public ArrayList<String> getRisposte() {
		return risposte;
	}

	public void setRisposte(ArrayList<String> risposte) {
		this.risposte = risposte;
	}

	public String getRispGiusta() {
		return rispGiusta;
	}

	public void setRispGiusta(String rispGiusta) {
		this.rispGiusta = rispGiusta;
	}


}
