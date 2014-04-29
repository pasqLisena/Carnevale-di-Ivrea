package it.polito.applicazionimultimediali.carnevalediivrea;

import java.io.IOException;
import java.util.ArrayList;

import org.xmlpull.v1.XmlPullParserException;

import it.polito.applicazionimultimediali.carnevalediivrea.map.MapPane;
import it.polito.applicazionimultimediali.carnevalediivrea.map.Place;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        GlobalRes.prepareResources(this);
        setContentView(R.layout.activity_main);
    }


	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    
    
    public void goToMap(View view) {
        Intent intent = new Intent(this, MapPane.class);
//        intent.putExtra(EXTRA_MESSAGE, message);
        startActivity(intent);
    }
}
