package journalEgocentrique.model;

import java.util.Calendar;
import java.util.List;

public class Entry implements EntryInterface {

	@Override
	public Calendar getDay() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getId() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Mood getMood() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Mood getPhoto() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Note> getNotes() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean canBeDeleted() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean canBeUpdated() {
		// TODO Auto-generated method stub
		return false;
	}

}
