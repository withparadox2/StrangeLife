package com.withparadox2.strangelife;

import java.util.zip.Inflater;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.DrawableContainer;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.TextView;

public class StrangeLifeViewActivity extends Activity {
    /** Called when the activity is first created. */
	MySurfaceView mySurfaceView;
	private ChartView chartView;
	private Button leftButton;
	private Button middleButton;
	private Button rightButton;
	
	FrameLayout frameLayout;
	boolean flag = true;
	
	private View layoutView;
	private MyAdapter myAdapter;
	private ListView dynamicListView;

	private int num = 0;
	
	private Drawable leftNormal;
	private Drawable leftPressed;
	private Drawable middleNormal;
	private Drawable middlePressed;
	private Drawable rightNormal;
	private Drawable rightPressed;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
   
        getDayFromCalendar();
        setContentView(R.layout.past_work);
        frameLayout = (FrameLayout) findViewById(R.id.frame_layout);
        leftButton = (Button) findViewById(R.id.left_button);
        middleButton = (Button) findViewById(R.id.middle_button);
        rightButton = (Button) findViewById(R.id.right_button);
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
				num = 30;
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
        
    }
    
    private void getDrawable(){
    	leftNormal    = getResources().getDrawable(R.drawable.left_button_shape_normal);
    	leftPressed   = getResources().getDrawable(R.drawable.left_button_shape_pressed);
    	middleNormal  = getResources().getDrawable(R.drawable.middle_button_shape_normal);
    	middlePressed = getResources().getDrawable(R.drawable.middle_button_shape_pressed);
    	rightNormal   = getResources().getDrawable(R.drawable.right_button_shape_normal);
    	rightPressed  = getResources().getDrawable(R.drawable.right_button_shape_pressed);
    }
    
    private void getDayFromCalendar(){
		NotStaticConstant.DATE = getIntent().getStringExtra("day").toString();
		System.out.println(NotStaticConstant.DATE);
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
    
  
    
    
    
}