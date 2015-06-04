package com.rama.dictionary.activity;

import java.util.ArrayList;
import java.util.Locale;

import android.app.Activity;
import android.app.ActivityOptions;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.rama.dictionary.B2EDictionaryAdapter;
import com.rama.dictionary.B2EListViewAdapter;
import com.rama.dictionary.Bean;
import com.rama.dictionary.BookMarksActivity;
import com.rama.dictionary.BookMarksDBHelper;
import com.rama.dictionary.DictionaryDBOpenHelper;
import com.rama.dictionary.E2BDictionaryAdapter;
import com.rama.dictionary.E2BListViewAdapter;
import com.rama.dictionary.R;

public class MainActivity extends Activity {
	DictionaryDBOpenHelper dbHelper;
	BookMarksDBHelper bookMarksDBHelper;
	public static final String FONT = "SolaimanLipi.ttf";

	// declare to change selector
	private boolean isSelect = false;
	private boolean isDictionary = false;
	private boolean isSelectDicionary = false;
	private boolean isChangeOption = false;
	private boolean isChackBookmarks = false;
	String[] engwords;
	String[] bangwords;

	// set dictionary type
	String e2bString = "English to Bangla";
	String b2eString = "Bangla to English";

	TextView etDictonary;
	EditText etSearch;
	ListView DictionaryListView;

	// initialize adapter

	E2BDictionaryAdapter e2bAdapter;
	B2EDictionaryAdapter b2eAdapter;

	// initialize array list

	ArrayList<Bean> wordList;

	// initialize list view adapter

	E2BListViewAdapter e2bListView;
	B2EListViewAdapter b2eListView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

		setContentView(R.layout.activity_main);
		etSearch = (EditText) findViewById(R.id.etSearch);
		dbHelper = DictionaryDBOpenHelper.getInstance(getApplicationContext());
		etDictonary = (TextView) findViewById(R.id.setDictionary);
		DictionaryListView = (ListView) findViewById(R.id.dictionaryList);
		bookMarksDBHelper = BookMarksDBHelper
				.getDbHelperInstance(getApplicationContext());
		wordList = dbHelper.getAllwords();
		loadEnglishToBangla();
		DictionaryListView
				.setOnItemLongClickListener(new OnItemLongClickListener() {

					@Override
					public boolean onItemLongClick(AdapterView<?> adapter,
							View v, int position, long id) {
						String word_eng = wordList.get(position).getEngWord();
						String word_bang = wordList.get(position).getBangWord();
						Bean b = new Bean(word_eng, word_bang);
						openAlert();
						if (isChackBookmarks) {
							bookMarksDBHelper.insertBookMarks(b);
						}

						return true;
					}
				});

	}

	// set alert bookmarks
	
	public void openAlert() {
		AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);

		builder.setTitle("Add Bookmarks");
		builder.setMessage("do you add bookmarks...");

		builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				isChackBookmarks = true;

			}
		});

		builder.setNegativeButton("No", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.cancel();
				isChackBookmarks = false;
			}
		});

		AlertDialog alertDialog = builder.create();

		alertDialog.show();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {

		case R.id.banglaView:
			isDictionary = true;
			if (isChangeOption) {
				loadB2EListView();
			} else {
				loadBanglaToEnglish();
			}
			return true;
		case R.id.englishView:
			isDictionary = false;
			if (isChangeOption) {
				loadE2BListView();
			} else {
				loadEnglishToBangla();
			}
			return true;

		case R.id.fullView:
			isSelect = true;
			isChangeOption = true;
			if (isSelectDicionary) {
				loadE2BListView();
			} else {
				loadB2EListView();
			}
			return true;
		case R.id.Normal:
			isSelect = false;
			isChangeOption = false;
			if (isSelectDicionary) {
				loadEnglishToBangla();
			} else {
				loadBanglaToEnglish();
			}
			return true;

		case R.id.exit:
			finish();
			break;
		case R.id.bookmarks:

			Intent intent = new Intent(MainActivity.this,
					BookMarksActivity.class);
			Bundle banAnimation = ActivityOptions.makeCustomAnimation(
					getApplicationContext(), R.anim.animation_next,
					R.anim.animaton_pre).toBundle();
			startActivity(intent, banAnimation);

		}
		return true;
	}

	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		super.onPrepareOptionsMenu(menu);
		menu.findItem(R.id.fullView).setVisible(!isSelect);
		menu.findItem(R.id.Normal).setVisible(isSelect);
		menu.findItem(R.id.banglaView).setVisible(!isDictionary);
		menu.findItem(R.id.englishView).setVisible(isDictionary);
		return true;
	}

	// set E2B

	public void loadEnglishToBangla() {
		isSelectDicionary = true;
		isSelect = false;
		etDictonary.setText(e2bString);
		e2bAdapter = new E2BDictionaryAdapter(getApplicationContext(), wordList);
		DictionaryListView.setAdapter(e2bAdapter);
		DictionaryListView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
		etSearch.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				// TODO Auto-generated method stub

			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub

			}

			@Override
			public void afterTextChanged(Editable s) {
				String text = etSearch.getText().toString()
						.toLowerCase(Locale.getDefault());
				e2bAdapter.filter(text);
			}
		});

	}

	// set bookmarks addapter

	// public void booksMarks() {
	//
	// etDictonary.setText("");
	// e2bAdapter = new E2BDictionaryAdapter(getApplicationContext(),
	// bookmarksAdapter);
	// DictionaryListView.setAdapter(e2bAdapter);
	// }

	// set List View

	public void loadE2BListView() {
		etDictonary.setText(e2bString);
		e2bListView = new E2BListViewAdapter(getApplicationContext(), wordList);
		DictionaryListView.setAdapter(e2bListView);
		DictionaryListView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
		etSearch.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				// TODO Auto-generated method stub

			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub

			}

			@Override
			public void afterTextChanged(Editable s) {
				String text = etSearch.getText().toString()
						.toLowerCase(Locale.getDefault());
				e2bListView.filter(text);
			}
		});

	}

	// set b2e List View adapter

	public void loadB2EListView() {
		etDictonary.setText(b2eString);
		b2eListView = new B2EListViewAdapter(getApplicationContext(), wordList);
		DictionaryListView.setAdapter(b2eListView);
		DictionaryListView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
		etSearch.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				// TODO Auto-generated method stub

			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub

			}

			@Override
			public void afterTextChanged(Editable s) {
				String text = etSearch.getText().toString()
						.toLowerCase(Locale.getDefault());
				b2eListView.filter(text);
			}
		});

	}

	// set B2E

	public void loadBanglaToEnglish() {
		isSelectDicionary = false;
		isSelect = false;
		etDictonary.setText(b2eString);
		b2eAdapter = new B2EDictionaryAdapter(getApplicationContext(), wordList);
		DictionaryListView.setAdapter(b2eAdapter);
		DictionaryListView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
		etSearch.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				// TODO Auto-generated method stub

			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub

			}

			@Override
			public void afterTextChanged(Editable s) {
				String text = etSearch.getText().toString()
						.toLowerCase(Locale.getDefault());
				b2eAdapter.filter(text);
			}
		});

	}
//
//	@Override
//	public void onBackPressed() {
//		super.onBackPressed();
//		if(isChackBookmarks){
//			finish();
//		}
//	}

}
