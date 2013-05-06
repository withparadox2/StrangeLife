package com.withparadox2.strangelife;

import java.util.Calendar;

import com.withparadox2.strangelife.util.TimeTool;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

public class StrangeLifeViewActivity extends Activity implements OnClickListener{
    /** Called when the activity is first created. */
	private Context context;
	MySurfaceView mySurfaceView;
	private ChartView chartView;
	private Button leftButton;
	private Button middleButton;
	private Button rightButton;
	private Button backButton;
	private TextView dateTextView; 
	private TextView whichKindTextView;
	private PopupWindow popupWindow;
	
	private FrameLayout frameLayout;
	private GestureDetector mGestureDetector;
	boolean flag = true;
	
	private View layoutView;
	private MyAdapter myAdapter;
	private ListView dynamicListView;

	private String formatDateString;

	private int num = 0;
	
	private Drawable leftNormal;
	private Drawable leftPressed;
	private Drawable middleNormal;
	private Drawable middlePressed;
	private Drawable rightNormal;
	private Drawable rightPressed;
	
	private final String TAG = "StrangeLifeActivity";
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
   
        getDateFromCalendar();
        setContentView(R.layout.past_work);
        context = StrangeLifeViewActivity.this;
        frameLayout = (FrameLayout) findViewById(R.id.frame_layout);
        leftButton = (Button) findViewById(R.id.left_button);
        middleButton = (Button) findViewById(R.id.middle_button);
        rightButton = (Button) findViewById(R.id.right_button);
        backButton = (Button) findViewById(R.id.back_button);
        dateTextView = (TextView) findViewById(R.id.date_textview);
        whichKindTextView = (TextView) findViewById(R.id.which_kind_textview);
        dateTextView.setText(new TimeTool().getDateText());
        
        mySurfaceView = new MySurfaceView(this);
        chartView = new ChartView(this);
        
        layoutView =  getLayoutInflater().inflate(R.layout.dynamic_listview, null);
        dynamicListView = (ListView) findViewById(R.id.dynamic_listview);
        myAdapter = new MyAdapter(this);
        dynamicListView.setAdapter(myAdapter);

        
        frameLayout.removeAllViews();
        frameLayout.addView(mySurfaceView);
        getDrawable();
	    leftButton.setBackgroundDrawable(leftPressed);
        leftButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				num = 0;
				myAdapter.notifyDataSetChanged();
				frameLayout.removeAllViews();
			    frameLayout.addView(mySurfaceView);
			    leftButton.setBackgroundDrawable(leftPressed);
			    middleButton.setBackgroundDrawable(middleNormal);
			    rightButton.setBackgroundDrawable(rightNormal);
			}
		});
        
        middleButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				num = 40;
				myAdapter.notifyDataSetChanged();
				frameLayout.removeAllViews();
			    leftButton.setBackgroundDrawable(leftNormal);
			    middleButton.setBackgroundDrawable(middlePressed);
			    rightButton.setBackgroundDrawable(rightNormal);
			}
		});
 
        rightButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				num = 0;
				myAdapter.notifyDataSetChanged();
				frameLayout.removeAllViews();
				frameLayout.addView(chartView);
			    leftButton.setBackgroundDrawable(leftNormal);
			    middleButton.setBackgroundDrawable(middleNormal);
			    rightButton.setBackgroundDrawable(rightPressed);
			}
		});
        
        backButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				StrangeLifeViewActivity.this.finish();
			}
		});
        
        whichKindTextView.setOnClickListener(new popupOnClickListener());
        dateTextView.setOnClickListener(new ChangeDateClickListener());
        
        frameLayout.setOnTouchListener(new myOnTouchListener());
        mGestureDetector = new GestureDetector(context, new myOnGestureListener());
    }
    
    private void getDrawable(){
    	leftNormal    = getResources().getDrawable(R.drawable.left_button_shape_normal);
    	leftPressed   = getResources().getDrawable(R.drawable.left_button_shape_pressed);
    	middleNormal  = getResources().getDrawable(R.drawable.middle_button_shape_normal);
    	middlePressed = getResources().getDrawable(R.drawable.middle_button_shape_pressed);
    	rightNormal   = getResources().getDrawable(R.drawable.right_button_shape_normal);
    	rightPressed  = getResources().getDrawable(R.drawable.right_button_shape_pressed);
    }
    
    private void getDateFromCalendar(){
		NotStaticConstant.DATE = new TimeTool().getDate(); 
		formatDateString = new TimeTool().getDateForamt();
	}
    
    class MyAdapter extends BaseAdapter{
    	LayoutInflater inflater;
    	public MyAdapter(Context ctx){
    		inflater = LayoutInflater.from(ctx);
    	}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return num;
		}

		@Override
		public Object getItem(int arg0) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public long getItemId(int arg0) {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public View getView(int position, View converView, ViewGroup arg2) {
			// TODO Auto-generated method stub
			ViewHolder viewHolder = null;
			if(converView == null){
				converView = getLayoutInflater().inflate(R.layout.dynamic_listview_item, null);
				viewHolder = new ViewHolder();
				viewHolder.x = (TextView) converView.findViewById(R.id.dynamic_listview_text);
				converView.setTag(viewHolder);
			}else{
				viewHolder = (ViewHolder) converView.getTag();
			}
			viewHolder.x.setText(""+position);
			return converView;
		}
		
		class ViewHolder{
			TextView x;
		}
    	
    }

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		
	}
	
	private class popupOnClickListener implements OnClickListener{

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			if(popupWindow == null){
				View popupView = getLayoutInflater().inflate(R.layout.which_kind_popup, null);
				Button yearButton = (Button) popupView.findViewById(R.id.pupup_year_button);
				Button monthButton = (Button) popupView.findViewById(R.id.popup_month_button);
				Button dayButton = (Button) popupView.findViewById(R.id.popup_day_button);
				yearButton.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View arg0) {
						// TODO Auto-generated method stub
						if(popupWindow.isShowing()){
							popupWindow.dismiss();
							Toast.makeText(StrangeLifeViewActivity.this, "Year", Toast.LENGTH_SHORT).show();
							whichKindTextView.setText("年");
						}
					}
				});
				monthButton.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View arg0) {
						// TODO Auto-generated method stub
						if(popupWindow.isShowing()){
							popupWindow.dismiss();
							Toast.makeText(StrangeLifeViewActivity.this, "Year", Toast.LENGTH_SHORT).show();
							whichKindTextView.setText("月");
						}
					}
				});
				dayButton.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View arg0) {
						// TODO Auto-generated method stub
						if(popupWindow.isShowing()){
							popupWindow.dismiss();
							Toast.makeText(StrangeLifeViewActivity.this, "Year", Toast.LENGTH_SHORT).show();
							whichKindTextView.setText("日");
						}
					}
				});
				popupWindow = new PopupWindow(popupView, LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
				popupWindow.setOutsideTouchable(true);
				popupWindow.setBackgroundDrawable(StrangeLifeViewActivity.this.getResources().getDrawable(R.drawable.poput_window_shape_bg));
				popupWindow.showAsDropDown(v, -1, 0);
			}else if(popupWindow.isShowing()){
				popupWindow.dismiss();
			}else{
				popupWindow.showAsDropDown(v);
			}
		}
		
	}
	
	private class ChangeDateClickListener implements OnClickListener{

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			Calendar calendar = Calendar.getInstance();
			Dialog mDialog = new DatePickerDialog(context, onDateSetListener, 
					calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
			mDialog.show();
		}
	}
	
	DatePickerDialog.OnDateSetListener onDateSetListener = new DatePickerDialog.OnDateSetListener() {
		  
		  public void onDateSet(DatePicker view, int year, int monthOfYear,
		    int dayOfMonth) {
		   String month = (monthOfYear + 1) < 10 ? "0" + (monthOfYear + 1) : "" + (monthOfYear + 1);	
		   String day = dayOfMonth < 10 ? "0" + dayOfMonth : "" + dayOfMonth;
		   dateTextView.setText(year + "年" + month  + "月" + day + "日");
		   NotStaticConstant.DATE = year + "-" + (monthOfYear + 1) + "-" + dayOfMonth;
		   formatDateString = year + "-" + month + "-" + day;
		   updateViewAfterChangeDateOrKind();
		  }
		  
		  
	}; 
	
	private void updateViewAfterChangeDateOrKind(){
		chartView.updateData();
		chartView.postInvalidate();
		mySurfaceView.updateData();
		mySurfaceView.postInvalidate();
		
	}
	
	private void getNextOrPreDay(int nextOrPre){
		String date = new TimeTool().getNextOrPreDate("yyyy-MM-dd", formatDateString, nextOrPre);
		formatDateString = date;
		String[] s = date.split("-");
		dateTextView.setText(s[0] + "年" + s[1] + "月" + s[2] + "日");
		updateViewAfterChangeDateOrKind();
	}
	
    class myOnTouchListener implements OnTouchListener{

		@Override
		public boolean onTouch(View arg0, MotionEvent arg1) {
			// TODO Auto-generated method stub
			return mGestureDetector.onTouchEvent(arg1);
			
		}
    	
    }
    
    class myOnGestureListener implements OnGestureListener{

		@Override
		public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
				float velocityY) {
			// TODO Auto-generated method stub
			final int FLING_MIN_DISTANCE = 100, FLING_MIN_VELOCITY = 200;
		    if (e1.getX() - e2.getX() > FLING_MIN_DISTANCE && Math.abs(velocityX) > FLING_MIN_VELOCITY) {
		        // Fling left
		        Log.i("MyGesture", "Fling left");
		        getNextOrPreDay(1);
		    } else if (e2.getX() - e1.getX() > FLING_MIN_DISTANCE && Math.abs(velocityX) > FLING_MIN_VELOCITY) {
		        // Fling right
		        Log.i("MyGesture", "Fling right");
		        getNextOrPreDay(-1);
		    }
		    return false;
		}

		@Override
		public boolean onDown(MotionEvent arg0) {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public void onLongPress(MotionEvent arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public boolean onScroll(MotionEvent arg0, MotionEvent arg1, float arg2,
				float arg3) {
			// TODO Auto-generated method stub
			Log.i(TAG, "onScroll");
			return false;
		}

		@Override
		public void onShowPress(MotionEvent arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public boolean onSingleTapUp(MotionEvent arg0) {
			// TODO Auto-generated method stub
			return false;
		}
    	
    }

	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		// TODO Auto-generated method stub
		 boolean h = super.dispatchTouchEvent(ev);
	     h = mGestureDetector.onTouchEvent(ev);    
	     return h;
	}
    
    

}