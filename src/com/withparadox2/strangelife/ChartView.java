package com.withparadox2.strangelife;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import com.withparadox2.strangelife.dao.CategoryHelper;
import com.withparadox2.strangelife.dao.NotesDbAdapter;

public class ChartView extends View{
	private CategoryHelper cDbHelper;
	private NotesDbAdapter mDbHelper;
	private List<String> categoryList = new ArrayList<String>();
	private List<Integer> timeConsumeList = new ArrayList<Integer>();
	private Map<String, Integer> timeConsumeMap = new HashMap<String, Integer>();
	private List<Integer> colorList = new ArrayList<Integer>(); 
	private Context context;

	
	public ChartView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		this.context = context;
		cDbHelper = new CategoryHelper(context);
		mDbHelper = new NotesDbAdapter(context);
		cDbHelper.open();
		mDbHelper.open();
		getCategoryList();
		getTimeConsume();
		getColorList();
	}
	
	public ChartView(Context context, AttributeSet attrs){
		super(context, attrs);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		// TODO Auto-generated method stub
		super.onDraw(canvas);
		canvas.drawColor(getResources().getColor(R.color.flat_gray));
		drawChart(canvas);
	}
	
	private void getCategoryList(){
		Cursor c = cDbHelper.fetchAllMotherCategoryItem();
		while(c.moveToNext()){
			categoryList.add(c.getString(c.getColumnIndex(CategoryHelper.KEY_CATEGOTY)));
		}
		c.close();
	}
	
	private void getTimeConsume(){
		Cursor c = null;
		int timeConsume = 0;
		for(int i = 0, length = categoryList.size(); i < length; i++){
			c = mDbHelper.fetchCursorInYearByMotherCategory(categoryList.get(i), NotStaticConstant.DATE);
			while(c.moveToNext()){
				timeConsume = timeConsume + c.getInt(c.getColumnIndex(NotesDbAdapter.KEY_TIME_CONSUME));
			}
			timeConsumeList.add(timeConsume);
			timeConsume = 0;
		}
		c.close();
	}
	
	private void getColorList(){
		colorList.add(context.getResources().getColor(R.color.flat_blue));
		colorList.add(context.getResources().getColor(R.color.flat_green));
		colorList.add(context.getResources().getColor(R.color.flat_red));
		colorList.add(context.getResources().getColor(R.color.flat_purple));
	}
	
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		// TODO Auto-generated method stub
		setMeasuredDimension(320, 470);
	}
	
	private void drawChart(Canvas canvas){
		Paint p = new Paint(Paint.ANTI_ALIAS_FLAG);
		p.setStyle(Paint.Style.FILL);
		RectF rectF = new RectF(40, 40, 280, 280);
		float timeConsume;
		float angle = 0;
		p.setColor(getResources().getColor(R.color.flat_brown));
		canvas.drawCircle(160, 160, 120, p);
		for(int i = 0, length = timeConsumeList.size(); i < length; i++){
			timeConsume = timeConsumeList.get(i);
			p.setColor(colorList.get(i));
			canvas.drawArc(rectF, angle, (timeConsume/1440 * 360), true, p);
			angle = angle + timeConsume/1440 * 360;
		}
		p.setColor(getResources().getColor(R.color.flat_gray));
		canvas.drawCircle(160, 160, 60, p);
		
		
		p.setColor(getResources().getColor(R.color.flat_red));
		canvas.drawRoundRect(new RectF(20, 300, 100, 460), 5, 5, p);
		p.setColor(getResources().getColor(R.color.flat_green));
		canvas.drawRoundRect(new RectF(140, 300, 300, 460), 5, 5, p);
		
		
		int itemLengthY = 30;
		int itemMarginY = 10;
		int startMarginY = 25;
		for(int i = 0; i < timeConsumeList.size(); i++){
			p.setColor(colorList.get(i));
			canvas.drawRoundRect(new RectF(105, 
					300 + 5 +(itemLengthY + itemMarginY) * i, 
					135, 
					300 + 5 + (itemLengthY + itemMarginY) * i + itemLengthY), 
					5, 5, p);
		}
		p.setTextSize(16);
		p.setColor(getResources().getColor(R.color.flat_white));
		p.setTextAlign(Paint.Align.CENTER);
		for(int i = 0; i < categoryList.size(); i++){
			canvas.drawText(categoryList.get(i), 
					60, 
					300 + startMarginY + (itemLengthY + itemMarginY) * i,
					p);
		}
		p.setTextAlign(Paint.Align.LEFT);
		for(int i = 0; i < timeConsumeList.size(); i++){
			canvas.drawText(getTimeConsumeString(timeConsumeList.get(i)) + "   " + timeConsumeList.get(i)*100/1440+"%", 
					150, 
					300 + startMarginY + (itemLengthY + itemMarginY) * i,
					p);
		}
		
	}
	
    private String getTimeConsumeString(int totalTime){
    	int m, h;
    	String ms, hs;
    	m = totalTime % 60;
    	h = totalTime / 60;
    	ms = m < 10 ? "0" + m : "" + m;
    	hs = h < 10 ? "0" + h : "" + h;
    	return hs + " h  " + ms + " m";
    }
	
	

}
