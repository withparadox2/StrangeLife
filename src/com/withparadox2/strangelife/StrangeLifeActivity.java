package com.withparadox2.strangelife;

import com.withparadox2.strangelife.R;
import com.withparadox2.strangelife.dao.NotesDbAdapter;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class StrangeLifeActivity extends Activity implements OnClickListener{
    /** Called when the activity is first created. */
	private Button buttonOfEnterToday = null;
	private Button buttonOfEnterPast = null;
	private Button buttonOfExit = null;
	private NotesDbAdapter myDbHelper;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        findViews();
        myDbHelper = new NotesDbAdapter(this);
        myDbHelper.open();
        buttonOfEnterToday.setOnClickListener(this);
        buttonOfEnterPast.setOnClickListener(this);
        buttonOfExit.setOnClickListener(this);  
     }
    
    private void findViews(){
    	buttonOfEnterToday = (Button)findViewById(R.id.buttonOfEnterToday);
    	buttonOfEnterPast = (Button)findViewById(R.id.buttonOfEnterPast);
    	buttonOfExit = (Button)findViewById(R.id.buttonOfExit);
    }
	
	private void enterWorkOfToday(){
		Intent intent = new Intent();
		intent.setClass(StrangeLifeActivity.this,WorkOfToday.class);
		startActivity(intent);
	}
	
	private void enterCalendar(){
		Intent intent = new Intent();
		intent.setClass(StrangeLifeActivity.this,DateWidget.class);
		startActivity(intent);
	}
	
	@Override
	public void onClick(View buttonView) {
		// TODO Auto-generated method stub
		switch(buttonView.getId()){
		case R.id.buttonOfEnterToday:
			enterWorkOfToday();
			break;
		case R.id.buttonOfEnterPast:
			enterCalendar();
			break;
		case R.id.buttonOfExit:
			finish();
			break;
	
	}
	}
}