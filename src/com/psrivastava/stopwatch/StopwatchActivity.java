package com.psrivastava.stopwatch;

import android.app.Activity;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.LinearLayout;
import android.widget.TextView;

public class StopwatchActivity extends Activity {
	/** Called when the activity is first created. */
	Chronometer mChronometer;
	Boolean paused = false;
	long elapsedTime = 0;
	int tickCount = 0;
	Button start, pause, reset;
	boolean started;
	int count = 0;
	int lapCount = 0;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		mChronometer = (Chronometer) findViewById(R.id.chronometer);

		start = (Button) findViewById(R.id.bStart);
		start.setOnClickListener(startListener);

		pause = (Button) findViewById(R.id.bPause);
		pause.setOnClickListener(pauseListener);

		reset = (Button) findViewById(R.id.bReset);
		reset.setOnClickListener(resetListener);

	}

	View.OnClickListener startListener = new OnClickListener() {
		public void onClick(View v) {
			if (paused)
				mChronometer.setBase(SystemClock.elapsedRealtime()
						- elapsedTime);
			else if (!started) {
				mChronometer.setBase(SystemClock.elapsedRealtime());

			} else if (!paused) {
				LinearLayout history = (LinearLayout) findViewById(R.id.llLaps);
				TextView lap = new TextView(getApplicationContext());
				lapCount++;
				lap.setText(lapCount
						+ "."
						+ timeFormat((SystemClock.elapsedRealtime() - mChronometer
								.getBase())));
				history.addView(lap, 0);
			}

			mChronometer.start();
			start.setText(R.string.split);

			paused = false;
			started = true;
		}

		private String timeFormat(long l) {
			int minutes;
			float seconds;
			String mins;
			String secs;

			float time = (float) l / 1000;

			minutes = (int) (time / 60);
			seconds = time % 60;

			if (minutes < 10) {
				mins = "0" + minutes;
			} else {
				mins = "" + minutes;
			}

			if (seconds < 10) {
				secs = "0" + seconds;
			} else {
				secs = "" + seconds;
			}

			return "\t\t\t" + mins + "\t:\t" + secs;
		}
	};

	View.OnClickListener pauseListener = new OnClickListener() {
		public void onClick(View v) {
			if (!paused) {
				mChronometer.stop();
				elapsedTime = SystemClock.elapsedRealtime()
						- mChronometer.getBase();
				paused = true;
				start.setText(R.string.start);
			}
		}
	};

	View.OnClickListener resetListener = new OnClickListener() {
		public void onClick(View v) {
			mChronometer.stop();
			mChronometer.setBase(SystemClock.elapsedRealtime());
			started = false;
			LinearLayout history = (LinearLayout) findViewById(R.id.llLaps);
			history.removeAllViews();
			if (!paused) {
				mChronometer.start();
				started = true;
				start.setText(R.string.split);
			} else {
				start.setText(R.string.start);
			}
			paused = false;
			lapCount = 0;
		}
	};
}