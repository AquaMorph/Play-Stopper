package com.aquamorph.playstopper;

import android.os.CountDownTimer;
import android.util.Log;

public class Timer {

	private final String TAG = "Timer";
	public CountDownTimer countDownTimer;
	public long time = 0;
	public boolean isTimerRunning = false;
	public boolean hasTimerFinished = false;
	public int displaySeconds, displayMinutes, displayHours;

	public void timer(Long timer, Long interval) {

		countDownTimer = new CountDownTimer(timer, interval) {

			public void onTick(long millisUntilFinished) {
				displaySeconds = (int) (millisUntilFinished/1000)%60;
				displayMinutes = (int) ((millisUntilFinished/(1000*60))%60);
				displayHours = (int) ((millisUntilFinished/(1000*60*60))%24);
				time = millisUntilFinished;
				Log.i(TAG, "Time: "+millisUntilFinished);
			}

			public void onFinish() {
				hasTimerFinished = true;
				isTimerRunning = false;
				time = 0;
			}
		};
	}

	//Starts the timer
	public void start() {
		hasTimerFinished = true;
		isTimerRunning = true;
		countDownTimer.start();
	}

	//Stops the timer
	public void stop() {
		hasTimerFinished = false;
		isTimerRunning = false;
		countDownTimer.cancel();
	}

	//Allows for other classes to reset the state of the timer
	public void resetTimerFinish() {
		hasTimerFinished = false;
	}
}