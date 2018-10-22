package com.spellmate.utils;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.res.AssetManager;

import com.spellmate.CSVReader;

public class CsvProcessor {

	ArrayList<String[]> questionList;

	public final List<String[]> bgn_lv_one(Context context) {
		questionList = new ArrayList<String[]>();
		AssetManager assetManager = context.getAssets();

		try {
			InputStream csvStream = assetManager.open("repo/1.csv");
			InputStreamReader csvStreamReader = new InputStreamReader(csvStream,"windows-1252");
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
	public final List<String[]> bgn_lv_two(Context context) {
		questionList = new ArrayList<String[]>();
		AssetManager assetManager = context.getAssets();

		try {
			InputStream csvStream = assetManager.open("repo/2.csv");
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
	public final List<String[]> bgn_lv_three(Context context) {
		questionList = new ArrayList<String[]>();
		AssetManager assetManager = context.getAssets();

		try {
			InputStream csvStream = assetManager.open("repo/3.csv");
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
	public final List<String[]> jnr_lv_one(Context context) {
		questionList = new ArrayList<String[]>();
		AssetManager assetManager = context.getAssets();

		try {
			InputStream csvStream = assetManager.open("repo/4.csv");
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
	public final List<String[]> jnr_lv_two(Context context) {
		questionList = new ArrayList<String[]>();
		AssetManager assetManager = context.getAssets();

		try {
			InputStream csvStream = assetManager.open("repo/5.csv");
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
	public final List<String[]> jnr_lv_three(Context context) {
		questionList = new ArrayList<String[]>();
		AssetManager assetManager = context.getAssets();

		try {
			InputStream csvStream = assetManager.open("repo/6.csv");
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
	public final List<String[]> adv_lv_one(Context context) {
		questionList = new ArrayList<String[]>();
		AssetManager assetManager = context.getAssets();

		try {
			InputStream csvStream = assetManager.open("repo/7.csv");
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
	public final List<String[]> adv_lv_two(Context context) {
		questionList = new ArrayList<String[]>();
		AssetManager assetManager = context.getAssets();

		try {
			InputStream csvStream = assetManager.open("repo/8.csv");
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
	public final List<String[]> adv_lv_three(Context context) {
		questionList = new ArrayList<String[]>();
		AssetManager assetManager = context.getAssets();

		try {
			InputStream csvStream = assetManager.open("repo/9.csv");
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
	public final List<String[]> adv_lv_four(Context context) {
		questionList = new ArrayList<String[]>();
		AssetManager assetManager = context.getAssets();

		try {
			InputStream csvStream = assetManager.open("repo/10.csv");
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

	public final List<String[]> readCsv(Context context) {
		questionList = new ArrayList<String[]>();
		AssetManager assetManager = context.getAssets();

		try {
			InputStream csvStream = assetManager.open("repo/WordRepository.csv");
			InputStreamReader csvStreamReader = new InputStreamReader(csvStream,"windows-1252");
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

}
