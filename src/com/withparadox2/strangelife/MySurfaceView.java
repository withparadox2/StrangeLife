package com.withparadox2.strangelife;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.withparadox2.strangelife.dao.NotesDbAdapter;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

public class MySurfaceView extends View{
	
	List<Map> myList = new ArrayList<Map>();
	private NotesDbAdapter mDbHelper;
	private String date;
    	
    	Paint myPaint;

    	public MySurfaceView(Context context) {
    		super(context);
    	     mDbHelper = new NotesDbAdapter(context);
    	        mDbHelper.open();
    		myPaint = new Paint();
    		date = NotStaticConstant.DATE;
    		getAllNotes();
    	}
    	
    	public MySurfaceView(Context context, AttributeSet attrs){
    		super(context, attrs);
    		 mDbHelper = new NotesDbAdapter(context);
 	        mDbHelper.open();
 		myPaint = new Paint();
 		date = NotStaticConstant.DATE;
 		System.out.println(date);
 		getAllNotes();
    	}
    	
    	

		@Override
		protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
			// TODO Auto-generated method stub
			setMeasuredDimension(320, 1470);
		}

		@Override
		protected void onDraw(Canvas canvas) {
			// TODO Auto-generated method stub
			super.onDraw(canvas);
			canvas.drawColor(Color.LTGRAY);
			makeBitmap2(canvas);
//			canvas.drawBitmap( makeBitmap2(), 0, 0, myPaint);
			
		}
		
			private void getAllNotes(){
		    	Map<Object, Object> map = new HashMap<Object, Object>();
				Cursor c = mDbHelper.fetchCursorFromYearByDate(date);
			
		    	while(c.moveToNext()){
					map = null;
					map = new HashMap<Object, Object>();
					map.put("start_hour", c.getInt(c.getColumnIndex(NotesDbAdapter.KEY_START_HOUR)));
			    	map.put("end_hour", c.getInt(c.getColumnIndex(NotesDbAdapter.KEY_END_HOUR)));
			    	map.put("start_minute", c.getInt(c.getColumnIndex(NotesDbAdapter.KEY_START_MINUTE)));
			    	map.put("end_minute", c.getInt(c.getColumnIndex(NotesDbAdapter.KEY_END_MINUTE)));
			    	map.put("category_child", c.getString(c.getColumnIndex(NotesDbAdapter.KEY_CATEGORY_CHILD)));
			    	myList.add(map);
		    	}
			c.close();
			}
		    

		    
		    private  void makeBitmap2(Canvas c) {
		    	int startPaddingY = 20;
		    	int startPaddingX = 30;
		    	int verticalLineMarginFromHour = 20;
		    	int verticalLineRidus = 5;
		    	int verticalHourTextSize = 16;
		    	int rectStartX = startPaddingX + verticalLineMarginFromHour + 20;
		    	int rectWidth = 120;
		    	int rectRadius = 5;
		    	int categoryTextSize = 16;
		    	int timeTextMarginFormRect = 10;
		        c.drawColor(getResources().getColor(R.color.flat_gray));
		      
		        Paint backgroundPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		        backgroundPaint.setColor(getResources().getColor(R.color.flat_green));
		        
		        Paint hourPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		        hourPaint.setColor(getResources().getColor(R.color.flat_red));
		        hourPaint.setTextAlign(Paint.Align.CENTER);
		        hourPaint.setTextSize(verticalHourTextSize);
		        
		        Paint shortLinePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		        shortLinePaint.setColor(getResources().getColor(R.color.flat_green));
		        shortLinePaint.setStrokeWidth(2);
		        
		        Paint categoryPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		        categoryPaint.setColor(getResources().getColor(R.color.white));
		        categoryPaint.setTextAlign(Align.CENTER);
		        categoryPaint.setTextSize(categoryTextSize);
		        
		        Paint titlePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		        titlePaint.setColor(Color.WHITE);
		        titlePaint.setTextAlign(Align.LEFT);
		        titlePaint.setTextSize(categoryTextSize);
		        
		        for(int i = 0; i < 25; i++){
		        	c.drawText(""+i, 
		        			startPaddingX, 
		        			startPaddingY + (60 * i) + verticalHourTextSize/2 - 2, 
		        			hourPaint);
		        	c.drawCircle(startPaddingX + verticalLineMarginFromHour, 
		        			startPaddingY + (60 * i), 
		        			verticalLineRidus, hourPaint);
		        	c.drawLine(startPaddingX + verticalLineMarginFromHour, 
		        			startPaddingY + (60 * i) + 2*verticalLineRidus , 
		        			startPaddingX + verticalLineMarginFromHour, 
		        			startPaddingY+(60*i + 60) - 2*verticalLineRidus, 
		        			shortLinePaint);
		        }
		        
		        hourPaint.setColor(Color.RED);
		        hourPaint.setTextAlign(Align.LEFT);
		        hourPaint.setTextSize(10);
		        int start_hour;
		        int end_hour;
		        int start_minute;
		        int end_minute;
		        int totalStart;
		        int totalEnd;
		        int coorY;
		        RectF rect;
		        for(int i = 0;i<myList.size();i++){
		        	start_hour = (Integer)myList.get(i).get("start_hour");
		        	start_minute = (Integer)myList.get(i).get("start_minute");
		        	end_hour = (Integer)myList.get(i).get("end_hour");
		        	end_minute = (Integer)myList.get(i).get("end_minute");
		        	totalStart = start_hour * 60 + start_minute;
		        	totalEnd = end_hour*60 + end_minute;
		        	rect = new RectF(rectStartX, 
		        			startPaddingY + totalStart, 
		        			rectStartX + rectWidth, 
		        			startPaddingY + totalEnd);
		        	c.drawRoundRect(rect, rectRadius, rectRadius, backgroundPaint);
		        	
		        	coorY = startPaddingY + (totalStart + totalEnd) / 2 + categoryTextSize / 2;
		        	
		        	c.drawText(getTimeConsumeString(totalEnd - totalStart),
		        			rectStartX + rectWidth + timeTextMarginFormRect, 
		        			coorY,
		        			titlePaint);
		        	c.drawText((String) myList.get(i).get("category_child"), 
		        			rectStartX + rectWidth / 2, 
		        			coorY, 
		        			categoryPaint);
		        }
		    }
		    
		    
		    private String getTimeConsumeString(int totalTime){
		    	if(totalTime/60 ==0 ){
		    		return totalTime+"m";
		    	}else{
		    		return totalTime/60+"h"+totalTime%60+"m";
		    	}
		    }
		    
		    
		    public void updateData(){
		    	date = NotStaticConstant.DATE;
		    	myList.clear();
		    	getAllNotes();
		    }
    	
    }
