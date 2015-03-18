package com.aquamorph.playstopper;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.content.res.Configuration;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends ActionBarActivity implements OnClickListener, OnSharedPreferenceChangeListener {

	private final String TAG = "MainActivity";
	private String timeText = "";
	private boolean needReset = false;
	static boolean userChoice = true;
	Notifications notifications = new Notifications();
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

		final View display = findViewById(R.id.display);
		final TextView timeDisplayText = (TextView) display.findViewById(R.id.timer);
		Button delete = (Button) display.findViewById(R.id.delete);

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
		boolean test = settings.getBoolean("nightmode", false);
		settings.registerOnSharedPreferenceChangeListener(MainActivity.this);
		if (test == true)
			userChoice = true;
		else if (test == false)
			userChoice = false;
	}

	public static void theme(Activity activity) {
		if (userChoice == true)
			activity.setTheme(R.style.Dark);
		else if (userChoice == false)
			activity.setTheme(R.style.Light);
	}

	public void checkForReset() {
		while (needReset == true) {
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
		String text = timeText;
		for (int i = 6-timeText.length(); i > 0; i--) {
			text = "0"+text;
		}
		return text.substring(0, 2)+":"+text.substring(2, 4)+":"+text.substring(4, 6);
	}
}
