package com.github.groupENIGMA.journalEgocentrique.model;

public class Note implements NoteInterface {
	
	private long Id;
	private String Text;
	
	public Note(long id, String text){
		Id = id;
		Text = text;
	}

	@Override
	public String getText() {

		return Text;
	}

	@Override
	public long getId() {

		return Id;
	}

	@Override
	public boolean canBeUpdated() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean canBeDeleted() {
		// TODO Auto-generated method stub
		return false;
	}

}
