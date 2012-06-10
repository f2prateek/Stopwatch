package com.psrivastava.stopwatch;

import android.app.Activity;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

public class StopwatchActivity extends Activity {
	/** Called when the activity is first created. */
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
			if (mChronoPaused)
				mChronometer.setBase(SystemClock.elapsedRealtime()
						- mElapsedTime);
			else if (!mChronometer.isStarted()) {
				mChronometer.setBase(SystemClock.elapsedRealtime());

			} else if (!mChronoPaused) {
				LinearLayout history = (LinearLayout) findViewById(R.id.llLaps);
				TextView lap = new TextView(getApplicationContext());
				mLapCount++;
				lap.setText(mLapCount
						+ "."
						+ timeFormat((SystemClock.elapsedRealtime() - mChronometer
								.getBase())));
				history.addView(lap, 0);
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
				mChronometer.stop();
				mElapsedTime = SystemClock.elapsedRealtime()
						- mChronometer.getBase();
				mChronoPaused = true;
				mStartButton.setImageResource(R.drawable.start);
			}
		}
	};

	View.OnClickListener stopListener = new OnClickListener() {
		public void onClick(View v) {
			mChronometer.stop();
			mChronometer.setBase(SystemClock.elapsedRealtime());
			LinearLayout history = (LinearLayout) findViewById(R.id.llLaps);
			history.removeAllViews();
			mStartButton.setImageResource(R.drawable.start);

			mChronoPaused = false;
			mLapCount = 0;
		}
	};

}