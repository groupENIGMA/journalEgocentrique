package com.github.groupENIGMA.journalEgocentrique.model;

import java.util.Calendar;
import java.util.List;

import android.database.SQLException;

public class DB implements DBInterface {

	@Override
	public void open() throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public void close() {
		// TODO Auto-generated method stub

	}

	@Override
	public Entry getEntryOfTheDay() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Entry getEntryOfTheDay(Calendar day) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Calendar> getDays() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Note insertNote(Entry entry, String note_text)
			throws InvalidOperationException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Note updateNote(Note note, String new_note_text)
			throws InvalidOperationException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void deleteNote(Note note) throws InvalidOperationException {
		// TODO Auto-generated method stub

	}

	@Override
	public void setMood(Entry entry, Mood mood)
			throws InvalidOperationException {
		// TODO Auto-generated method stub

	}

	@Override
	public void removeMood(Entry entry) throws InvalidOperationException {
		// TODO Auto-generated method stub

	}

	@Override
	public List<Mood> getAvailableMoods() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Photo setPhoto(Entry entry, String path)
			throws InvalidOperationException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void deletePhoto(Photo photo) throws InvalidOperationException {
		// TODO Auto-generated method stub

	}

	@Override
	public List<Photo> getPhotos() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Photo> getPhotos(Calendar from, Calendar to) {
		// TODO Auto-generated method stub
		return null;
	}

}
