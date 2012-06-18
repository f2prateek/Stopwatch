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
	private final ArrayList<String> mValuesLapSplit;
	int mLayoutResourceId;

	public LapViewAdapter(Context context, int resourceID,
			ArrayList<String> valuesLapSplit) {
		super(context, resourceID, valuesLapSplit);
		mContext = context;
		mLayoutResourceId = resourceID;
		mValuesLapSplit = valuesLapSplit;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = (LayoutInflater) mContext
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		View rowView = inflater.inflate(mLayoutResourceId, parent, false);

		TextView lapNumber = (TextView) rowView.findViewById(R.id.tvLapNumber);
		TextView lapSplitTime = (TextView) rowView
				.findViewById(R.id.tvSplitTimeLap);

		lapNumber.setText(mContext.getString(R.string.lap) + " "
				+ Integer.toString(mValuesLapSplit.size() - position));
		lapSplitTime.setText(mValuesLapSplit.get(position));

		return rowView;
	}

}