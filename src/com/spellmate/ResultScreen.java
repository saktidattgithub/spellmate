package com.spellmate;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;



public class ResultScreen extends Activity {

	TextView attemp,correct,wrong,skiped;
	Button replay;
	private Typeface Font;
	SharedPreferences pref;
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.result_screen);
		Font = Typeface.createFromAsset(getAssets(),"AbrilFatface-Regular_0.ttf");
		pref=getSharedPreferences("pref_type", MODE_WORLD_READABLE);
		attemp=(TextView)findViewById(R.id.attempedid);
		correct=(TextView)findViewById(R.id.ansid);
		wrong=(TextView)findViewById(R.id.worngid);
		skiped=(TextView)findViewById(R.id.skipid);

		attemp.setTypeface(Font);correct.setTypeface(Font);wrong.setTypeface(Font);skiped.setTypeface(Font);
		attemp.setText("You have scored :"+getIntent().getStringExtra("correct")+"/10");
		correct.setText("You have correctly answered :"+getIntent().getStringExtra("correct")+" word/s");
		wrong.setText("You have incorrectly answered :"+getIntent().getStringExtra("wrong")+" word/s");
		skiped.setText("You have Skipped :"+getIntent().getStringExtra("skip")+" word/s");
		replay=(Button)findViewById(R.id.replayid);

		replay.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				startActivity(new Intent(getApplicationContext(), NewGameScreen.class).putExtra("type", pref.getString("type", "begin")).putExtra("level", pref.getString("level", "1")));

				//startActivity(new Intent(ResultScreen.this, InitialLevel.class));	
			}
		});

	}


}
