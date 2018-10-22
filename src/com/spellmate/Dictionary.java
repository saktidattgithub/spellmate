package com.spellmate;

import java.util.ArrayList;
import java.util.List;

import com.spellmate.FreeDictionary.DictionaryAdapter;
import com.spellmate.utils.CsvProcessor;

import android.app.Activity;
import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
public class Dictionary extends Activity implements OnItemClickListener{

	private List<String[]> wordList;
	private ArrayList<DictionaryModel> dictionary_list;
	private DictionaryModel dictionary_obj;
	private MediaPlayer mediaPlayer;
	private EditText et_search_word;
	private ListView list;
	private boolean search_status=false;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dictionary);
		list = (ListView) findViewById(R.id.dictionary_list);
		et_search_word = (EditText) findViewById(R.id.et_search_word);
		et_search_word.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				// TODO Auto-generated method stub
				fillCustomWordList(s);
				list.setAdapter(new DictionaryAdapter());
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
				
			}
		});
		fillWordList();
		list.setAdapter(new DictionaryAdapter());
		list.setOnItemClickListener(this);
	}
	
	private void fillWordList()
	{
		
		wordList = new ArrayList<String[]>();
		dictionary_obj = new DictionaryModel();
		dictionary_list = new ArrayList<DictionaryModel>();
		wordList = new CsvProcessor().readCsv(getApplicationContext());
		for(int i=0;i<wordList.size();i++)
		{
			dictionary_obj = new DictionaryModel();
			String[] test=wordList.get(i);
			//Log.v("chack","");
			dictionary_obj.setWord(test[1]);
			dictionary_obj.setAudio(test[2]);
			dictionary_obj.setOrigin(test[5]);
			dictionary_obj.setMeaning(test[6]);
			dictionary_obj.setExample(test[7]);
			dictionary_obj.setPronounciation(test[15]);
			dictionary_obj.setPos(test[4]);
			dictionary_list.add(dictionary_obj);
		}
	}
	
	private void fillCustomWordList(CharSequence s)
	{
		
		wordList = new ArrayList<String[]>();
		dictionary_obj = new DictionaryModel();
		dictionary_list = new ArrayList<DictionaryModel>();
		wordList = new CsvProcessor().readCsv(getApplicationContext());
		for(int i=0;i<wordList.size();i++)
		{
			dictionary_obj = new DictionaryModel();
			String[] test=wordList.get(i);
			//Log.v("chack","");
			if(test[1].toString().toLowerCase().startsWith(s.toString().toLowerCase()))
			{
			dictionary_obj.setWord(test[1]);
			dictionary_obj.setAudio(test[2]);
			dictionary_obj.setOrigin(test[5]);
			dictionary_obj.setMeaning(test[6]);
			dictionary_obj.setExample(test[7]);
			dictionary_obj.setPronounciation(test[15]);
			dictionary_obj.setPos(test[4]);
			dictionary_list.add(dictionary_obj);
			}
		}
		search_status=true;
	}
	
	class DictionaryAdapter extends BaseAdapter
	{

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			if(search_status)
			return dictionary_list.size()/2;
			else
				return dictionary_list.size();	
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			View row_view = getLayoutInflater().inflate(R.layout.dictionary_rows, null);
			TextView tv_example = (TextView) row_view.findViewById(R.id.tv_example);
			TextView tv_meaning = (TextView) row_view.findViewById(R.id.tv_meaning);
			TextView tv_origin = (TextView) row_view.findViewById(R.id.tv_origin);
			TextView tv_pronounciation = (TextView) row_view.findViewById(R.id.tv_pronounciation);
			TextView tv_word = (TextView) row_view.findViewById(R.id.tv_word);
			
			tv_example.setText("Example : "+dictionary_list.get(position).getExample().replace("'","\u0027").replace("?", "\u0027"));
			tv_meaning.setText("Meaning : "+dictionary_list.get(position).getMeaning().replace("'","\u0027").replace("?", "\u0027"));
			tv_origin.setText("Origin : "+dictionary_list.get(position).getOrigin().replace("'","\u0027").replace("?", "\u0027"));
			tv_word.setText("Word : "+dictionary_list.get(position).getWord().replace("'","\u0027").replace("?", "\u0027"));
			tv_pronounciation.setText("Pos : "+getExactPos(dictionary_list.get(position).getPos()));
			
			return row_view;
		}
		
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		// TODO Auto-generated method stub
		try {
		AssetFileDescriptor afd = getAssets().openFd(dictionary_list.get(arg2).getAudio());
		mediaPlayer = new MediaPlayer();
		mediaPlayer.setDataSource(afd.getFileDescriptor(),afd.getStartOffset(),afd.getLength());
		mediaPlayer.prepare();
		mediaPlayer.start();
		}
		catch (Exception e) {	}
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
