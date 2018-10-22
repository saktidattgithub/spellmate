package com.spellmate;



import com.spellmate.inappbill.InAppBillCommunication;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;


public class Beginner extends Activity implements OnClickListener{
	private String type;
	AlertDialog.Builder dialog;
	private Typeface Font;
	private InAppBillCommunication inappbill;
	private SharedPreferences my_pref;
	private SharedPreferences.Editor pref_edit;
	private StringBuffer buffer;
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.beginner);
		inappbill=new InAppBillCommunication(Beginner.this);
		Font = Typeface.createFromAsset(getAssets(),"AbrilFatface-Regular_0.ttf");
		((Button)findViewById(R.id.lv1id)).setOnClickListener(this);
		((Button)findViewById(R.id.lv2id)).setOnClickListener(this);
		((Button)findViewById(R.id.lv3id)).setOnClickListener(this);
		((Button)findViewById(R.id.lv4id)).setOnClickListener(this);
		((ImageView)findViewById(R.id.backid)).setOnClickListener(this);
		((Button)findViewById(R.id.lv1id)).setTypeface(Font);((Button)findViewById(R.id.lv2id)).setTypeface(Font);
		((Button)findViewById(R.id.lv3id)).setTypeface(Font);((Button)findViewById(R.id.lv4id)).setTypeface(Font);
		type=getIntent().getStringExtra("type");
		dialog=new AlertDialog.Builder(Beginner.this);
		dialog.create();
		my_pref=getSharedPreferences("my_pref", MODE_WORLD_READABLE);
		pref_edit=my_pref.edit();
		pref_edit.putString("two", "0");pref_edit.putString("three", "0");pref_edit.putString("four", "0");
		pref_edit.commit();
	}
	public void onResume()
	{
		super.onResume();
		if(type.equals("advanced"))
		{
			((Button)findViewById(R.id.lv4id)).setVisibility(View.VISIBLE);	
		}

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.backid:
			finish();
			break;
		case R.id.lv1id:
			startActivity(new Intent(getApplicationContext(), NewGameScreen.class).putExtra("type", type).putExtra("level", "1"));
			break;
		case R.id.lv2id:
			buffer=new StringBuffer();buffer.append("two");
			if(my_pref.getString("two", "0").equals("0"))
				showDialog();
			else
				startActivity(new Intent(getApplicationContext(), NewGameScreen.class).putExtra("type", type).putExtra("level", "2"));
			break;
		case R.id.lv3id:
			buffer=new StringBuffer();buffer.append("three");
			if(my_pref.getString("three", "0").equals("0"))
				showDialog();
			else
				startActivity(new Intent(getApplicationContext(), NewGameScreen.class).putExtra("type", type).putExtra("level", "3"));
			break;
		case R.id.lv4id:
			buffer=new StringBuffer();buffer.append("four");
			if(my_pref.getString("four", "0").equals("0"))
				showDialog();
			else
				startActivity(new Intent(getApplicationContext(), NewGameScreen.class).putExtra("type", type).putExtra("level", "4"));
			break;

		default:
			break;
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

