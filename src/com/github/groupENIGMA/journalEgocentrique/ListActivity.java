package com.github.groupENIGMA.journalEgocentrique;

import java.util.Calendar;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.github.groupENIGMA.journalEgocentrique.model.DB;
import com.github.groupENIGMA.journalEgocentrique.model.Entry;
import com.github.groupENIGMA.journalEgocentrique.model.Note;

public class ListActivity extends Activity {

	private List<Calendar> menu;
	private DB dataBase;
	private Entry selectedEntry = null;
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
		setContentView(R.layout.main);
	       if(selectedEntry == null){
    	    ImageView img = (ImageView) findViewById(R.id.dailyPhoto); 
    	    img.setImageResource(R.drawable.ic_launcher);
    	    img = (ImageView)findViewById(R.id.emoticon);
    	    img.setImageResource(R.drawable.ic_launcher);
        }
	    /*
	     * Caso entry selezionata dall'utente:
	     * vengono aggiornate la foto, il mood e le note.
	     * Controllando se è possibile la modifica si permetta la stessa o meno
	     */
        else{
    	    boolean editable = selectedEntry.canBeUpdated();
    	    ImageView img = (ImageView) findViewById(R.id.dailyPhoto); 
    	    img.setImageURI(Uri.parse(selectedEntry.getPhoto().getPath()));
    	    ImageView mood = (ImageView)findViewById(R.id.emoticon);
    	    mood.setImageURI(Uri.parse(selectedEntry.getMood().getPathImage()));
    	    if(editable){
    	    	 img.setOnTouchListener(new OnTouchListener()
    	         {
    	             @Override
    	             public boolean onTouch(View v, MotionEvent event)
    	             {
    	            	 // qui carica la vista per la fotoCamera
    	                 return false;
    	             }
    	        });
    	    	 mood.setOnTouchListener(new OnTouchListener()
    	    	 {
    	    		@Override
    	    		public boolean onTouch(View v, MotionEvent event)
    	    		{
    	    			// qui carica la vista per il moood
    	    			return false;
    	    		}
    	    	 });
    	    }
    	    LinearLayout notes = (LinearLayout)findViewById(R.id.notes);
    	    List<Note> myNotes = selectedEntry.getNotes();
    	    for(int i = 0;i < myNotes.size();i++){
    	    	EditText tmp = new EditText(this);
    	    	tmp.append(myNotes.get(i).toString());
    	    	tmp.setFocusable(editable);
    	    	notes.addView(tmp);
    	    }
        }
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        TextView yourEditText = (TextView)findViewById(R.id.EditText02);
		imm.showSoftInput(yourEditText, InputMethodManager.SHOW_FORCED);
		setContentView(R.layout.main);
	}


}
