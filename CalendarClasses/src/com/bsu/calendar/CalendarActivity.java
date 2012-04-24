package com.bsu.calendar;

import java.util.ArrayList;
import java.util.Date;

import com.bsu.calendar.entities.dao.BellDao;
import com.bsu.calendar.entities.dao.NoteDao;
import com.bsu.calendar.entities.program.Bell;
import com.bsu.calendar.entities.program.Note;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

public class CalendarActivity extends Activity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
//        testBell();
//        testNote();
       
    }
    
    public void testNote() {
    	NoteDao noteDao = new NoteDao(this);
    	
//    	noteDao.create(new Note("n1", "lang", "bla-bla-bla"));
//    	noteDao.create(new Note("n2", "lang", "bla-bla-bla"));
//    	noteDao.create(new Note("n3", "fang", "bla-bla-bla"));
//    	noteDao.create(new Note("n4", "fang", "bla-bla-bla"));
//    	noteDao.create(new Note("n5", "fang", "bla-bla-bla"));
//    	noteDao.create(new Note("n6", "gang", "bla-bla-bla"));
    	
//    	Log.d("...................", ".................");
//    	ArrayList<Note> notes = noteDao.getAll();
//        for (int i = 0; i < notes.size(); ++i) {
//        	Log.d("note", notes.get(i).toString());
//        }
       
//        Log.d("...................", ".................");
//        ArrayList<String> types = new ArrayList<String>();
//        types.add("lang");
//        types.add("gang");
//        notes = noteDao.getByType(types);
//        for (int i = 0; i < notes.size(); ++i) {
//        	Log.d("note #" + i, notes.get(i).toString());
//        }
        
//        Log.d("...................", ".................");        
//        Date from = new Date(112, 3, 24, 7, 13, 0);
//        //Log.d("from", String.valueOf(from.getTime()));        
//        Log.d("from", from.toString());
//        Date to = new Date(112, 3, 24, 7, 21, 0);
//        //Log.d("to", String.valueOf(to.getTime()));
//        Log.d("to", to.toString());
//        notes = noteDao.getByCrDate(from, to);
//        for (int i = 0; i < notes.size(); ++i) {
//        	Log.d("note #" + i, notes.get(i).toString());
//        }
    }
    
    public void testBell() {
    	 BellDao bellDao = new BellDao(this);
//       bellDao.create(new Bell(new Date()));
//       bellDao.create(new Bell(new Date()));
//       bellDao.create(new Bell(new Date()));
//       bellDao.create(new Bell(new Date()));
//       
       ArrayList<Bell> bells = bellDao.getAll();
       for (int i = 0; i < bells.size(); ++i) {
    	   Log.d("bell #" + i, bells.get(i).toString());
       }
       
       Bell bell = bellDao.getById(8);
       bell.setDate(new Date(123));
       bellDao.update(bell);
       
       bells = bellDao.getAll();
       for (int i = 0; i < bells.size(); ++i) {
       	Log.d("bell #" + i, bells.get(i).toString());
       }
    }
}