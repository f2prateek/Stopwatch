package com.psrivastava.stopwatch;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class LapViewAdapter extends ArrayAdapter<String> {

	private final Context mContext;
	private final ArrayList<String> mValues;

	public LapViewAdapter(Context context, ArrayList<String> values) {
		super(context, R.layout.list_lap);
		mContext = context;
		mValues = values;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = (LayoutInflater) mContext
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		View rowView = inflater.inflate(R.layout.list_lap, parent, false);

		TextView lapNumber = (TextView) rowView.findViewById(R.id.tvLapNumber);
		lapNumber.setText(mContext.getString(R.string.lap) + " "
				+ Integer.toString(position));
		TextView splitTime = (TextView) rowView.findViewById(R.id.tvSplitTime);
		splitTime.setText(mValues.get(position));

		return rowView;
	}

}
