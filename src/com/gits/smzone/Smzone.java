package com.gits.smzone;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;



public class Smzone extends Activity{

	@Override
	public void onCreate(Bundle savedInstanceState){
		
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, 
               WindowManager.LayoutParams.FLAG_FULLSCREEN);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.splash);
		
		/*
		 * set timeout splash
		 */
		final int welcomeScreenDisplay = 3000;
		
		/*
		 * create thread for splash time
		 */
		Thread welcomeThread = new Thread() {
			int wait = 0;
			
			@Override
			public void run() {
				try {
					super.run();
					
					while (wait<welcomeScreenDisplay) {
						sleep(100);
						wait += 100;
					}
				} catch (Exception e) {
					System.out.println("Exc = " + e);
				}
				
				finally{
					startActivity(new Intent(Smzone.this,ListZoneActivity.class));
					finish();
				}
			}
		};
		
		welcomeThread.start();
		
	}
	
	
}