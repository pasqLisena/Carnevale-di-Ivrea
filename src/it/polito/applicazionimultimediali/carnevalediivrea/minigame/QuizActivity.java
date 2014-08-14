package it.polito.applicazionimultimediali.carnevalediivrea.minigame;

import it.polito.applicazionimultimediali.carnevalediivrea.GlobalRes;
import it.polito.applicazionimultimediali.carnevalediivrea.R;
import it.polito.applicazionimultimediali.carnevalediivrea.map.MapPane;

import java.io.BufferedReader;
import java.io.Console;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Random;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.Bundle;
import android.os.Handler;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.AssetManager;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

public class QuizActivity extends Activity {

	private TextView domandaText;
	private RadioButton r0, r1, r2;
	
	private int idPosto = 0; 	//id del luogo
	private String jName; 		//nome del file json
	private int domCount=0;
	private int ndomanda=0;
	ArrayList <Integer> ndomandauscita= new ArrayList<Integer>();
	
	int orangeWin=0;
	
	//Creo un array per salvarmi tutte le domande che leggero' dal json del luogo selezionato
	final ArrayList<Domanda> domande = new ArrayList<Domanda>();
	
	
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);
        
        domandaText = (TextView) findViewById(R.id.domandaText);
        r0 = (RadioButton) findViewById(R.id.radio0);
        r1 = (RadioButton) findViewById(R.id.radio1);
        r2 = (RadioButton) findViewById(R.id.radio2);
           
        switch(idPosto){
        case 0:
        	jName = "Castello.json";
        	break;
        
        case 1:
        	jName = "Duomo.json";
        	break;
        	
        case 2:
        	jName = "Olivetti.json";
        	break;
        }
        
        //leggo file json in base allo switch
        //Asset manager serve per gestire ed accedere ai file nella cartella assets
        AssetManager am = getApplicationContext().getAssets();
        try {
			BufferedReader br = new BufferedReader(new InputStreamReader(am.open("QuizData/"+jName)));
			StringBuilder sb = new StringBuilder();
			String line = null;
			
			while((line=br.readLine())!=null){
				sb.append(line);
			}
			String s = sb.toString();
			JSONArray jsArr = new JSONArray(s);
			for(int i=0; i<jsArr.length(); i++){
				JSONObject jsOb = jsArr.getJSONObject(i);
				String testo = jsOb.getString("Domanda");
				String rispGiusta = jsOb.getString("rispGiusta");
				
				//recupero l'array di risposte con un ciclo. Memorizzo la risposta che leggo e l'aggiungo all'array di risposte
				JSONArray rispJson = jsOb.getJSONArray("risposte");
				ArrayList<String> risposte = new ArrayList<String>();
				
				for (int j= 0; j < rispJson.length(); j++) {
				    String rispAttuale = rispJson.getString(j);
				    risposte.add(rispAttuale);
				}
				
				//Creo un nuovo oggetto Domanda e lo aggiungo all'array di domande
				Domanda d = new Domanda(testo, risposte, rispGiusta);
				domande.add(d);
						
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
        //Gestire toast e listener delle risposte
        
        ndomanda=generaNDomanda();
   
        textLayout(ndomanda);    
        
        
        r0.setOnClickListener(new View.OnClickListener() {			
        
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if( isCorretta(ndomanda, r0.getText().toString())){
					corretta();
				}
				else {
					sbagliata();										
				}		
			}
		});
        
        r1.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if( isCorretta(ndomanda, r1.getText().toString())){
					corretta();
				}
				else{
					sbagliata();
				}				
			}
		});
        
        r2.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if( isCorretta(ndomanda, r2.getText().toString())){					
					corretta();
				}
				else{
					sbagliata();
				}
			}
		});
        
    }
    
    
    //Creo un metodo (per ottimizzare codice) per settare il testo della domanda e delle risposte.
    public void textLayout(int i){
    	 domandaText.setText(domande.get(i).getTesto());
         r0.setText(domande.get(i).getRisposte().get(0));
         r1.setText(domande.get(i).getRisposte().get(1));
         r2.setText(domande.get(i).getRisposte().get(2));        
    }
    
    
    public int generaNDomanda(){
    	int n=0;
    	boolean trovato=true;
    	while (trovato==true)
    	{
    		trovato=false;
    			n=new Random().nextInt(10);
    			for(int j=0; j< ndomandauscita.size();j++){
    					if(n==ndomandauscita.get(j))
    					trovato=true;	
    			}
    	}
    	ndomandauscita.add(n);
    	return n;
    }
    
    
    
    //Metodo per controllare quale e' il radiobutton con la risposta corretta. j e' indice per scorrere nell'array di domande.
    public boolean isCorretta(int j, String s){
    	boolean corretta = false;
    	
    	if(domande.get(j).getRispGiusta().equals(s)){
    		corretta = true;
    		orangeWin=orangeWin+10;
			Toast.makeText(getApplicationContext(), "+10 arance", Toast.LENGTH_SHORT).show();
    	}
    	return corretta;
    }
    
    
    public void corretta(){
    	//final Intent i1 = new Intent(getApplicationContext(), WinQuizActivity.class);
    	final Intent i2 = new Intent(getBaseContext(), MapPane.class);
    	if(domCount==9){
    		GlobalRes.getCurrentPlayer().gainOranges(orangeWin);
    		
    		AlertDialog.Builder builder = new AlertDialog.Builder(
					QuizActivity.this);
    		builder.setTitle("Livello completato");
    		builder.setMessage("Complimenti. Hai vinto " + orangeWin + "/100 arance.")
					.setPositiveButton("OK",
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog,
										int id) {
									startActivity(i2);
								}
							});
    		builder.create().show();
			//Toast.makeText(getApplicationContext(), "Hai completato il livello", Toast.LENGTH_SHORT).show();

    	}
		else {
			domCount++;
			ndomanda=generaNDomanda();  
			textLayout(ndomanda);
		//il domCount verra' incrementato in ogni caso, bisogna solo memeorizzare il punteggio
		}
    }
    
    
    public void sbagliata(){
    	//final Intent i0 = new Intent(getApplicationContext(), LooserQuizActivity.class);
    	//startActivity(i0);
    	
		GlobalRes.getCurrentPlayer().gainOranges(orangeWin);
        Toast.makeText(getApplicationContext(), "Non e' la risposta giusta", Toast.LENGTH_SHORT).show();
       
        final Intent i1 = new Intent(getBaseContext(), MapPane.class);
    		
    		AlertDialog.Builder builder = new AlertDialog.Builder(
					QuizActivity.this);
    		builder.setTitle("Livello completato");
    		builder.setMessage("Risposta sbagliata. Non puoi proseguire. Hai vinto " + orangeWin + "/100 arance. Torna a giocare per vincere altre arance.")
					.setPositiveButton("OK",
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog,
										int id) {
									startActivity(i1);
								}
							});
			builder.create().show();
        
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_quiz, menu);
        return true;
    }
}
