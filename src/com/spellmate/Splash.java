package com.spellmate;

import java.io.IOException;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.Window;

public class Splash extends Activity {
	private DataBaseHelper db_helper;
	private static final int STOPSPLASH = 0;
	// time duration in millisecond for which your splash screen should visible
	// to
	// user. here i have taken half second
	private static final long SPLASHTIME = 1500;

	// handler for splash screen
	private Handler splashHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case STOPSPLASH:
				// Generating and Starting new intent on splash time out
				Intent intent = new Intent(getApplicationContext(),
						MenuScreen.class);
				startActivity(intent);
				Splash.this.finish(); // Updated (Thanks to Jerimiah)
				break;
			}
			super.handleMessage(msg);
		}
	};

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.splash);
		db_helper=new DataBaseHelper(getApplicationContext());
		if(!db_helper.checkDataBase()){
			
			try {
				db_helper.createDataBase();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			Message msg = new Message();
			msg.what = STOPSPLASH;
			splashHandler.sendMessage(msg);
		}else{
			// Generating message and sending it to splash handle
			Message msg = new Message();
			msg.what = STOPSPLASH;
			splashHandler.sendMessageDelayed(msg, SPLASHTIME);
		}
					
		
		
	}
	
}

