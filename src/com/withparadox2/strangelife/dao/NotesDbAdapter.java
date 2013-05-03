/*
 * Copyright (C) 2008 Google Inc.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

package com.withparadox2.strangelife.dao;

import com.withparadox2.strangelife.util.TimeTool;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class NotesDbAdapter {
	
    public static final String KEY_ROWID = "_id";
    public static final String KEY_ID = "_id";
    public static final String KEY_CATEGORY_MOTHER = "category_mother";
    public static final String KEY_CATEGORY_CHILD = "category_child";
    public static final String KEY_DETAIL = "detail";
    public static final String KEY_TIME_CONSUME = "time_consume";
    public static final String KEY_MONTH = "month";
    public static final String KEY_DAY = "day";
    public static final String KEY_START_HOUR = "start_hour";
    public static final String KEY_END_HOUR = "end_hour";
    public static final String KEY_START_MINUTE = "start_minute";
    public static final String KEY_END_MINUTE = "end_minute";
    public static final String KEY_ON_FLAG = "on_flag";
    public static final String KEY_NOTE_DATE = "note_time";
    
    public static final String ALLNOTES_TABLE_NAME = "all_notes";
    public static final String YEAR_TABLE_NAME = "year_"+new TimeTool().getYear();
    
    

    private static final String TAG = "NotesDbAdapter";
    

    private DatabaseHelper mDbHelper;
    private SQLiteDatabase mDb;

    private static final String YEAR_TABLE_CREATE ="create table "
    		+ YEAR_TABLE_NAME +" (_id integer primary key autoincrement, "
    		+ KEY_DETAIL +" text not null, "
    		+ KEY_CATEGORY_MOTHER +" text not null,"
    		+ KEY_CATEGORY_CHILD +" text not null,"
    		+ KEY_NOTE_DATE + " text not null,"
    		+ KEY_MONTH +" integer not null, " 
    		+ KEY_DAY +" integer not null, " 
    		+ KEY_START_HOUR +" integer not null, "
    		+ KEY_END_HOUR +" integer not null, " 
    		+ KEY_START_MINUTE +" integer not null, " 
    		+ KEY_END_MINUTE +" integer not null, "
    		+ KEY_TIME_CONSUME+" integer not null);";
    
    private static final String ALLNOTES_TABLE_CREAT = "create table "
    		+ ALLNOTES_TABLE_NAME +" (_id integer primary key autoincrement, "
    		+ KEY_DETAIL +" text not null, "
    		+ KEY_CATEGORY_MOTHER +" text not null,"
    		+ KEY_CATEGORY_CHILD +" text not null,"
    		+ KEY_NOTE_DATE + " text not null,"
    		+ KEY_ON_FLAG +" integer not null, "
    		+ KEY_START_HOUR +" integer not null, "
    		+ KEY_END_HOUR +" integer not null, " 
    		+ KEY_START_MINUTE +" integer not null, "
    		+ KEY_END_MINUTE +" integer not null);";

    private static final String DATABASE_NAME = "data";
    private static final int DATABASE_VERSION = 1;

    private final Context mCtx;

    private static class DatabaseHelper extends SQLiteOpenHelper {

        DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {

            db.execSQL(YEAR_TABLE_CREATE);
            db.execSQL(ALLNOTES_TABLE_CREAT);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            Log.w(TAG, "Upgrading database from version " + oldVersion + " to "
                    + newVersion + ", which will destroy all old data");
            db.execSQL("DROP TABLE IF EXISTS notes");
            onCreate(db);
        }
    }

    public NotesDbAdapter(Context ctx) {
        this.mCtx = ctx;
    }

    public NotesDbAdapter open() throws SQLException {
        mDbHelper = new DatabaseHelper(mCtx);
        mDb = mDbHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        mDbHelper.close();
    }

    public long createItemInAllNotes(String detail, String date, 
    	String categoryMother, String categoryChild, Integer flag, Integer startHour,Integer endHour,
    	Integer startMinute,Integer endMinute) {
    	
        ContentValues initialValues = new ContentValues();
        initialValues.put(KEY_DETAIL, detail);
        initialValues.put(KEY_NOTE_DATE, date);
        initialValues.put(KEY_CATEGORY_MOTHER, categoryMother);
        initialValues.put(KEY_CATEGORY_CHILD, categoryChild);
        initialValues.put(KEY_ON_FLAG, flag);
        initialValues.put(KEY_START_HOUR, startHour);
        initialValues.put(KEY_END_HOUR, endHour);
        initialValues.put(KEY_START_MINUTE, startMinute);
        initialValues.put(KEY_END_MINUTE, endMinute);
        return mDb.insert(ALLNOTES_TABLE_NAME, null, initialValues);
    }
    
    public long creatItemInYear(int month,int day,String content, 
    	String categoryMother, String categoryChild,  String date, int startHour,int endHour,int startMinute,
    	int endMinute,int timeConsume) {
        
    	ContentValues initialValues = new ContentValues();
        initialValues.put(KEY_DETAIL, content);
        initialValues.put(KEY_DAY, day);
        initialValues.put(KEY_MONTH, month);
        initialValues.put(KEY_CATEGORY_MOTHER, categoryMother);
        initialValues.put(KEY_CATEGORY_CHILD, categoryChild);
        initialValues.put(KEY_TIME_CONSUME, timeConsume);
        initialValues.put(KEY_NOTE_DATE, date);
        initialValues.put(KEY_START_HOUR, startHour);
        initialValues.put(KEY_END_HOUR, endHour);
        initialValues.put(KEY_START_MINUTE, startMinute);
        initialValues.put(KEY_END_MINUTE, endMinute);
        
        return mDb.insert(YEAR_TABLE_NAME, null, initialValues);
    }
    
    public Cursor fetchTodayNotes(){
    	return mDb.rawQuery("SELECT * FROM "+ ALLNOTES_TABLE_NAME  ,null);
    }
    
    public String[] fetchItemByIdInAllNotes(Long itemId){
    	String[] item = new String[2];
    	Cursor c = fetchSingleCursorById(itemId); 
    	c.moveToFirst();
    	String detail = c.getString(c.getColumnIndex(NotesDbAdapter.KEY_DETAIL));
    	if(detail.trim().equals("")){
    		item[0] = "No Detail";    		
    	}else{
    		item[0] = detail;
    	}
    	c.close();
    	return item;
    }
    

    public Cursor fetchSingleCursorById(Long itemId){
    	return   mDb.rawQuery("SELECT * FROM " + ALLNOTES_TABLE_NAME + " WHERE " + KEY_ID + " = '"+ itemId +"'" , null);
    }
    
    public Cursor fetchCursorInYearByMotherCategory(String mother, String date){
    	return   mDb.rawQuery("SELECT * FROM " + YEAR_TABLE_NAME + " WHERE " + KEY_CATEGORY_MOTHER + " = '"+ mother +"' AND "
    			+ KEY_NOTE_DATE + " = '" + date + "'", null);
    }
    
    public boolean updateCategoryInAllNotes(long id,String categoryMother, String categoryChild){
    	ContentValues args = new ContentValues();
        args.put(KEY_CATEGORY_MOTHER, categoryMother);
        args.put(KEY_CATEGORY_CHILD, categoryChild);
        return mDb.update(ALLNOTES_TABLE_NAME, args, KEY_ROWID + "=" + id, null) > 0;
    }
    
    public boolean updateOnFlagAndStartTime(long id, String note_date, int start_hour, int start_minute, int flag ){
      ContentValues args = new ContentValues();
      args.put(KEY_START_HOUR, start_hour);
      args.put(KEY_START_MINUTE, start_minute);
      args.put(KEY_NOTE_DATE, note_date);
      args.put(KEY_ON_FLAG, flag);
      args.put(KEY_ID, id);
      return mDb.update(ALLNOTES_TABLE_NAME, args, KEY_ROWID + "=" + id, null) > 0;
    }
    
    public boolean updateOnFlagAndEndTime(long id,int end_hour,int end_minute,int flag ){
        ContentValues args = new ContentValues();
        args.put(KEY_END_HOUR, end_hour);
        args.put(KEY_END_MINUTE, end_minute);
        args.put(KEY_ON_FLAG, flag);
        args.put(KEY_ID, id);
        return mDb.update(ALLNOTES_TABLE_NAME, args, KEY_ROWID + "=" + id, null) > 0;
      }
    
    public boolean updateStartTimeInAllNotes(int startHour, int startMinute, long id ){
        ContentValues args = new ContentValues();
        args.put(KEY_START_HOUR, startHour);
        args.put(KEY_START_MINUTE, startMinute);
        return mDb.update(ALLNOTES_TABLE_NAME, args, KEY_ROWID + "=" + id, null) > 0;
      }
    
    public boolean updateOnContentInYear(long id,String content ){
        ContentValues args = new ContentValues();
        args.put(KEY_DETAIL, content);
        args.put(KEY_ID, id);
        return mDb.update(YEAR_TABLE_NAME, args, KEY_ROWID + "=" + id, null) > 0;
      }
    
    public boolean updateOnContentInAllNotes(long id,String content ){
        ContentValues args = new ContentValues();
        args.put(KEY_DETAIL, content);
        args.put(KEY_ID, id);
        return mDb.update(ALLNOTES_TABLE_NAME, args, KEY_ROWID + "=" + id, null) > 0;
      }
    
    
    public Cursor fetchCursorFromYearByDate(String date){
    	return  mDb.rawQuery("SELECT * FROM " + YEAR_TABLE_NAME + " WHERE note_time = '"+date+"'" , null);
    }
    
    public Cursor fetchCursorFromYearByCategory(String date,String category){
    	return  mDb.rawQuery("SELECT * FROM " + YEAR_TABLE_NAME + " WHERE note_time = '"+
    date+"'"+"AND category = '"+category+"'" , null);
    }

    public boolean deleteNote(long rowId) {

        return mDb.delete(ALLNOTES_TABLE_NAME, KEY_ROWID + "=" + rowId, null) > 0;
    }

//    public Cursor fetchAllNotes() {
//    	System.out.println("取值");
//        return mDb.query(DATABASE_TABLE, new String[] {KEY_ROWID, KEY_TITLE, KEY_KIND ,KEY_STATUS ,
//        		KEY_TIME_CONSUME, KEY_START_TIME, KEY_DAY
//        		}, null, null, null, null, null);
//        
//    }
    
    public Cursor fetchSingleDayNotes(String singleDay){
    	return mDb.rawQuery("SELECT * FROM notes WHERE day = '"+singleDay+"'" , null);
    }
    
//    public Cursor fetchNote(long rowId) throws SQLException {
//
//        Cursor mCursor =
//
//            mDb.query(true, DATABASE_TABLE, new String[] {KEY_ROWID,
//                    KEY_TITLE}, KEY_ROWID + "=" + rowId, null,
//                    null, null, null, null);
//        if (mCursor != null) {
//            mCursor.moveToFirst();
//        }
//        return mCursor;
//
//    }

//    public boolean updateNote(long rowId, String status, String timeConsume) {
//        ContentValues args = new ContentValues();
//        args.put(KEY_STATUS, status);
//        args.put(KEY_TIME_CONSUME, timeConsume);
//        return mDb.update(DATABASE_TABLE, args, KEY_ROWID + "=" + rowId, null) > 0;
//    }
}
