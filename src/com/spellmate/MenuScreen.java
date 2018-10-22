package com.spellmate;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;

import com.spellmate.fb.FacebookConnector;
import com.spellmate.fb.SessionEvents;
import com.spellmate.inappbill.InAppBillCommunication;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;




public class MenuScreen extends Activity implements OnClickListener{
	private static final String FACEBOOK_APPID = "433118696817179";
	private static final String FACEBOOK_PERMISSION = "publish_stream";
	private static final String TAG = "Facebook";
	private final Handler mFacebookHandler = new Handler();
	private TextView loginStatus;
	private Typeface Font;
	private FacebookConnector facebookConnector;
	AlertDialog.Builder dialog;
	
	private InAppBillCommunication inappbill;
	private SharedPreferences my_pref;
	private SharedPreferences.Editor pref_edit;
	private StringBuffer buffer;
	final Runnable mUpdateFacebookNotification = new Runnable() {
		@Override
		public void run() {
			Toast.makeText(getBaseContext(), "Message sent to wall !", Toast.LENGTH_LONG).show();
			//finish();
		}
	};
	List<String[]> questionList;MediaPlayer mediaPlayer;

	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.menuscreen);
		inappbill=new InAppBillCommunication(MenuScreen.this);
		 Font = Typeface.createFromAsset(getAssets(),"AbrilFatface-Regular_0.ttf");
		((Button)findViewById(R.id.playid)).setOnClickListener(this);
		((Button)findViewById(R.id.fbid)).setOnClickListener(this);
		((Button)findViewById(R.id.mailid)).setOnClickListener(this);((Button)findViewById(R.id.aboutid)).setOnClickListener(this);
		((Button)findViewById(R.id.webbookid)).setOnClickListener(this);((Button)findViewById(R.id.freewebbookid)).setOnClickListener(this);
		this.facebookConnector = new FacebookConnector(FACEBOOK_APPID, this, getApplicationContext(), new String[] {FACEBOOK_PERMISSION});
		((Button)findViewById(R.id.playid)).setTypeface(Font);((Button)findViewById(R.id.mailid)).setTypeface(Font);
		((Button)findViewById(R.id.aboutid)).setTypeface(Font);((Button)findViewById(R.id.webbookid)).setTypeface(Font);
		((Button)findViewById(R.id.freewebbookid)).setTypeface(Font);
		
		dialog=new AlertDialog.Builder(MenuScreen.this);
		dialog.create();
		my_pref=getSharedPreferences("my_pref_menu", MODE_WORLD_READABLE);
		pref_edit=my_pref.edit();
		pref_edit.putString("webbook", "0");
		pref_edit.commit();
		/*readCsv(getApplicationContext());
		String[] test=questionList.get(0);
		String data=test[2];

		try {
			AssetFileDescriptor afd = getAssets().openFd(data);
			mediaPlayer = new MediaPlayer();
			mediaPlayer.setDataSource(afd.getFileDescriptor(),afd.getStartOffset(),afd.getLength());
			mediaPlayer.prepare();
			mediaPlayer.start();
		}
		catch (IllegalArgumentException e) {    }
		catch (IllegalStateException e) { }
		catch (IOException e) { }*/
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.playid:
			startActivity(new Intent(getApplicationContext(), InitialLevel.class));
			break;
		case R.id.fbid:
			//postMessage();
			break;
		case R.id.aboutid:
			startActivity(new Intent(getApplicationContext(), AboutUs.class));
			break;
		case R.id.webbookid:
			buffer=new StringBuffer();buffer.append("webbook");
			if(my_pref.getString("webbook", "0").equals("0"))
				showDialog();
			else
			startActivity(new Intent(getApplicationContext(), Dictionary.class));
			break;
		case R.id.freewebbookid:
			startActivity(new Intent(getApplicationContext(), FreeDictionary.class));
			break;

		case R.id.mailid:
			Intent intent = new Intent(Intent.ACTION_SEND);
			intent.setType("text/plain");
			intent.putExtra(Intent.EXTRA_SUBJECT,"Spell Mate");
			// String aEmailList[] = { ""};
			String aEmailCCList[] = { "support@spellmate.com"};

			// intent.putExtra(android.content.Intent.EXTRA_EMAIL, aEmailList);
			intent.putExtra(android.content.Intent.EXTRA_CC, aEmailCCList);
			intent.putExtra(Intent.EXTRA_TEXT, " I thought to tell you about spell mate app,app designed specially to improve your Spell IQ and as Coach to Spell Competitions");

			startActivity(Intent.createChooser(intent, "Send Email"));
			break;

		default:
			break;
		}
	}
	public final List<String[]> readCsv(Context context) {
		questionList = new ArrayList<String[]>();
		AssetManager assetManager = context.getAssets();

		try {
			InputStream csvStream = getAssets().open("repo/WordRepository.csv");
			InputStreamReader csvStreamReader = new InputStreamReader(csvStream);
			CSVReader csvReader = new CSVReader(csvStreamReader);
			String[] line;

			// throw away the header
			csvReader.readNext();

			while ((line = csvReader.readNext()) != null) {
				questionList.add(line);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return questionList;
	}
		/*protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		this.facebookConnector.getFacebook().authorizeCallback(requestCode, resultCode, data);
	}*/


	@Override
	protected void onResume() {
		super.onResume();
		updateLoginStatus();
	}

	public void updateLoginStatus() {
		facebookConnector.getFacebook().isSessionValid();
	}

	public void postMessage() {

		if (facebookConnector.getFacebook().isSessionValid()) {
			postMessageInThread();
		} else {
			SessionEvents.AuthListener listener = new SessionEvents.AuthListener() {

				@Override
				public void onAuthSucceed() {
					postMessageInThread();
				}

				@Override
				public void onAuthFail(String error) {

				}
			};
			SessionEvents.addAuthListener(listener);
			facebookConnector.login();

		}
	}

	private void postMessageInThread() {
		Thread t = new Thread() {
			public void run() {

				try {
					facebookConnector.postMessageOnWall("Wiley");
					mFacebookHandler.post(mUpdateFacebookNotification);
				} catch (Exception ex) {
					Log.e(TAG, "Error sending msg",ex);
				}
			}
		};
		t.start();
	}

	private void clearCredentials() {
		try {
			facebookConnector.getFacebook().logout(getApplicationContext());
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private void showDialog()
	{
		dialog.setMessage("Please make payment");
		dialog.setNeutralButton("OK", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				// User pressed Cancel button. Write Logic Here
				dialog.dismiss();
				inappbill.onBuy(buffer.toString());
			}
		});

		// Showing Alert Message
		dialog.show();}
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		Log.d("TAG", "onActivityResult(" + requestCode + "," + resultCode + "," + data);

		// Pass on the activity result to the helper for handling
		if(inappbill!=null){
			if (!inappbill.mHelper.handleActivityResult(requestCode, resultCode, data)) {
				// not handled, so handle it ourselves (here's where you'd
				// perform any handling of activity results not related to in-app
				// billing...
				super.onActivityResult(requestCode, resultCode, data);
			}
			else {
				Log.d("TAG", "onActivityResult handled by IABUtil.");
				if(resultCode==-1&&data.getExtras()!=null)
				{
             pref_edit.putString(buffer.toString(), "1");pref_edit.commit();

				}
			}
		}
	}



}
