package com.psrivastava.stopwatch;

import android.app.Activity;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class StopwatchActivity extends Activity {
	/** Called when the activity is first created. */
	private static final String TAG = "StopwatchActivity";

	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onRestoreInstanceState(savedInstanceState);

		if (savedInstanceState.getBoolean("running")) {
			// TODO: DEBUG FLAG
			Toast.makeText(this, "base changed", Toast.LENGTH_SHORT);
			mChronometer.setBase(savedInstanceState.getLong("base"));
			mChronometer.start();
		}
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		// TODO Auto-generated method stub
		outState.putBoolean("running", mChronometer.isRunning());
		outState.putLong("base", mChronometer.getBase());

		super.onSaveInstanceState(outState);
	}

	Chronometer mChronometer;
	Boolean mChronoPaused = false;
	long mElapsedTime = 0;
	ImageButton mStartButton, mPauseButton, mStopButton;
	int mLapCount = 0;

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
				mPauseButton.setVisibility(View.VISIBLE);
				mStopButton.setVisibility(View.VISIBLE);
				// chronometer was stopped, restart
				Log.v(TAG, "start-chrono was stopped");
				mChronometer.setBase(SystemClock.elapsedRealtime());
				getWindow().addFlags(
						WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
			} else if (!mChronoPaused) {
				// chronometer, is running so split into new lap
				Log.v(TAG, "split button pressed");
				LinearLayout history = (LinearLayout) findViewById(R.id.llLaps);
				TextView lapNumber = new TextView(getApplicationContext());
				TextView splitTimer = new TextView(getApplicationContext());
				mLapCount++;
				lapNumber.setText(mLapCount + ".");
				splitTimer
						.setText(timeFormat((SystemClock.elapsedRealtime() - mChronometer
								.getBase())));
				
				//TODO:make linear layout iwht lapNumber and splitTiemr
				//TODO: make listview
				
				history.addView(splitTimer, 0);
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
			LinearLayout history = (LinearLayout) findViewById(R.id.llLaps);
			history.removeAllViews();
			mStartButton.setImageResource(R.drawable.start);
			mChronoPaused = false;
			mLapCount = 0;
			getWindow().clearFlags(
					WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		}
	};

}