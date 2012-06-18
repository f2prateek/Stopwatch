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
	int mLayoutResourceId;

	public LapViewAdapter(Context context, int resourceID,
			ArrayList<String> values) {
		super(context, resourceID, values);
		mContext = context;
		mLayoutResourceId = resourceID;
		mValues = values;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = (LayoutInflater) mContext
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		View rowView = inflater.inflate(mLayoutResourceId, parent, false);

		TextView lapNumber = (TextView) rowView.findViewById(R.id.tvLapNumber);
		lapNumber.setText(mContext.getString(R.string.lap) + " "
				+ Integer.toString(mValues.size() - position));
		TextView splitTime = (TextView) rowView.findViewById(R.id.tvSplitTime);
		splitTime.setText(mValues.get(position));

		return rowView;
	}

}