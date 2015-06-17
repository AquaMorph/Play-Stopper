package com.aquamorph.playstopper;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.content.res.Configuration;
import android.graphics.Color;
import android.media.AudioManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements OnClickListener, OnSharedPreferenceChangeListener, AudioManager.OnAudioFocusChangeListener {

	private final String TAG = "MainActivity";
	private String timeText = "";
	private boolean needReset = false;
	static boolean userChoice = true;
	Timer clock = new Timer();

	//Menu Options
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case R.id.action_settings:
				Log.i(TAG, "Settings Item Clicked");
				openSettings();
				return true;
			default:
				return super.onOptionsItemSelected(item);
		}
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		loadPreferences();
		theme(this);
		setContentView(R.layout.activity_main);

		//Dial buttons
		final View dial = findViewById(R.id.dial);
		Button dialButtons[] = new Button[10];
		dialButtons[0] = (Button) dial.findViewById(R.id.number0);
		dialButtons[1] = (Button) dial.findViewById(R.id.number1);
		dialButtons[2] = (Button) dial.findViewById(R.id.number2);
		dialButtons[3] = (Button) dial.findViewById(R.id.number3);
		dialButtons[4] = (Button) dial.findViewById(R.id.number4);
		dialButtons[5] = (Button) dial.findViewById(R.id.number5);
		dialButtons[6] = (Button) dial.findViewById(R.id.number6);
		dialButtons[7] = (Button) dial.findViewById(R.id.number7);
		dialButtons[8] = (Button) dial.findViewById(R.id.number8);
		dialButtons[9] = (Button) dial.findViewById(R.id.number9);

		//Display button and text
		final View display = findViewById(R.id.display);
		final TextView timeDisplayText = (TextView) display.findViewById(R.id.timer);
		Button delete = (Button) display.findViewById(R.id.delete);

		//Play and pause button
		final View button = findViewById(R.id.buttons);
		final Button start = (Button) button.findViewById(R.id.start);
		start.setText("Start");

		//Scrolls through dial button onClickListners
		for (int i = 0; i < dialButtons.length; i++) {
			final int number = i;
			dialButtons[i].setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					addValueToString(number);
					Log.i(TAG, "Button "+number+" Clicked");
					timeDisplayText.setText(displayText());
				}
			});
		}

		//onClickListner for the display delete button
		delete.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				subtractValueToString();
				timeDisplayText.setText(displayText());
				Log.i(TAG, "Button Delete Clicked");
			}
		});

		//onClickListner for the start button
		start.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Log.i(TAG, "Button Start Clicked");
				if (clock.hasTimerFinished)
					timerPause();
				else
					timerStart();
			}
		});

		Thread updateDisplayText = new Thread() {

			@Override
			public void run() {
				try {
					while (!isInterrupted()) {
						Thread.sleep(100);
						runOnUiThread(new Runnable() {
							@Override
							public void run() {
								if (clock.isTimerRunning) {
									timeDisplayText.setText(getTimerText());
								}
								if (clock.time < 900) {
									timeDisplayText.setText(displayText());
									if (clock.hasTimerFinished) {
										pauseAudio();
										clock.resetTimerFinish();
										start.setText("Start");
									}
								}
							}
						});
					}
				} catch (InterruptedException e) {
					Log.e(TAG, "updateDisplayTextError"+e);
				}
			}
		};

		updateDisplayText.start();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	protected void onPause() {
		super.onPause();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	@Override
	protected void onResume() {
		super.onResume();
		checkForReset();
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
	}

	@Override
	protected void onStop() {
		super.onStop();
	}

	@Override
	public void onClick(DialogInterface dialog, int which) {
	}

	public void loadPreferences() {
		SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
		boolean night = settings.getBoolean("nightmode", false);
		settings.registerOnSharedPreferenceChangeListener(MainActivity.this);
		userChoice = night;
	}

	public static void theme(Activity activity) {
		if (userChoice) {
			activity.setTheme(R.style.Dark);
		}
		else {
			activity.setTheme(R.style.Light);
		}
	}

	public void checkForReset() {
		while (needReset) {
			needReset = false;
			finish();
			startActivity(getIntent());
		}
	}

	public void openSettings() {
		Intent intent = new Intent(MainActivity.this, Preference.class);
		startActivity(intent);
	}

	@Override
	public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
		loadPreferences();
		needReset = true;
	}

	//Adds and interger value at the end of the timeText string
	public void addValueToString(int value) {
		if (timeText.length() < 6) {
			timeText += value;
		}
		timeText = trimLeadingZeros(timeText);
		Log.i(TAG, "timeText: "+timeText);
		Log.i(TAG, "display: "+displayText());
	}

	//Deleted the last character in the timeText string
	public void subtractValueToString() {
		if (!timeText.equals("")) {
			timeText = timeText.substring(0, timeText.length()-1);
		}
		Log.i(TAG, "timeText: "+timeText);
	}

	//Convert timeText to display
	public String displayText() {
		return sixCharacterTime().substring(0, 2)+":"+sixCharacterTime().substring(2, 4)+":"+sixCharacterTime().substring(4, 6);
	}

	//clears leading zeros from a string
	private static String trimLeadingZeros(String source) {
		int length = source.length();
		int i;

		if (length < 2)
			return source;

		for (i = 0; i < length-1; i++) {
			char c = source.charAt(i);
			if (c != '0')
				break;
		}

		if (i == 0)
			return source;

		return source.substring(i);
	}

	public String sixCharacterTime() {
		String text = timeText;
		for (int i = 6-timeText.length(); i > 0; i--) {
			text = "0"+text;
		}
		return text;
	}

	//Returns hours of the timer
	public int getHours() {
		return Integer.parseInt(sixCharacterTime().substring(0, 2));
	}

	//Returns minutes of the timer
	public int getMinutes() {
		return Integer.parseInt(sixCharacterTime().substring(2, 4));
	}

	//Returns seconds of the timer
	public int getSeconds() {
		return Integer.parseInt(sixCharacterTime().substring(4, 6));
	}

	//Returns milliseconds of the timer
	public long getMilliseconds() {
		return getHours()*3600000+getMinutes()*60000+getSeconds()*1000;
	}

	//Returns the time of the clock in the standard format
	public String getTimerText() {
		return String.format("%02d", clock.displayHours)+":"+String.format("%02d", clock.displayMinutes)+":"+String.format("%02d", clock.displaySeconds);
	}

	public void pauseAudio() {
		AudioManager mAudioManager = (AudioManager) this.getSystemService(Context.AUDIO_SERVICE);
		if (mAudioManager.isMusicActive()) {
			mAudioManager.requestAudioFocus(this, AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN);
		}
	}

	//Handles pausing of the timer
	public void timerPause() {
		final View button = findViewById(R.id.buttons);
		final Button start = (Button) button.findViewById(R.id.start);
		//Display button and text
		final View display = findViewById(R.id.display);
		final TextView timeDisplayText = (TextView) display.findViewById(R.id.timer);
		start.setText("Start");
		timeText = String.format("%02d", clock.displayHours)+String.format("%02d", clock.displayMinutes)+String.format("%02d", clock.displaySeconds);
		timeDisplayText.setText(getTimerText());
		clock.stop();
	}

	//Handles starting of the timer
	public void timerStart() {
		clock.timer(getMilliseconds(), (long) 1000);
		clock.start();
		final View button = findViewById(R.id.buttons);
		final Button start = (Button) button.findViewById(R.id.start);
		start.setText("Pause");
	}

	@Override
	public void onAudioFocusChange(int arg0) {
	}

}