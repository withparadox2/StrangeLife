package com.withparadox2.strangelife.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class CategoryHelper {
	

	 	public static final String KEY_ID = "_id";
	 	public static final String KEY_MOTHER_ID = "mother_id";
	    public static final String KEY_CATEGOTY = "category";
		public static final String CHILDREN_CATEGORY_TABLE_NAME = "children_category";
		public static final String MOTHER_CATEGORY_TABLE_NAME = "mother_category";
		public static final String DATABASE_NAME = "category";
	    private static final int DATABASE_VERSION = 1;

	    private DatabaseHelper mDbHelper;
	    private SQLiteDatabase mDb;

	    /**
	     * Database creation sql statement
	     */
	    private static final String CHILDREN_CATEGORY_TABLE_CREAT = "create table "
				+ CHILDREN_CATEGORY_TABLE_NAME + " ("
				+ KEY_ID +" integer primary key autoincrement, "
    			+ KEY_MOTHER_ID +" integer not null, " 
		        + KEY_CATEGOTY + " text not null);";
	    
	    private static final String MOTHER_CATEGORY_TABLE_CREAT = "create table "
				+ MOTHER_CATEGORY_TABLE_NAME + " ("
				+ KEY_ID +" integer primary key autoincrement, "
		        + KEY_CATEGOTY + " text not null);";
	    
	    
	    private final Context mCtx;

	    private static class DatabaseHelper extends SQLiteOpenHelper {

	        DatabaseHelper(Context context) {
	            super(context, DATABASE_NAME, null, DATABASE_VERSION);
	        }

	        @Override
	        public void onCreate(SQLiteDatabase db) {

	            db.execSQL(CHILDREN_CATEGORY_TABLE_CREAT);
	            db.execSQL(MOTHER_CATEGORY_TABLE_CREAT);
	        }

	        @Override
	        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
	            onCreate(db);
	        }
	    }

	    public CategoryHelper(Context ctx ) {
	        this.mCtx = ctx;
	    }

	    public CategoryHelper open() throws SQLException {
	        mDbHelper = new DatabaseHelper(mCtx);
	        mDb = mDbHelper.getWritableDatabase();
	        return this;
	    }

	    public void close() {
	        mDbHelper.close();
	    }
	    
	    public long creatChildrenCategoryItem(String category, Long motherId) {
	        ContentValues initialValues = new ContentValues();
	        initialValues.put(KEY_CATEGOTY, category);
	        initialValues.put(KEY_MOTHER_ID, motherId);
	        return mDb.insert(CHILDREN_CATEGORY_TABLE_NAME, null, initialValues);
	    }
	    
	    public Cursor fetchAllChildrenCategoryItem(Long motherId){
	    	return mDb.rawQuery("SELECT * FROM "+ CHILDREN_CATEGORY_TABLE_NAME + " WHERE " + KEY_MOTHER_ID + "=" + motherId, null);
	    }
	    
	    public boolean deleteChildrenCategoryItem(long rowId) {
	        return mDb.delete(CHILDREN_CATEGORY_TABLE_NAME, KEY_ID + "=" + rowId, null) > 0;
	    }
	    
	    public long creatMotherCategoryItem(String category) {
	        ContentValues initialValues = new ContentValues();
	        initialValues.put(KEY_CATEGOTY, category);
	        return mDb.insert(MOTHER_CATEGORY_TABLE_NAME, null, initialValues);
	    }
	    
	    public Cursor fetchAllMotherCategoryItem(){
	    	return mDb.rawQuery("SELECT * FROM "+ MOTHER_CATEGORY_TABLE_NAME, null);
	    }
	    
	    public boolean deleteMotherCategoryItem(long rowId) {
	        return mDb.delete(MOTHER_CATEGORY_TABLE_NAME, KEY_ID + "=" + rowId, null) > 0;
	    }
}
