package com.spellmate;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
public class InitialLevel extends Activity implements OnClickListener{
	private Typeface Font;
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		 Font = Typeface.createFromAsset(getAssets(),"AbrilFatface-Regular_0.ttf");
		setContentView(R.layout.initial_level);
		((Button)findViewById(R.id.beginid)).setOnClickListener(this);
		((Button)findViewById(R.id.juniorid)).setOnClickListener(this);
		((Button)findViewById(R.id.advanceid)).setOnClickListener(this);
		((ImageView)findViewById(R.id.backid)).setOnClickListener(this);
		((Button)findViewById(R.id.beginid)).setTypeface(Font);((Button)findViewById(R.id.juniorid)).setTypeface(Font);
		((Button)findViewById(R.id.advanceid)).setTypeface(Font);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.beginid:
			startActivity(new Intent(getApplicationContext(), Beginner.class).putExtra("type", "begin"));
			break;
		case R.id.juniorid:
			startActivity(new Intent(getApplicationContext(), Beginner.class).putExtra("type", "junior"));
			break;
		case R.id.advanceid:
			startActivity(new Intent(getApplicationContext(), Beginner.class).putExtra("type", "advanced"));
			break;
		case R.id.backid:
			finish();
			break;

		default:
			break;
		}

	}

}
