package com.github.groupENIGMA.journalEgocentrique;

import java.util.Calendar;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;

import com.github.groupENIGMA.journalEgocentrique.model.DB;
import com.github.groupENIGMA.journalEgocentrique.model.Entry;

public class ListActivity extends Activity {

	private List<Calendar> menu;
	private DB dataBase;
	private Entry selectedEntry;
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	
	    /*
	     * Popolazione della ListView con i dati forniti dal database
	     * Viene inoltre creato un listener sulla lista che al click
	     * di un certo giorno, visualizza i dettagli dello stesso
	     */
	    menu = dataBase.getDays();
	    ListView entry = (ListView)findViewById(R.id.listView1);
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, R.layout.row, R.id.textViewList);
        entry.setAdapter(arrayAdapter);
        OnItemClickListener clickListener = new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapter, View view,
                int position, long id) {
                selectedEntry = (Entry)adapter.getItemAtPosition(position);
            }
        };
        entry.setOnItemClickListener(clickListener);
        /*
         * Aggiorna i dati sulla schermata d'inizio
         * manca notes
         */
        if(selectedEntry == null){
    	    ImageView i = (ImageView) findViewById(R.id.dailyPhoto); 
    	    i.setImageResource(R.drawable.ic_launcher);
    	    i = (ImageView)findViewById(R.id.emoticon);
    	    i.setImageResource(R.drawable.ic_launcher);
        }
        else{
    	    ImageView i = (ImageView) findViewById(R.id.dailyPhoto); 
    	    i.setImageDrawable(selectedEntry.getPhoto().getDrawable());
    	    i = (ImageView)findViewById(R.id.emoticon);
    	    i.setImageBitmap(selectedEntry.getMood().getPathImage());
        }
        
		setContentView(R.layout.main);
	}


}
