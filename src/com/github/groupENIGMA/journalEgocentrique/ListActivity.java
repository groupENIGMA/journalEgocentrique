package com.github.groupENIGMA.journalEgocentrique;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;

import com.github.groupENIGMA.journalEgocentrique.model.DB;
import com.github.groupENIGMA.journalEgocentrique.model.Entry;
import com.github.groupENIGMA.journalEgocentrique.model.Note;

public class ListActivity extends Activity {
	
	public final static String EXTRA_MESSAGE = "com.example.myfirstapp.MESSAGE";
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
	    ListView notes = (ListView)findViewById(R.id.notes);
	    
	    setListView(list, menu);

	    
	   setImages(selectedEntry);
	    
	   setNotes(notes, selectedEntry);
	    }
	
/**
 * Displays the correct notes for the entry selected by the user.
 * @param selected Entry. The entry selected by the user.
 */
	private void setNotes(ListView list, Entry selected) {
        boolean editable = selected.canBeUpdated();
        List<Note> tmp = selected.getNotes();
        List<String> notes = new ArrayList<String>();
        for(int i = 0;i < tmp.size();i++){
        	notes.add(tmp.get(i).getText());
        }
		ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, R.layout.row, notes);
        list.setAdapter(arrayAdapter);
        if(editable){
	        OnItemClickListener clickListener = new OnItemClickListener() {
	
	            @Override
	            public void onItemClick(AdapterView<?> adapter, View view,
	                int position, long id) {
	                Note modify = (Note)adapter.getItemAtPosition(position);
	                Intent intent = new Intent(getApplicationContext(), WriteNote.class);//ho messo WriteNote.class
	                intent.putExtra(EXTRA_MESSAGE, modify.getText());
	                startActivity(intent);
	            }
	        };
	        list.setOnItemClickListener(clickListener);
        }
	}


	/**
	 * The ListView will be populated with the data given by the database.
	 * It is also created a OnItemClickListener that at the click will display the details of the day.
	 * @param list ListView. The list to populate.
	 * @param entry List<Calendar> With this List we will populate the ListView 
	 */
	private void setListView(ListView list, List<Calendar> entry){
		List<String> listEntry = new ArrayList<String>();
		SimpleDateFormat sdf = new SimpleDateFormat("DD/MM/YYYY");
		for(int i = 0;i < entry.size();i++){
			listEntry.add(i, sdf.format(entry.get(i).getTime()));
		}
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, listEntry);
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
    	            	 Intent intent = new Intent(getApplicationContext(), PhotoActivity.class);//ho messo PhotoActivity.class
    	            	 intent.putExtra(EXTRA_MESSAGE, selectedEntry.getId());
    	            	 startActivity(intent);
    	                 return false;
    	             }
    	        });
    	    	 mood.setOnTouchListener(new OnTouchListener()
    	    	 {
    	    		@Override
    	    		public boolean onTouch(View v, MotionEvent event)
    	    		{
    	    			// qui carica la vista per il moood
    	    			Intent intent = new Intent(getApplicationContext(), MoodActivity.class);//ho messo MoodActivity.class
    	    			intent.putExtra(EXTRA_MESSAGE, selectedEntry.getId());
    	    			startActivity(intent);
    	    			return false;
    	    		}
    	    	 });
    	    }
        }
	}


}
