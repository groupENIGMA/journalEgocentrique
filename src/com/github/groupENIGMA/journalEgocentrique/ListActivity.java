package com.github.groupENIGMA.journalEgocentrique;

import java.util.Calendar;
import java.util.List;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;

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

		setContentView(R.layout.main);
	    dataBase = new DB();
	    menu = dataBase.getDays();

	    ListView list = (ListView)findViewById(R.id.list);
	    
	    setListView(list, menu);
	    
	    setImages(selectedEntry);
	    
	    setNotes(selectedEntry);
	    }
	
/**
 * Displays the correct notes for the entry selected by the user.
 * @param selected Entry. The entry selected by the user.
 */
	private void setNotes(Entry selected) {
    	boolean editable = selectedEntry.canBeUpdated();
	    LinearLayout notes = (LinearLayout)findViewById(R.id.notes);
	    List<Note> myNotes = selectedEntry.getNotes();
	    for(int i = 0;i < myNotes.size();i++){
	    	EditText tmp = new EditText(this);
	    	tmp.append(myNotes.get(i).toString());
	    	tmp.setFocusable(editable);
	    	notes.addView(tmp);
	    }
	}


	/**
	 * The ListView will be populated with the data given by the database.
	 * It is also created a OnItemClickListener that at the click will display the details of the day.
	 * @param list ListView. The list to populate.
	 * @param entry List<Calendar> With this List we will populate the ListView 
	 */
	private void setListView(ListView list, List<Calendar> entry){
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, R.layout.row, R.id.textViewList);
        list.setAdapter(arrayAdapter);
        OnItemClickListener clickListener = new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapter, View view,
                int position, long id) {
                selectedEntry = (Entry)adapter.getItemAtPosition(position);
            }
        };
        list.setOnItemClickListener(clickListener);
	}
	
	
	/**
	 * Sets the correct image for photo and mood selected by the user.
	 * @param selected Entry. The entry selected by the user, its details will be displayed.
	 */
	private void setImages(Entry selected){
	   /*
	    * Caso iniziale:nessun entry selezionata 
	    */
		if(selected == null){
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
    	    boolean editable = selected.canBeUpdated();
    	    ImageView img = (ImageView) findViewById(R.id.dailyPhoto); 
    	    img.setImageURI(Uri.parse(selected.getPhoto().getPath()));
    	    ImageView mood = (ImageView)findViewById(R.id.emoticon);
    	    mood.setImageURI(Uri.parse(selected.getMood().getPathImage()));
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
        }
	}


}
