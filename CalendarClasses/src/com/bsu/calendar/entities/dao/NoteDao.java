package com.bsu.calendar.entities.dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.Set;

import com.bsu.calendar.entities.program.Bell;
import com.bsu.calendar.entities.program.Note;
import com.bsu.calendar.entities.program.Sys;
import com.bsu.calendar.errors.Err;
import com.bsu.calendar.errors.LastErrors;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class NoteDao {
	
	/**
	 * Helper for current class.
	 */
	private NoteHelper dbHelper;
	
	public NoteDao(Context context) {
		this.dbHelper = new NoteHelper(context);
	}
	
	/**
	 * Creates new record in db-table and gets its id.
	 * @param note Note to save into table.
	 * @return Note with its id from the table.
	 */
	public Note create(Note note) {
		LastErrors.getInstance().clean();
		SQLiteDatabase db = this.dbHelper.getWritableDatabase();
		 
        ContentValues values = this.noteToValues(note);
 
        long id = db.insert(dbHelper.TABLE_NAME, null, values);
        db.close();
        
        if (id == -1) {
        	LastErrors.getInstance().add(new Err("New record was not created."));
        	return null;
        } else {
        	Note nt = this.getById(id);
        	Log.d("Note created: ", nt.toString());
            return nt;
        }
	}
	
	/**
	 * Updates or creates new record in db.
	 * @param note Note to update.
	 * @return Updated Note or null, if something was wrong.
	 */
	public Note update(Note note) {
		LastErrors.getInstance().clean();
		Note nt;
		
		if (note.getId() == null) {
			nt = this.create(note);
		} else {
			SQLiteDatabase db = this.dbHelper.getWritableDatabase();
			 
			ContentValues values = this.noteToValues(note);
	 
	        int res = db.update(dbHelper.TABLE_NAME, values, dbHelper.COLUMN_ID + " = ?",
	                new String[] { String.valueOf(note.getId()) });
	        db.close();
	        if (res != 1) {
	        	LastErrors.getInstance().add(new Err("Wrong number of rows were modified."));
	        	nt = null;
	        } else {
	        	nt = this.getById(note.getId());
	        	Log.d("Note updated: ", nt.toString());
	        }
		}		
		
		return nt;
	}
	
	public Note getById(long id) {
		LastErrors.getInstance().clean();
		SQLiteDatabase db = this.dbHelper.getReadableDatabase();
		
		Cursor cursor = db.query(dbHelper.TABLE_NAME, null, dbHelper.COLUMN_ID + "=?",
                new String[] { String.valueOf(id) }, null, null, null, null);
		if (cursor == null) {
			LastErrors.getInstance().add(new Err("No Bell with such id was found"));
			return null;
		}
        cursor.moveToFirst();
        
        Note note = this.cursorToNote(cursor);
        
        Log.d("Bell getById", note.toString());        
        return note;
	}
	
	public void deleteById(Long id) {
		SQLiteDatabase db = this.dbHelper.getWritableDatabase();
        db.delete(dbHelper.TABLE_NAME, dbHelper.COLUMN_ID + " = ?",
                new String[] { String.valueOf(id) });
        db.close();
	}
	
	public void delete(Note note) {
		this.deleteById(note.getId());
	}

	public ArrayList<Note> getAll() {
		LastErrors.getInstance().clean();
		SQLiteDatabase db = this.dbHelper.getReadableDatabase();
		ArrayList<Note> notes = new ArrayList<Note>();
		
		Cursor cursor = db.query(dbHelper.TABLE_NAME, null, null,
                null, null, null, null, null);		
		if (cursor.moveToFirst()) {
            do {
                Note note = this.cursorToNote(cursor);
                notes.add(note);
            } while (cursor.moveToNext());
        }
                
        return notes;
	}
	
	public ArrayList<Note> getByType(ArrayList<String> types) {
		LastErrors.getInstance().clean();
		SQLiteDatabase db = this.dbHelper.getReadableDatabase();
		ArrayList<Note> notes = new ArrayList<Note>();
		
		for (int i = 0; i < types.size(); ++i) {
			Cursor cursor = db.query(dbHelper.TABLE_NAME, null, dbHelper.COLUMN_TYPE + "=?",
					new String[] { types.get(i) }, null, null, null, null);		
			if (cursor.moveToFirst()) {
	            do {
	                Note note = this.cursorToNote(cursor);
	                notes.add(note);
	            } while (cursor.moveToNext());
	        }
		}
		
		return notes;
	}
	
	public ArrayList<Note> getByCrDate(Date from, Date to) {
		LastErrors.getInstance().clean();
		SQLiteDatabase db = this.dbHelper.getReadableDatabase();
		ArrayList<Note> notes = new ArrayList<Note>();
		
		Cursor cursor = db.query(dbHelper.TABLE_NAME, null,
				dbHelper.COLUMN_CREATED + ">=?" + " and "
				+ dbHelper.COLUMN_CREATED + "<=?",
				new String[] { String.valueOf(from.getTime()),
				String.valueOf(to.getTime()) }, null, null, null, dbHelper.COLUMN_CREATED + " DESC");		
		if (cursor.moveToFirst()) {
            do {
                Note note = this.cursorToNote(cursor);
                notes.add(note);
            } while (cursor.moveToNext());
        }
		
		return notes;
	}
	
	public ArrayList<Note> getByMdDate(Date from, Date to) {
		LastErrors.getInstance().clean();
		SQLiteDatabase db = this.dbHelper.getReadableDatabase();
		ArrayList<Note> notes = new ArrayList<Note>();
		
		Cursor cursor = db.query(dbHelper.TABLE_NAME, null,
				dbHelper.COLUMN_MODIFIED + ">=?" + " and "
				+ dbHelper.COLUMN_MODIFIED + "<=?",
				new String[] { String.valueOf(from.getTime()),
				String.valueOf(to.getTime()) }, null, null, null, dbHelper.COLUMN_MODIFIED + " DESC");		
		if (cursor.moveToFirst()) {
            do {
                Note note = this.cursorToNote(cursor);
                notes.add(note);
            } while (cursor.moveToNext());
        }
		
		return notes;
	}
	
	private ContentValues noteToValues(Note note) {
		ContentValues values = new ContentValues();
    	values.put(dbHelper.COLUMN_CREATED, note.getSys().getCr().getTime());
    	values.put(dbHelper.COLUMN_MODIFIED, note.getSys().getMd().getTime());
    	values.put(dbHelper.COLUMN_TITLE, note.getTitle());
    	values.put(dbHelper.COLUMN_TYPE, note.getType());
    	values.put(dbHelper.COLUMN_CONTENT, note.getCont());
        return values;
	}
	
	private Note cursorToNote(Cursor cursor) {
		Long id = cursor.getLong(0);
		
		Date cr = new Date(cursor.getLong(1));
		Date md = new Date(cursor.getLong(2));
		Sys sys = new Sys(cr, md);
		
		String title = cursor.getString(3);
		String type = cursor.getString(4);
		String cont = cursor.getString(5);
		
		Note note = new Note(id, sys, title, type, cont);        
        return note;
	}

}














