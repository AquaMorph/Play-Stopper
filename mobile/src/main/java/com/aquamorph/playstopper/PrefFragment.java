package com.aquamorph.playstopper;

import android.os.Bundle;

public class PrefFragment extends android.preference.PreferenceFragment {
	@Override
	public void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.preference);
	}
}
