package com.withparadox2.strangelife.mythread;

import com.withparadox2.strangelife.WorkOfToday.UpdateUIHandler;

import android.os.Message;

public class ThreadUpdateUI extends Thread {
	
	private UpdateUIHandler myHandler;
	
	public ThreadUpdateUI(String name, UpdateUIHandler myHandler) {
		super(name);
		// TODO Auto-generated constructor stub
		this.myHandler = myHandler;
	}
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		
		while(true){
			try {
				Thread.sleep(1000*30);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			Message msg = myHandler.obtainMessage();
			msg.arg1 = 1;
			msg.sendToTarget();
		}
	}
	

}
