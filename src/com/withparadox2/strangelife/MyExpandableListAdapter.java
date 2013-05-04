package com.withparadox2.strangelife;

import java.util.List;
import java.util.Map;

import com.withparadox2.strangelife.R;
import com.withparadox2.strangelife.WorkOfToday.UpdateUIHandler;
import com.withparadox2.strangelife.dao.CategoryHelper;
import com.withparadox2.strangelife.dao.NotesDbAdapter;
import com.withparadox2.strangelife.util.TimeTool;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.DataSetObserver;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

public class MyExpandableListAdapter extends BaseExpandableListAdapter implements OnClickListener{
	
	private Context context;
	private Map<String, Integer> timeToolMap;
	private List<String> category_list ;
	private List<Integer> flag_list ;
	private List<Long> id_list ;
	public  boolean[] flag ;
	private TimeTool timeTool;
	private NotesDbAdapter mDbHelper;
	private CategoryHelper mCLHelper;
	private long idWhenCreatItemInYear;
	private UpdateUIHandler myHandler;
	private int selectGroupPosition;
	private GroupViewHolder holder;
	private Typeface face;
	

	
    private static final int WORK_BUTTON_TAG = 1;
    private static final int DELETE_BUTTON_TAG = 2;
    private static final int CATEGORY_BUTTON_TAG = 3;
    private static final int START_TIME_BUTTON_TAG = 4;
    private static final int DETATIL_BUTTON_TAG = 5;

	public MyExpandableListAdapter() {
		// TODO Auto-generated constructor stub
	}
	public MyExpandableListAdapter(
			Context context,
			UpdateUIHandler myHandler,
			List<String> category_list,
			List<Integer> flag_list,
			List<Long> id_list,
			boolean[] flag) {
		this.context = context;
		this.category_list = category_list;
		this.flag_list = flag_list;
		this.id_list = id_list;
		mDbHelper = new NotesDbAdapter(context);
		mDbHelper.open();
		mCLHelper = new CategoryHelper(context);
		mCLHelper.open();
		timeTool = new TimeTool();
		this.myHandler = myHandler;
		face=Typeface.createFromAsset(context.getAssets(), "font/DinDisplayProThin.otf");
	}

	@Override
	public Object getChild(int arg0, int arg1) {
		return null;
	}

	@Override
	public long getChildId(int arg0, int arg1) {
		return 0;
	}
	
	public class ChildViewHolder{
		public TextView detailText;
		public TextView timeText;
		
		public Button categoty_edit_button;
		public Button start_time_edit_button;
		public Button detail_edit_button;
	}

	@Override
	public View getChildView(final int groupPosition, int childPosition,
		boolean isLastChild, View convertView, ViewGroup parent) {
		selectGroupPosition = groupPosition;
		ChildViewHolder viewHolder = null;
		if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.expandable_child, null);
            viewHolder = new ChildViewHolder();
            viewHolder.categoty_edit_button = (Button)convertView.findViewById(R.id.categoty_edit);
            viewHolder.start_time_edit_button = (Button)convertView.findViewById(R.id.start_time_edit);
            viewHolder.detail_edit_button = (Button)convertView.findViewById(R.id.detail_edit);
            viewHolder.detailText = (TextView)convertView.findViewById(R.id.child_detail_text);
            convertView.setTag(viewHolder);
        }else{
        	viewHolder = (ChildViewHolder)convertView.getTag();
        }

		final String[] item = mDbHelper.fetchItemByIdInAllNotes(id_list.get(groupPosition));
		 viewHolder.detailText.setTypeface(face);
		 
		 viewHolder.detailText.setText("Detail:"+ item[0]);
		 viewHolder.categoty_edit_button.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
//				getCategoryDialogShow("选择分类",  id_list.get(groupPosition));
				Message msg = myHandler.obtainMessage();
				msg.arg1 = WorkOfToday.CategoryClickMsgFlag;
				msg.obj = id_list.get(groupPosition);
				msg.sendToTarget();
			}
		});
		 
         viewHolder.start_time_edit_button.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				if(flag[groupPosition]==true){
					 new TimePickerDialog(context,
				                mTimeSetListener, timeTool.getHour(),timeTool.getMinute(), false).show();
				}else{
					Toast.makeText(context, "请先开始任务！", Toast.LENGTH_LONG).show();
				}
			}
		});
         viewHolder.detail_edit_button.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				updateDetailDialogShow(id_list.get(groupPosition), 2);
			}
		});
	        
		return convertView;
	}
	
	 @Override
	    public void registerDataSetObserver(DataSetObserver observer) {
	        super.registerDataSetObserver(observer);    
	    }

	@Override
	public int getChildrenCount(int arg0) {
		return 1;
	}

	@Override
	public Object getGroup(int arg0) {
		return null;
	}

	@Override
	public int getGroupCount() {
		return category_list.size();
	}

	@Override
	public long getGroupId(int position) {
		// TODO Auto-generated method stub
		 return id_list.get(position);
	}
	
	public class GroupViewHolder{
		public Button workButton;
		public Button deleteButton;
		public TextView categoryText;
		public TextView timeText;
	}

	@Override
	public View getGroupView(int groupPosition, boolean isExpanded,
		View convertView, ViewGroup parent) {
		flag = null;
		flag = new boolean[getGroupCount()];
		for(int i = 0, length = getGroupCount(); i < length; i++){
			flag[i] = (flag_list.get(i) == 0) ? false : true;
		}
		final int selectPos = groupPosition;
		final Long itemId = id_list.get(selectPos);
		
		GroupViewHolder viewHolder = null;
		if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.expandable_group, null);
            viewHolder = new GroupViewHolder();
            viewHolder.workButton = (Button)convertView.findViewById(R.id.myButton);
            viewHolder.deleteButton = (Button)convertView.findViewById(R.id.deteleItemButton);
            viewHolder.categoryText = (TextView)convertView.findViewById(R.id.group_category_text);
            viewHolder.timeText = (TextView)convertView.findViewById(R.id.group_time_text);
            convertView.setTag(viewHolder);
        }else{
        	viewHolder = (GroupViewHolder)convertView.getTag();
        }
		holder = viewHolder;
		holder.timeText.setTypeface(face);
		holder.categoryText.setTypeface(face);
		holder.categoryText.setText(category_list.get(groupPosition));
		holder.timeText.setText(getTimeDisplayString(selectPos));
		holder.workButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				selectGroupPosition = selectPos;
				timeToolMap = new TimeTool().getTime();			
				if(flag[selectPos] == false){
					mDbHelper.updateOnFlagAndStartTime(itemId,
														timeTool.getDate(),
													   timeToolMap.get("HOUR"),
													   timeToolMap.get("MINUTE"),
													   1);
				}else{
					mDbHelper.updateOnFlagAndEndTime(  itemId,
													   timeToolMap.get("HOUR"),
													   timeToolMap.get("MINUTE"),
													   0);
					
					Cursor c = mDbHelper.fetchSingleCursorById(itemId);
					c.moveToFirst();
					String oldDate = c.getString(c.getColumnIndex(NotesDbAdapter.KEY_NOTE_DATE));
					String[] oldMonthAndDay = oldDate.split("-");//get month and day from yesterday date e.g. 2013-02-03
					
					String detail = c.getString(c.getColumnIndex(NotesDbAdapter.KEY_DETAIL));
					String categoryMother = c.getString(c.getColumnIndex(NotesDbAdapter.KEY_CATEGORY_MOTHER));
					String categoryChild = c.getString(c.getColumnIndex(NotesDbAdapter.KEY_CATEGORY_CHILD));
					int startHour = c.getInt(c.getColumnIndex(NotesDbAdapter.KEY_START_HOUR)); 
					int endHour = c.getInt(c.getColumnIndex(NotesDbAdapter.KEY_END_HOUR));
					int startMinute = c.getInt(c.getColumnIndex(NotesDbAdapter.KEY_START_MINUTE));
					int endMinute = c.getInt(c.getColumnIndex(NotesDbAdapter.KEY_END_MINUTE));
					
					if(oldDate.equals(timeTool.getDate())){
					//wheather the time cross over two days, if not:
						if(timeConsume(c) > 2){
							mDbHelper.creatItemInYear(
									timeToolMap.get("MONTH"), 
									timeToolMap.get("DAY"), 
									detail, 
									categoryMother,
									categoryChild,
									timeTool.getDate(),
									startHour, 
									endHour, 
									startMinute, 
									endMinute, 
									timeConsume(c)
									);
							}else{
							Toast.makeText(context, "时间太短，不计入！", Toast.LENGTH_SHORT).show();
						}
					}else{
						//update yesterday:
						mDbHelper.creatItemInYear(
								Integer.parseInt(oldMonthAndDay[1]),//month
								Integer.parseInt(oldMonthAndDay[2]),//day
								detail, 
								categoryMother,
								categoryChild,
								oldDate,
								startHour, 
								24,
								startMinute, 
								0, 
								timeConsumeYesterday(c)
								);
						//update today
						mDbHelper.creatItemInYear(
								timeToolMap.get("MONTH"), 
								timeToolMap.get("DAY"), 
								detail, 
								categoryMother,
								categoryChild,
								timeTool.getDate(),
								0, 
								endHour, 
								0, 
								endMinute, 
								timeConsumeToday(c)
								);
					}
				}
				Message msg = myHandler.obtainMessage();
				msg.arg1 = WorkOfToday.UpdateListMsgFlag;
				msg.sendToTarget();
			}
		});
		
		holder.deleteButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				mDbHelper.deleteNote(itemId);
        		Message msg = myHandler.obtainMessage();
				msg.arg1 = WorkOfToday.UpdateListMsgFlag;
				msg.sendToTarget();
			}
		});

		if(flag[selectPos]==true){
			holder.workButton.setBackgroundResource(R.drawable.item_over);
		}else{
			holder.workButton.setBackgroundResource(R.drawable.item_start);
		} 
		return convertView;
	}
	
	
	

	@Override
	public boolean hasStableIds() {
		return false;
	}

	@Override
	public boolean isChildSelectable(int arg0, int arg1) {
		return false;
	}
	
	private int timeConsume(Cursor c){
		int[] time = new int[4];
		time[0] = c.getInt(c.getColumnIndex(NotesDbAdapter.KEY_START_HOUR));
		time[1] = c.getInt(c.getColumnIndex(NotesDbAdapter.KEY_END_HOUR));
		time[2] = c.getInt(c.getColumnIndex(NotesDbAdapter.KEY_START_MINUTE));
		time[3] = c.getInt(c.getColumnIndex(NotesDbAdapter.KEY_END_MINUTE));
		return 60*time[1]+time[3]-60*time[0]-time[2];
	}
	
	private int timeConsumeYesterday(Cursor c){
		int[] time = new int[4];
		time[0] = c.getInt(c.getColumnIndex(NotesDbAdapter.KEY_START_HOUR));
		time[1] = 24;
		time[2] = c.getInt(c.getColumnIndex(NotesDbAdapter.KEY_START_MINUTE));
		time[3] = 0;
		return 60*time[1]+time[3]-60*time[0]-time[2];
	}
	
	private int timeConsumeToday(Cursor c){
		int[] time = new int[4];
		time[0] = 0;
		time[1] = c.getInt(c.getColumnIndex(NotesDbAdapter.KEY_END_HOUR));
		time[2] = 0;
		time[3] = c.getInt(c.getColumnIndex(NotesDbAdapter.KEY_END_MINUTE));
		return 60*time[1]+time[3]-60*time[0]-time[2];
	}
	
	 private void updateDetailDialogShow(final long id, final int year_allNotes_flag){
	    final EditText editText = new EditText(context);
	    Cursor c = mDbHelper.fetchSingleCursorById(id_list.get(selectGroupPosition));
	    c.moveToFirst();
	    editText.setText(c.getString(c.getColumnIndex(NotesDbAdapter.KEY_DETAIL)));
     	Builder builder1 = new AlertDialog.Builder(context);
     	builder1.setTitle("是否添加详细?").setView(editText);
     	builder1.setPositiveButton("确定", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					String editString = editText.getText().toString();
					if(year_allNotes_flag == 1){
						mDbHelper.updateOnContentInYear(id, editString);
					}else{
						mDbHelper.updateOnContentInAllNotes(id, editString);
					}
				}
			});
     	builder1.setNegativeButton("取消", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface arg0, int arg1) {
				// TODO Auto-generated method stub
			}
		});
     	AlertDialog dialog = builder1.create();
     	dialog.show();
	    }
	 
	 private String getTimeDisplayString(int position){
		 Cursor c  = mDbHelper.fetchSingleCursorById(id_list.get(position));
		 c.moveToFirst();
		 String oldDate = c.getString(c.getColumnIndex(NotesDbAdapter.KEY_NOTE_DATE));
		 int start_hour = Integer.parseInt(c.getString(c.getColumnIndex(NotesDbAdapter.KEY_START_HOUR)));
		 int start_minute = Integer.parseInt(c.getString(c.getColumnIndex(NotesDbAdapter.KEY_START_MINUTE)));
		 int end_hour = timeTool.getTime().get("HOUR");
		 int end_minute = timeTool.getTime().get("MINUTE");
		 int total;
		 if(oldDate.equals(timeTool.getDate())){
			 total =  (end_hour-start_hour)*60 + (end_minute - start_minute);
		 }else{
			 total =  (24-start_hour)*60 + (0 - start_minute) + end_hour*60 + end_minute;
		 }
		 c.close();
		 if(flag[position] == false){
			 return "Not Start!";
		 }else{
			return "" +total/60+" 小时 " + total % 60 + " 分钟";
		 }
		
	 }
	
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			int tag = (Integer)v.getTag();
			switch(tag){
				case DETATIL_BUTTON_TAG:
					updateDetailDialogShow(id_list.get(selectGroupPosition), 2);
					break;
					
				case START_TIME_BUTTON_TAG:
					if(flag[selectGroupPosition]==true){
						 new TimePickerDialog(context,
					                mTimeSetListener, timeTool.getHour(),timeTool.getMinute(), false).show();
					}else{
						Toast.makeText(context, "请先开始任务！", Toast.LENGTH_LONG).show();
					}
					
					break;
			}
		}
		
		private TimePickerDialog.OnTimeSetListener mTimeSetListener =
			    new TimePickerDialog.OnTimeSetListener() {
			        @Override
					public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
			            mDbHelper.updateStartTimeInAllNotes(hourOfDay, minute, id_list.get(selectGroupPosition));
			            Message msg2 = myHandler.obtainMessage();
						msg2.arg1=1;
						msg2.sendToTarget();
			        }
			    };
	 
}
