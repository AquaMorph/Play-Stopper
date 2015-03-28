package com.aquamorph.playstopper;

import android.os.CountDownTimer;
import android.util.Log;

public class Timer {

	private final String TAG = "Timer";
	public CountDownTimer countDownTimer;
	public long time = 0;
	public boolean hasBeenStarted = false;
	public boolean isTimerRunning = false;
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
				isTimerRunning = false;
				time = 0;
				//pauseAudio();
			}
		};
	}

	public void start() {
		hasBeenStarted = true;
		isTimerRunning = true;
		countDownTimer.start();
	}

	public void stop() {
		countDownTimer.cancel();
	}

	public long time() {
		return time;
	}

	public boolean isTimerRunning() {
		return isTimerRunning;
	}

	public boolean hasBeenStarted() {
		return hasBeenStarted;
	}

	public void setIsTimerRunning(boolean value) {
		isTimerRunning = value;
	}
}