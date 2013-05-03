package com.withparadox2.strangelife.util;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

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
	
	public String getDate(){
		setTime();
		return mYear+"-"+mMonth + "-"+mDay;
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
}
