package com.psrivastava.stopwatch;

import java.util.ArrayList;

import android.app.Activity;
import android.app.ListFragment;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ListView;

public class StopwatchActivity extends Activity {
	/** Called when the activity is first created. */
	private static final String TAG = "StopwatchActivity";

	Chronometer mChronometer;
	Boolean mChronoPaused = false;
	long mElapsedTime = 0;
	ImageButton mStartButton, mPauseButton, mStopButton;
	ArrayList<String> mSplitTimes = new ArrayList<String>();
	LapViewFragment splitTimesFragment;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		mChronometer = (Chronometer) findViewById(R.id.chronometer);

		mStartButton = (ImageButton) findViewById(R.id.bStart);
		mStartButton.setOnClickListener(startListener);

		mPauseButton = (ImageButton) findViewById(R.id.bPause);
		mPauseButton.setOnClickListener(pauseListener);

		mStopButton = (ImageButton) findViewById(R.id.bStop);
		mStopButton.setOnClickListener(stopListener);

		if (getFragmentManager().findFragmentById(R.id.flLapView) == null) {
			splitTimesFragment = new LapViewFragment();
			getFragmentManager().beginTransaction()
					.add(R.id.flLapView, splitTimesFragment).commit();
		}

	}

	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		super.onRestoreInstanceState(savedInstanceState);

		if (savedInstanceState.getBoolean("running")) {
			Log.i("StopwatchActivity", "base changed");

			mChronometer.setBase(savedInstanceState.getLong("base"));
			mChronometer.start();
		}
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		outState.putBoolean("running", mChronometer.isRunning());
		outState.putLong("base", mChronometer.getBase());

		super.onSaveInstanceState(outState);
	}

	View.OnClickListener startListener = new OnClickListener() {
		public void onClick(View v) {

			if (mChronoPaused) {
				// chronometer was paused, now resume
				mPauseButton.setVisibility(View.VISIBLE);
				mStopButton.setVisibility(View.VISIBLE);
				Log.v(TAG, "start-chrono was paused");
				mChronometer.setBase(SystemClock.elapsedRealtime()
						- mElapsedTime);
				getWindow().addFlags(
						WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
			} else if (!mChronometer.isStarted()) {
				// chronometer was stopped, restart

				mPauseButton.setVisibility(View.VISIBLE);
				mStopButton.setVisibility(View.VISIBLE);
				((FrameLayout) findViewById(R.id.flLapView)).removeAllViews();

				splitTimesFragment = new LapViewFragment();
				getFragmentManager().beginTransaction()
						.add(R.id.flLapView, splitTimesFragment).commit();
				Log.v(TAG, "start-chrono was stopped");
				mChronometer.setBase(SystemClock.elapsedRealtime());
				getWindow().addFlags(
						WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
			} else if (!mChronoPaused) {
				// chronometer, is running so split into new lap
				Log.v(TAG, "split button pressed");

				mSplitTimes
						.add(0,
								timeFormat((SystemClock.elapsedRealtime() - mChronometer
										.getBase())));
				Log.i("split button listener, first elemtn", mSplitTimes.get(0));

				splitTimesFragment.refresh();

			}

			mChronometer.start();
			mStartButton.setImageResource(R.drawable.split);
			mChronoPaused = false;
		}

		private String timeFormat(long l) {
			int minutes;
			float seconds;
			int milliseconds;
			String mins;
			String secs;

			float time = (float) l / 1000;

			minutes = (int) (time / 60);
			seconds = (time % 60);
			milliseconds = (int) (((int) l % 1000) / 100);

			if (minutes < 10) {
				mins = "0" + minutes;
			} else {
				mins = "" + minutes;
			}

			if (seconds < 10) {
				secs = "0" + (int) seconds;
			} else {
				secs = "" + (int) seconds;
			}

			return "\t\t\t" + mins + ":" + secs + "." + milliseconds;
		}
	};

	View.OnClickListener pauseListener = new OnClickListener() {
		public void onClick(View v) {
			if (!mChronoPaused) {
				Log.v(TAG, "pause");
				mPauseButton.setVisibility(View.GONE);
				mChronometer.stop();
				mElapsedTime = SystemClock.elapsedRealtime()
						- mChronometer.getBase();
				mChronoPaused = true;
				mStartButton.setImageResource(R.drawable.start);
				getWindow().clearFlags(
						WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
			}
		}
	};

	View.OnClickListener stopListener = new OnClickListener() {
		public void onClick(View v) {
			Log.v(TAG, "stop");
			mStartButton.setVisibility(View.VISIBLE);
			mPauseButton.setVisibility(View.GONE);
			mStopButton.setVisibility(View.GONE);
			mChronometer.stop();
			mChronometer.setBase(SystemClock.elapsedRealtime());
			mSplitTimes = new ArrayList<String>();

			mStartButton.setImageResource(R.drawable.start);
			mChronoPaused = false;
			getWindow().clearFlags(
					WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		}
	};

	public class LapViewFragment extends ListFragment {

		LapViewAdapter mLapViewAdapter;

		@Override
		public void onActivityCreated(Bundle savedInstanceState) {
			super.onActivityCreated(savedInstanceState);
			mLapViewAdapter = new LapViewAdapter(getActivity(),
					R.layout.list_lap, mSplitTimes);
			setListAdapter(mLapViewAdapter);
		}

		public void refresh() {
			Log.i("LapViewFragment", "trigger refresh");
			mLapViewAdapter.notifyDataSetChanged();
		}

		@Override
		public void onListItemClick(ListView l, View v, int position, long id) {
			Log.i("FragmentList", "Item clicked: " + id);
		}
	}

}