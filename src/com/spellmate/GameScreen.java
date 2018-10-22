package com.spellmate;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.spellmate.utils.CsvProcessor;

import android.app.Activity;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.VideoView;



public class GameScreen extends Activity implements OnClickListener{
	VideoView video_player_view;
	DisplayMetrics dm;
	SurfaceView sur_View;
	MediaController media_Controller;
	List<String[]> wordList, tenWordSet;MediaPlayer mediaPlayer;
	private String type,level;

	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.gamescreen);
		/*((Button)findViewById(R.id.beginid)).setOnClickListener(this);
		((Button)findViewById(R.id.juniorid)).setOnClickListener(this);*/
		((ImageView)findViewById(R.id.homeid)).setOnClickListener(this);
		((ImageView)findViewById(R.id.backid)).setOnClickListener(this);
		type=getIntent().getStringExtra("type");level=getIntent().getStringExtra("level");


		/*video_player_view=(VideoView)findViewById(R.id.videoid);
		video_player_view.setVideoPath("file:///android_asset/thinking.mp4");
		video_player_view.setVideoURI(Uri.parse("android.resource://" + getPackageName() +"/"+R.raw.thinking));
		video_player_view.start();*/
	}

	public void onResume()
	{
		super.onResume();

		fillWordList();
		//prepareMedia();
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		/*case R.id.beginid:
			startActivity(new Intent(getApplicationContext(), Beginner.class));
			break;
		case R.id.juniorid:

			break;
		case R.id.advanceid:

			break;*/
		case R.id.backid:
			finish();
			break;
		case R.id.homeid:
			startActivity(new Intent(getApplicationContext(), MenuScreen.class));
			break;

		default:
			break;
		}

	}

	private void fillWordList()
	{
		Thread t=new Thread(){
			public void run()
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
				}
				/*else if(type.equals("junior"))
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
				}*/


				tenWordSet = new ArrayList<String[]>();
				Random r = new Random();
				for(int i=0;i<10;i++)
				{
					int random=r.nextInt(wordList.size());
					tenWordSet.add(wordList.get(random));
				}
				handler.sendEmptyMessage(0);
			}
		};t.start();

	}

	private Handler handler=new Handler()
	{
		public void handleMessage(Message msg)
		{

			//populateTenWordSet();
			Random r = new Random();
			int random=r.nextInt(10);
			//String[] test=tenWordSet.get(random);
			for(int i=0;i<tenWordSet.size();i++){
				String[] test=tenWordSet.get(i);
				String data=test[2];
				try {
					AssetFileDescriptor afd = getAssets().openFd(data);
					//if(mediaPlayer==null)
					//{
					mediaPlayer = new MediaPlayer();
					mediaPlayer.setDataSource(afd.getFileDescriptor(),afd.getStartOffset(),afd.getLength());
					mediaPlayer.prepare();
					mediaPlayer.start();
					Thread.sleep(5000);
					//}
					/*else
				{
					mediaPlayer.stop();
					mediaPlayer.release();
					mediaPlayer=null;
				}*/
				}
				catch (IllegalArgumentException e) {    }
				catch (IllegalStateException e) { }
				catch (IOException e) { } catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			mediaPlayer.stop();
			mediaPlayer.release();
			mediaPlayer=null;

		}
	};

	private void prepareMedia()
	{
		//populateTenWordSet();
		Random r = new Random();
		int random=r.nextInt(10);
		//String[] test=tenWordSet.get(random);
		for(int i=0;i<tenWordSet.size();i++){
			String[] test=tenWordSet.get(i);
			String data=test[2];
			try {
				AssetFileDescriptor afd = getAssets().openFd(data);
				//if(mediaPlayer==null)
				//{
				mediaPlayer = new MediaPlayer();
				mediaPlayer.setDataSource(afd.getFileDescriptor(),afd.getStartOffset(),afd.getLength());
				mediaPlayer.prepare();
				mediaPlayer.start();
				Thread.sleep(5000);
				//}
				/*else
			{
				mediaPlayer.stop();
				mediaPlayer.release();
				mediaPlayer=null;
			}*/
			}
			catch (IllegalArgumentException e) {    }
			catch (IllegalStateException e) { }
			catch (IOException e) { } catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		mediaPlayer.stop();
		mediaPlayer.release();
		mediaPlayer=null;
	}

	private void populateTenWordSet()
	{
		tenWordSet = new ArrayList<String[]>();
		Random r = new Random();
		for(int i=0;i<10;i++)
		{
			int random=r.nextInt(wordList.size());
			tenWordSet.add(wordList.get(random));
		}
	}
}
