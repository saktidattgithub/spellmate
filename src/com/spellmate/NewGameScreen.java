package com.spellmate;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.AssetFileDescriptor;
import android.graphics.Typeface;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.AlphaAnimation;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.VideoView;

import com.spellmate.utils.CsvProcessor;



public class NewGameScreen extends Activity implements OnClickListener{
	private VideoView video_player_view;
	private List<String[]> wordList, tenWordSet;MediaPlayer mediaPlayer;
	private int currentItem=0,current=0;
	private String hint,data;
	private EditText etWord;
	private UpdateTask p;
	private int counter=0,correct_counter=0,wrong_counter=0,new_count=0;
	private String word;
	protected Handler handler = new Handler(); 
	private TextView seconds,hintBox,tvTotalProgress,tvWord;
	private ImageView progressBar,overlay_videoid;
	public static Integer[] quick_progress = new Integer[]{R.drawable.s0,R.drawable.s1,R.drawable.s2,R.drawable.s3,R.drawable.s4,R.drawable.s5,R.drawable.s6,R.drawable.s7,
		R.drawable.s8,R.drawable.s9,R.drawable.s10,};
	private String type,level;
	private static final long PLAYTIME = 3000;
	private static final int STOP = 0;
	private Typeface Font;
	private Typeface Font_hint;
	private AudioManager audio;
	SharedPreferences pref_type_lvl;
	SharedPreferences.Editor edit_pref;
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.gamescreen);
		audio = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
		Font = Typeface.createFromAsset(getAssets(),"AbrilFatface-Regular_0.ttf");
		Font_hint = Typeface.createFromAsset(getAssets(),"AbrilFatface-Regular_0.ttf");
		counter=0;
		((ImageView)findViewById(R.id.homeid)).setOnClickListener(this);
		((ImageView)findViewById(R.id.backid)).setOnClickListener(this);
		((Button)findViewById(R.id.lv1id)).setOnClickListener(this);
		((Button)findViewById(R.id.lv2id)).setOnClickListener(this);
		((Button)findViewById(R.id.lv3id)).setOnClickListener(this);
		((Button)findViewById(R.id.lv1id)).setTypeface(Font);((Button)findViewById(R.id.lv2id)).setTypeface(Font);
		((Button)findViewById(R.id.lv3id)).setTypeface(Font);
		progressBar = (ImageView) findViewById(R.id.trackid);
		hintBox = (TextView) findViewById(R.id.hintBox);
		etWord = (EditText) findViewById(R.id.etWord);
		seconds = (TextView) findViewById(R.id.textView2);
		tvTotalProgress = (TextView) findViewById(R.id.tvTotalProgress);
		overlay_videoid = (ImageView) findViewById(R.id.overlay_videoid);
		tvWord = (TextView) findViewById(R.id.tvWord);
		hintBox.setTypeface(Font);
		tvWord.setTypeface(Font);

		pref_type_lvl=getSharedPreferences("pref_type", MODE_WORLD_READABLE);
		
		edit_pref=pref_type_lvl.edit();
		
		type=getIntent().getStringExtra("type");level=getIntent().getStringExtra("level");
		edit_pref.putString("type", type);		edit_pref.putString("level", level);
edit_pref.commit();
		etWord.setOnEditorActionListener(new OnEditorActionListener() {

			@Override
			public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
				if(actionId==EditorInfo.IME_ACTION_DONE)
				{
					hideKeyboard();
					if(currentItem<10)
					{	
						if(etWord.getText().toString().equalsIgnoreCase(word))
						{
							if(correct_counter<=10){
								correct_counter++;new_count++;
								progressBar.setImageResource(quick_progress[new_count]);
								play_happy();
							}
						}
						else
						{
							if(wrong_counter<=10){
								wrong_counter++;new_count++;
								progressBar.setImageResource(quick_progress[new_count]);
								play_sad();}
						}
						//total_progress++;
						tvTotalProgress.setText(currentItem+"/10 complete");
						invalidateButtons(false);
						hintBox.setVisibility(View.INVISIBLE);
						new CountDownTimer(5000,1000) {

							@Override
							public void onTick(long millisUntilFinished) {
								// TODO Auto-generated method stub

							}

							@Override
							public void onFinish() {
								// TODO Auto-generated method stub
								prepareMedia();
								invalidateButtons(true);
							}
						}.start();
					}
					else
					{
						showResult();
					}
					etWord.setText("");
					return true;
				}
				else
					return false;
			}
		});
		new Thread(new Runnable() {

			@Override
			public void run() {
				fillWordList();
				populateTenWordSet();
				prepareMedia();		
			}
		}).start();

		video_player_view=(VideoView)findViewById(R.id.videoid);
		video_player_view.setVisibility(View.INVISIBLE);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.backid:
			finish();
			break;
		case R.id.homeid:
			startActivity(new Intent(getApplicationContext(), MenuScreen.class));
			break;
		case R.id.lv1id:
			hintBox.setVisibility(View.INVISIBLE);hideKeyboard();playAgain();
			/*new Thread(new Runnable() {

				@Override
				public void run() {
					// TODO Auto-generated method stub
					playAgain();
				}
			}).start();*/
			break;
		case R.id.lv2id:
			hintBox.setVisibility(View.INVISIBLE);hideKeyboard();showHint();
			break;
		case R.id.lv3id:
			hintBox.setVisibility(View.INVISIBLE);hideKeyboard();showWord();
			break;

		default:
			break;
		}

	}
	private void hideKeyboard()
	{
		InputMethodManager imm = (InputMethodManager)getSystemService(
				Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(etWord.getWindowToken(), 0);
	}

	private void showResult()
	{
		finish();
		//Toast.makeText(getApplicationContext(), "Correct Answers : "+correct_counter+"\nWrong Answers :"+wrong_counter+"\nSkipped Answers :"+String.valueOf(total_progress-(correct_counter+wrong_counter)), Toast.LENGTH_LONG).show();
		startActivity(new Intent(NewGameScreen.this, ResultScreen.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_CLEAR_TASK).putExtra("correct", String.valueOf(correct_counter)).putExtra("wrong", String.valueOf(wrong_counter)).putExtra("skip", String.valueOf(currentItem-(correct_counter+wrong_counter))));
	}

	private void showHint() {
		hintBox.setVisibility(View.VISIBLE);
		hintBox.setText(hint);
		/*invalidateButtons(false);
		new CountDownTimer(1000,500) {

			@Override
			public void onTick(long millisUntilFinished) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onFinish() {
				// TODO Auto-generated method stub
				//				hintBox.setVisibility(View.INVISIBLE);
				invalidateButtons(true);
			}
		}.start();*/
	}

	private void invalidateButtons(boolean status)
	{
		if(status)
		{
			((Button)findViewById(R.id.lv1id)).setEnabled(true);
			((Button)findViewById(R.id.lv2id)).setEnabled(true);
			((Button)findViewById(R.id.lv3id)).setEnabled(true);
			AlphaAnimation alpha = new AlphaAnimation(1.0F, 1.0F);
			alpha.setDuration(100); // Make animation instant
			alpha.setFillAfter(true); // Tell it to persist after the animation ends
			((Button)findViewById(R.id.lv1id)).startAnimation(alpha);
			((Button)findViewById(R.id.lv2id)).startAnimation(alpha);
			((Button)findViewById(R.id.lv3id)).startAnimation(alpha);
		}
		else
		{
			((Button)findViewById(R.id.lv1id)).setEnabled(false);
			((Button)findViewById(R.id.lv2id)).setEnabled(false);
			((Button)findViewById(R.id.lv3id)).setEnabled(false);
			AlphaAnimation alpha = new AlphaAnimation(0.5F, 0.5F);
			alpha.setDuration(100); // Make animation instant
			alpha.setFillAfter(true); // Tell it to persist after the animation ends
			((Button)findViewById(R.id.lv1id)).startAnimation(alpha);
			((Button)findViewById(R.id.lv2id)).startAnimation(alpha);
			((Button)findViewById(R.id.lv3id)).startAnimation(alpha);
		}
	}

	private void showWord() {
		tvWord.setVisibility(View.VISIBLE);
		tvWord.setText(word.toUpperCase());
		invalidateButtons(false);
		new CountDownTimer(3000,1000) { 

			@Override
			public void onTick(long millisUntilFinished) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onFinish() {
				// TODO Auto-generated method stub
				tvWord.setVisibility(View.INVISIBLE);
				invalidateButtons(true);
				if(currentItem<10)
				{
					new_count++;
					tvTotalProgress.setText(currentItem+"/10 complete");
					progressBar.setImageResource(quick_progress[new_count]);
					prepareMedia();
					/*new Thread(new Runnable() {

						@Override
						public void run() {
							// TODO Auto-generated method stub
							prepareMedia();
						}
					}).start();*/
				}
				else
				{
					//				v.setEnabled(false);
					showResult();
				}
			}
		}.start();
	}

	private void fillWordList()
	{
		wordList = new ArrayList<String[]>();
		if(type.equals("begin"))
		{
			if(level.equals("1"))
			{
				wordList = new CsvProcessor().bgn_lv_one(getApplicationContext());	
			}else if(level.equals("2"))
			{
				wordList = new CsvProcessor().bgn_lv_two(getApplicationContext());	
			}else if(level.equals("3"))
			{
				wordList = new CsvProcessor().bgn_lv_three(getApplicationContext());	
			}
		}else if(type.equals("junior"))
		{
			if(level.equals("1"))
			{
				wordList = new CsvProcessor().jnr_lv_one(getApplicationContext());	
			}else if(level.equals("2"))
			{
				wordList = new CsvProcessor().jnr_lv_two(getApplicationContext());	
			}else if(level.equals("3"))
			{
				wordList = new CsvProcessor().jnr_lv_three(getApplicationContext());	
			}
		}else if(type.equals("advanced"))
		{
			if(level.equals("1"))
			{
				wordList = new CsvProcessor().adv_lv_one(getApplicationContext());	
			}else if(level.equals("2"))
			{
				wordList = new CsvProcessor().adv_lv_two(getApplicationContext());	
			}else if(level.equals("3"))
			{
				wordList = new CsvProcessor().adv_lv_three(getApplicationContext());	
			}else if(level.equals("4"))
			{
				wordList = new CsvProcessor().adv_lv_four(getApplicationContext());	
			}
		}
	}

	private void prepareMedia()
	{
		if(currentItem<10)
		{
			current = currentItem;
			String[] test=tenWordSet.get(currentItem);
			data=test[2];
			word = test[1];
			hint = "Meaning:"+test[6]+"\n"+"Origin:"+test[5]+"\n"+"Pos:"+getExactPos(test[4]);
			currentItem++;
			if(mediaPlayer==null)
			{
				new Thread(new Runnable() {

					@Override
					public void run() {
						// TODO Auto-generated method stub
						try {
							AssetFileDescriptor afd = getAssets().openFd(data);
							mediaPlayer = new MediaPlayer();
							mediaPlayer.setDataSource(afd.getFileDescriptor(),afd.getStartOffset(),afd.getLength());
							mediaPlayer.prepare();
							mediaPlayer.start();
							mediaPlayer.setOnCompletionListener(new OnCompletionListener() {

								@Override
								public void onCompletion(MediaPlayer mp) {
									// TODO Auto-generated method stub
									if(mediaPlayer!=null){
										mediaPlayer.stop();
										mediaPlayer.release();}
									updateTimer();
									mediaPlayer=null;
								}
							});

						}
						catch (Exception e) {    }
					}
				}).start();
			}
			else
			{
				mediaPlayer=null;
			}
		}
	}

	private void playAgain()
	{
		String[] test=tenWordSet.get(current);
		data=test[2];
		hint = test[6];
		word = test[1];
		if(mediaPlayer==null)
		{
			new Thread(new Runnable() {

				@Override
				public void run() {
					// TODO Auto-generated method stub
					try {
						AssetFileDescriptor afd = getAssets().openFd(data);
						mediaPlayer = new MediaPlayer();
						mediaPlayer.setDataSource(afd.getFileDescriptor(),afd.getStartOffset(),afd.getLength());
						mediaPlayer.prepare();
						mediaPlayer.start();
						mediaPlayer.setOnCompletionListener(new OnCompletionListener() {

							@Override
							public void onCompletion(MediaPlayer mp) {
								// TODO Auto-generated method stub
								mediaPlayer.stop();
								mediaPlayer.release();
								mediaPlayer=null;
							}
						});
					}
					catch (Exception e) {    }
				}
			}).start();
		}
		else
		{
			mediaPlayer.stop();
			mediaPlayer.release();
			mediaPlayer=null;
		}
	}

	private void populateTenWordSet()
	{
		tenWordSet = new ArrayList<String[]>(10);
		Random r = new Random();
		for(int i=0;i<10;i++)
		{
			int random=r.nextInt(wordList.size());
			if(!tenWordSet.contains(wordList.get(random)))
				tenWordSet.add(wordList.get(random));
			else
				i -= 1;
		}
	}

	class UpdateTask implements Runnable {

		public UpdateTask(){}

		public void run() {
			counter++;
			seconds.setText(counter+" seconds");
			if(counter==30)
				play_think();
			handler.postDelayed(this, 1000);
		}
	}

	private void updateTimer()
	{
		counter=0;
		handler.removeCallbacks(p);
		p = new UpdateTask();
		handler = new Handler();
		handler.postDelayed(p,1000);
	}

	private Handler happy_sad_Handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case STOP:

				video_player_view.setVisibility(View.GONE);
				video_player_view.stopPlayback();
				overlay_videoid.setVisibility(View.VISIBLE);
				break;
			}
			super.handleMessage(msg);
		}
	};

	private void play_think()
	{
		overlay_videoid.setVisibility(View.INVISIBLE);
		video_player_view.setVideoURI(Uri.parse("android.resource://" + getPackageName() +"/"+R.raw.thinking));
		video_player_view.start();
		video_player_view.setVisibility(View.VISIBLE);
		happy_sad_Handler.sendEmptyMessageDelayed(STOP, PLAYTIME);
	}
	private void play_happy()
	{
		overlay_videoid.setVisibility(View.INVISIBLE);
		video_player_view.setVideoURI(Uri.parse("android.resource://" + getPackageName() +"/"+R.raw.happy));
		video_player_view.start();
		video_player_view.setVisibility(View.VISIBLE);
		happy_sad_Handler.sendEmptyMessageDelayed(STOP, PLAYTIME);
	}
	private void play_sad()
	{
		overlay_videoid.setVisibility(View.INVISIBLE);
		video_player_view.setVideoURI(Uri.parse("android.resource://" + getPackageName() +"/"+R.raw.sad));
		video_player_view.start();
		video_player_view.setVisibility(View.VISIBLE);	
		happy_sad_Handler.sendEmptyMessageDelayed(STOP, PLAYTIME);
	}
	public void onPause()
	{
		super.onPause();
		if(video_player_view!=null)
			video_player_view.stopPlayback();
		if(mediaPlayer!=null){
			mediaPlayer.stop();
			mediaPlayer.release();}
		finish();
	}
	
	/*public void onBackPressed()
	{
		super.onBackPressed();
		if(video_player_view!=null)
			video_player_view.stopPlayback();
		if(mediaPlayer!=null){
			mediaPlayer.stop();
			mediaPlayer.release();}
		finish();	
	}*/

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		switch (keyCode) {
		case KeyEvent.KEYCODE_VOLUME_UP:
			audio.adjustStreamVolume(AudioManager.STREAM_MUSIC,
					AudioManager.ADJUST_RAISE, AudioManager.FLAG_SHOW_UI);
			return true;
		case KeyEvent.KEYCODE_VOLUME_DOWN:
			audio.adjustStreamVolume(AudioManager.STREAM_MUSIC,
					AudioManager.ADJUST_LOWER, AudioManager.FLAG_SHOW_UI);
			return true;
		default:
			return false;
		}
	}
	String getExactPos(String pos_val)
	{
		if(pos_val.equals("1"))
		{
			return "Noun";
		}else if(pos_val.equals("2"))
		{
			return "ProNoun";
		}if(pos_val.equals("3"))
		{
			return "Verb";
		}if(pos_val.equals("4"))
		{
			return "Adverb";
		}if(pos_val.equals("5"))
		{
			return "Adjective";
		}if(pos_val.equals("6"))
		{
			return "Preposition";
		}if(pos_val.equals("7"))
		{
			return "Conjunction";
		}else
		{
			return "Interjection";
		}
	}
}
