package com.rama.dictionary;

import java.util.ArrayList;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

public class E2BListViewAdapter extends BaseAdapter implements Filterable {
	public static final String FONT = "SolaimanLipi.ttf";

	SharedPreferences preferences;
	private static final String myPreference = "Mypreference";

	Context context;
	ArrayList<Bean> wordLists;
	ArrayList<Bean> searchWorld;

	WordFilter valueFilter;

	public E2BListViewAdapter(Context context, ArrayList<Bean> words) {
		this.context = context;
		this.wordLists = words;
		this.searchWorld = words;
	}

	@Override
	public int getCount() {
		return wordLists.size();
	}

	@Override
	public Object getItem(int position) {
		return wordLists.get(position);
	}

	public class ViewHolder {
		TextView eng_word, bang_word;
	}

	@Override
	public long getItemId(int position) {
		return wordLists.indexOf(getItem(position));
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		ViewHolder holder;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = inflater.inflate(R.layout.e2b_list_view, null);
			holder.eng_word = (TextView) convertView
					.findViewById(R.id.view_eng);
			holder.bang_word = (TextView) convertView
					.findViewById(R.id.view_bang);
			// String str = ((TextView) convertView.findViewById(R.id.txt_eng))
			// .getText().toString();

			convertView.setTag(holder);

		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		holder.eng_word.setText(wordLists.get(position).getEngWord());
		holder.bang_word.setText(wordLists.get(position).getBangWord());
		holder.bang_word.setTypeface(Typeface.createFromAsset(
				context.getAssets(), E2BDictionaryAdapter.FONT));

		if (getFonts().equals("Small")) {
			holder.eng_word.setTextSize(15);
			holder.bang_word.setTextSize(15);
		} else if (getFonts().equals("Medium")) {
			holder.eng_word.setTextSize(18);
			holder.bang_word.setTextSize(18);
		} else if (getFonts().equals("Large")) {
			holder.eng_word.setTextSize(21);
			holder.bang_word.setTextSize(21);
		} else if (getFonts().equals("Extra Large")) {
			holder.eng_word.setTextSize(24);
			holder.bang_word.setTextSize(24);
		}

		return convertView;
	}

	@Override
	public Filter getFilter() {
		if (valueFilter == null) {
			valueFilter = new WordFilter();
		}
		return valueFilter;
	}

	private class WordFilter extends Filter {

		@Override
		protected FilterResults performFiltering(CharSequence constraint) {
			FilterResults results = new FilterResults();
			if (constraint != null && constraint.length() > 0) {
				ArrayList<Bean> filterList = new ArrayList<Bean>();
				for (int i = 0; i < searchWorld.size(); i++) {
					if ((searchWorld.get(i).getEngWord().toLowerCase())
							.contains(constraint.toString().toLowerCase())) {
						Bean words = new Bean(searchWorld.get(i).getEngWord(),
								searchWorld.get(i).getBangWord());
						filterList.add(words);
					}
				}
				results.count = filterList.size();
				results.values = filterList;
			} else {
				results.count = searchWorld.size();
				results.values = searchWorld;
			}
			return results;
		}

		@Override
		protected void publishResults(CharSequence constraint,
				FilterResults results) {
			wordLists = (ArrayList<Bean>) results.values;
			notifyDataSetChanged();
		}

	}

	public String getFonts() {
		preferences = context.getSharedPreferences(myPreference,
				Context.MODE_PRIVATE);

		String tempFonts;
		String orginalFonts = "";

		tempFonts = preferences.getString("select_fonts", "");
		if (tempFonts != null && !tempFonts.equals(""))
			orginalFonts = tempFonts;

		Log.d("Disctionary adapter......... ", orginalFonts);
		return orginalFonts;
	}
}
