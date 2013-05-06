package com.withparadox2.strangelife.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import com.withparadox2.strangelife.NotStaticConstant;

import android.text.format.DateFormat;

public class TimeTool {
    private int mDay;
    private int mMonth;
    private int mYear;
    
    private int mHour;
    private int mMinute;
    private int mSecond;
    
	private Calendar c;
	private Map<String, Integer> map = new HashMap<String, Integer>();
	
	public Map<String, Integer> getTime(){
		setTime();
		map.put("YEAR", mYear);
		map.put("MONTH", mMonth);
		map.put("DAY", mDay);
		map.put("HOUR", mHour);
		map.put("MINUTE", mMinute);
		map.put("SECOND", mSecond);
		return map;
		
	}
	
	private void setTime(){
		c = Calendar.getInstance();
		mHour = c.get(Calendar.HOUR_OF_DAY);
        mMinute = c.get(Calendar.MINUTE);
        mDay = c.get(Calendar.DAY_OF_MONTH);
        mMonth = c.get(Calendar.MONTH)+1;
        mYear = c.get(Calendar.YEAR);
        mSecond = c.get(Calendar.SECOND);
	}
	
	public String getDateForamt(){
		setTime();
		return mYear + "-" + 
	       (mMonth < 10 ? "0" + mMonth : "" + mMonth) + "-" + 
	       (mDay < 10 ? "0" + mDay : "" + mDay);
	}
	
	public String getDate(){
		setTime();
		return mYear + "-" + mMonth  + "-" + mDay;
	}
	
	public String getDateText(){
		setTime();
		
		return mYear + "年" + 
		       (mMonth < 10 ? "0" + mMonth : "" + mMonth) + "月" + 
		       (mDay < 10 ? "0" + mDay : "" + mDay) + "日";
	}
	
	public String getYear(){
		setTime();
		return mYear+"";
	}
	public int getHour(){
		c = Calendar.getInstance();
		return c.get(Calendar.HOUR_OF_DAY);
	}
	
	public int getMinute(){
		c = Calendar.getInstance();
		return c.get(Calendar.MINUTE);
	}

	public String getNextOrPreDate(String pattern, String date, int preOrNext){
		System.out.println("-------"+date);
		c = Calendar.getInstance();
		SimpleDateFormat dateFormat = new SimpleDateFormat(pattern);
		try {
			c.setTime(dateFormat.parse(date));
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("-------"+c.getTime());
		c.add(Calendar.DATE, preOrNext);
		NotStaticConstant.DATE = c.get(Calendar.YEAR) + "-" + (c.get(Calendar.MONTH) + 1) + "-" + c.get(Calendar.DAY_OF_MONTH);
		System.out.println(NotStaticConstant.DATE);
		return dateFormat.format(c.getTime());
	}
	
}
