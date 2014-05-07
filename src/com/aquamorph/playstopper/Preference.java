package com.aquamorph.playstopper;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;

public class Preference extends PreferenceActivity {
	
	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		MainActivity.theme(this);
		super.onCreate(savedInstanceState);
		
		addPreferencesFromResource(R.xml.preference);
		
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        boolean autoStart = prefs.getBoolean("checkBox1", true);
        System.out.println(autoStart);
	}
}
