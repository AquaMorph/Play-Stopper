package com.aquamorph.playstopper;

import android.app.Activity;
import android.content.Context;
import android.media.AudioManager;
import android.os.CountDownTimer;
import android.util.Log;

public class Timer extends Activity implements AudioManager.OnAudioFocusChangeListener {

	private final String TAG = "Timer";
	public CountDownTimer countDownTimer;
	public long time = 0;
	public boolean hasBeenStarted = false;
	public boolean isTimerRunning = false;
	//Notifications notifications = new Notifications();

	public void timer(Long timer, Long interval) {

		countDownTimer = new CountDownTimer(timer, interval) {

			public void onTick(long millisUntilFinished) {
				int displaySeconds = (int) (millisUntilFinished/1000)%60;
				int displayMinutes = (int) ((millisUntilFinished/(1000*60))%60);
				int displayHours = (int) ((millisUntilFinished/(1000*60*60))%24);
				time = millisUntilFinished;
				Log.i(TAG, "Time: "+millisUntilFinished);
				//notifications.timer(Timer.this, "Play Stopper", Long.toString(displayHours)+":"+Long.toString(displayMinutes)+":"+Long.toString(displaySeconds));
			}

			public void onFinish() {
				isTimerRunning = false;
				//notifications.timer(Timer.this, "Play Stopper", "00:00:00");
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

	public void pauseAudio() {
		AudioManager mAudioManager = (AudioManager) this.getSystemService(Context.AUDIO_SERVICE);
		if (mAudioManager.isMusicActive()) {
			int result = mAudioManager.requestAudioFocus(this, AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN);
			if (result == AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {
			}
		}
	}

	@Override
	public void onAudioFocusChange(int focusChange) {
	}
}