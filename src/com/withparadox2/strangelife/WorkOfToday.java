package com.withparadox2.strangelife;

import java.util.ArrayList;
import java.util.List;

import com.withparadox2.strangelife.R;
import com.withparadox2.strangelife.dao.NotesDbAdapter;
import com.withparadox2.strangelife.mythread.ThreadUpdateUI;
import com.withparadox2.strangelife.util.TimeTool;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnGroupExpandListener;
import android.widget.TextView;

public class WorkOfToday extends Activity{
	
	private Button addItemButton = null;
	private Button viewPastButton;
	private TextView titleName;
	private  MyExpandableListAdapter myAdapter;
	private List<String> category_list = new ArrayList<String>();
	private List<Integer> flag_list = new ArrayList<Integer>();
	private List<Long> id_list = new ArrayList<Long>();
	
	private Typeface typeFace;
	
	private NotesDbAdapter mDbHelper;
	private ExpandableListView expandableListView;
	
	private UpdateUIHandler myHandler;
	private ThreadUpdateUI myThread;
	
	public  boolean[] flag ;
	private long itemId;//contained in msg sent from expandableListAdapter
	
	public static final int UpdateListMsgFlag = 1;
	public static final int CategoryClickMsgFlag = 2;

	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.todaywork);
        mDbHelper = new NotesDbAdapter(this);
        mDbHelper.open();

        getNotesList();
        myHandler = new UpdateUIHandler(Looper.myLooper());
        myThread = new ThreadUpdateUI("hh", myHandler);
        
        expandableListView = (ExpandableListView)findViewById(R.id.expandlistview);
        myAdapter = new MyExpandableListAdapter(this,myHandler, category_list, flag_list, id_list, flag);
        expandableListView.setGroupIndicator(null);
        expandableListView.setAdapter(myAdapter);
        myThread.start();
        
        titleName = (TextView)findViewById(R.id.titleFuck);
    	typeFace = Typeface.createFromAsset(getAssets(), "font/DinDisplayProThin.otf");
		titleName.setTypeface(typeFace);
        addItemButton = (Button)findViewById(R.id.addItemButton);
        viewPastButton = (Button)findViewById(R.id.view_past_button);
        addItemButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				mDbHelper.createItemInAllNotes("",
						new TimeTool().getDate(), "", "", 0, 0, 0, 0, 0);
				category_list.clear();
				flag_list.clear();
				id_list.clear();
				getNotesList();
				myAdapter.notifyDataSetChanged();
			}
		});
        viewPastButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent i = new Intent();
				i.setClass(WorkOfToday.this, StrangeLifeViewActivity.class);
				startActivity(i);
			}
		});
        expandableListView.setOnGroupExpandListener(new OnGroupExpandListener() {

			@Override
			public void onGroupExpand(int groupPosition) {
				// TODO Auto-generated method stub
				for (int i = 0; i < myAdapter.getGroupCount(); i++) {
					if (groupPosition != i) {
						expandableListView.collapseGroup(i);
					}
				}

			}

		});
    }
    
	private void getNotesList(){
    	Cursor mCursor = mDbHelper.fetchTodayNotes();
    	while(mCursor.moveToNext()){
    		category_list.add(mCursor.getString(mCursor.getColumnIndex(NotesDbAdapter.KEY_CATEGORY_CHILD)));
    		flag_list.add(mCursor.getInt(mCursor.getColumnIndex(NotesDbAdapter.KEY_ON_FLAG)));
			id_list.add(mCursor.getLong(mCursor.getColumnIndex(NotesDbAdapter.KEY_ID)));
    	}
    	mCursor.close();
    }
	
	public class UpdateUIHandler extends Handler{
		 public UpdateUIHandler(Looper looper){  
	            super(looper);  
	        }
		
	        @Override  
	        public void handleMessage(Message msg) {
	        	switch(msg.arg1){
	        	case UpdateListMsgFlag:
	        		updateListData();
	        		break;
	        	case CategoryClickMsgFlag:
	        		itemId = (Long) msg.obj;
	        		startActivity();
	        	}
	        }
	}
	
	private void updateListData(){
		category_list.clear();
		flag_list.clear();
		id_list.clear();
		getNotesList();
		myAdapter.notifyDataSetChanged();
	}
	
	private void startActivity(){
		Intent i = new Intent();
		i.setClass(this, CategoryActivity.class);
		startActivityForResult(i, 100);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if(resultCode == RESULT_OK){
			String[] category = data.getExtras().getString("category").split("\\|");
			mDbHelper.updateCategoryInAllNotes(itemId, category[0], category[1]);
			updateListData();
		}
	}
	
	
	
	
}