package com.withparadox2.strangelife.custom;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

public class CustomViewGroup extends ViewGroup{
	 private final int VIEW_MARGIN=2;

	public CustomViewGroup(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}
	public CustomViewGroup(Context context, AttributeSet attr){
		super(context, attr);
	}


	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		// TODO Auto-generated method stub
//		System.out.println( "widthMeasureSpec = "+MeasureSpec.toString(widthMeasureSpec)+" heightMeasureSpec"+MeasureSpec.toString(heightMeasureSpec));

		int childMeaWidth = MeasureSpec.makeMeasureSpec(400, MeasureSpec.AT_MOST);
		int childMeaHeight = MeasureSpec.makeMeasureSpec(40, MeasureSpec.EXACTLY);
	     int childCount = getChildCount();
	      for(int i = 0; i < childCount; i ++){
	         View v = getChildAt(i);
	         v.measure(childMeaWidth, childMeaHeight );
	      }
	      
		setMeasuredDimension(320 , getHeightOfGroup(320)+VIEW_MARGIN) ; 
	}


	  @Override
     protected void onLayout(boolean arg0, int left, int top, int right, int bottom) {
		  top=0;
//         System.out.println("changed = "+arg0+" left = "+left+" top = "+top+" right = "+right+" botom = "+arg4);
         final int count = getChildCount();
         List<Integer> lengthXGroup = new ArrayList<Integer>();
         int row=0;
         boolean getPlaceFlag = false;
         lengthXGroup.add(row, 0);
         int lengthX = left;   
         int lengthY = top;   
         for(int i = 0; i < count; i++){
             final View child = this.getChildAt(i);
             int width = child.getMeasuredWidth();
             int height = child.getMeasuredHeight();
             for(int j = 0; j < lengthXGroup.size(); j++){
            	if((lengthXGroup.get(j) + width + VIEW_MARGIN) < right){
            		lengthX = lengthXGroup.get(j) + width + VIEW_MARGIN;
            		lengthY = (j + 1) * (height + VIEW_MARGIN) + top;
            		lengthXGroup.set(j, lengthX);
            		getPlaceFlag = true;
//            		System.out.println("i="+i+"---row="+row+"---width="+width+"----j="+j+"-------"+ lengthX + "------" + lengthY+"-----"+lengthXGroup.get(j));
            		break;
            	}
             }
             if(!getPlaceFlag){
            	 row++;
            	 lengthX = width + VIEW_MARGIN + left;
            	 lengthY=(row + 1) * (height + VIEW_MARGIN) + top;
            	 lengthXGroup.add(row, lengthX);
//          		System.out.println("i="+i+"---row="+row+"---width="+width+"-----"+ lengthX + "------" + lengthY+"-----"+lengthXGroup.get(row));
             }
             
             getPlaceFlag = false;
             child.layout(lengthX-width, lengthY-height, lengthX, lengthY);
         }
 
     }
	  
	 private int getHeightOfGroup(int groupWidth){
		   final int count = getChildCount();
	         int row=0;// which row lay you view relative to parent
	         int lengthX=0;    // right position of child relative to parent
	         int lengthY=0;    // bottom position of child relative to parent
	         for(int i=0;i<count;i++){
	             
	             final View child = this.getChildAt(i);
	             int width = child.getMeasuredWidth();
	             int height = child.getMeasuredHeight();
	             lengthX+=width+VIEW_MARGIN;
	             lengthY=row*(height+VIEW_MARGIN)+VIEW_MARGIN+height+0;
	             //if it can't drawing on a same line , skip to next line
	             if(lengthX>groupWidth){
	                 lengthX=width+VIEW_MARGIN+0;
	                 row++;
	                 lengthY=row*(height+VIEW_MARGIN)+VIEW_MARGIN+height+0;
	             }
	         }
	         return lengthY;
	 }

}
