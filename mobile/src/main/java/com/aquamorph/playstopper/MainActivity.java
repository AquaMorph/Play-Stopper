package com.aquamorph.playstopper;

import android.media.AudioManager;
import android.media.AudioManager.OnAudioFocusChangeListener;
import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.content.res.Configuration;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.NumberPicker;
import android.os.CountDownTimer;
import android.preference.PreferenceManager;

public class MainActivity extends Activity implements OnClickListener, OnSharedPreferenceChangeListener, OnAudioFocusChangeListener {

    private CountDownTimer countDownTimer;
    private final long interval = 1 * 1000;
    private long numberSeconds = 0;
    private long numberMinutes = 0;
    private long numberHours = 0;
    private boolean hasBeenStarted = false;
    private boolean isTimerRunning = false;
    private boolean needReset = false;
    static boolean userChoice = true;
    Notifications notifications = new Notifications();
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        loadPreferences();
        theme(this);
        setContentView(R.layout.activity_main);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.app_name);

        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()){
                    case R.id.action_settings:
                        openSettings();
                        return true;
                    default:
                        return true;
                }
            }
        });
        toolbar.inflateMenu(R.menu.main);

        final NumberPicker secondsPicker = (NumberPicker) findViewById(R.id.numberPickerSeconds);
        secondsPicker.setMaxValue(59);
        secondsPicker.setMinValue(0);
        secondsPicker.setFormatter(new NumberPicker.Formatter() {

            @Override
            public String format(int value) {
                return String.format("%02d", value);
            }
        });
        final NumberPicker minutesPicker = (NumberPicker) findViewById(R.id.numberPickerMinutes);
        minutesPicker.setMaxValue(59);
        minutesPicker.setMinValue(0);
        minutesPicker.setFormatter(new NumberPicker.Formatter() {

            @Override
            public String format(int value) {
                return String.format("%02d", value);
            }
        });
        final NumberPicker hoursPicker = (NumberPicker) findViewById(R.id.numberPickerHours);
        hoursPicker.setMaxValue(24);
        hoursPicker.setMinValue(0);

        Button start = (Button) findViewById(R.id.button2);
        Button reset = (Button) findViewById(R.id.button1);

        start.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                if (isTimerRunning == false) {
                    if(secondsPicker.getValue() != 0) {
                        numberSeconds = (secondsPicker.getValue()*1000);
                    }
                    if(minutesPicker.getValue() != 0) {
                        numberMinutes = (minutesPicker.getValue()*1000*60);
                    }
                    if(hoursPicker.getValue() != 0) {
                        numberHours = numberHours + (hoursPicker.getValue()*1000*60*60);
                    }
                    long timer = numberSeconds + numberMinutes + numberHours;

                    timer(timer, interval);

                    countDownTimer.start();
                    hasBeenStarted = true;
                    isTimerRunning = true;

                    notifications.timer(MainActivity.this,"Play Stopper","0:0:0");
                }
            }
        });

        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (hasBeenStarted == true) {
                    countDownTimer.cancel();
                    isTimerRunning = false;
                    secondsPicker.setValue(0);
                    minutesPicker.setValue(0);
                    hoursPicker.setValue(0);
                    numberSeconds = 0;
                    numberMinutes = 0;
                    numberHours = 0;
                }
            }
        });
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

    //Creates Menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {
    }

    public void loadPreferences() {
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        boolean test = settings.getBoolean("nightmode", false);
        settings.registerOnSharedPreferenceChangeListener(MainActivity.this);
        if (test == true)userChoice=true;
        else if (test == false)userChoice=false;
    }

    public static void theme(Activity activity) {
        if (userChoice==true)activity.setTheme(R.style.HoloDark);
        else if (userChoice==false)activity.setTheme(R.style.HoloLight);
    }

    public void checkForReset() {
        while (needReset==true) {
            needReset = false;
            finish();
            startActivity(getIntent());
        }
    }

    public void openSettings() {
        Intent intent = new Intent(MainActivity.this, Preference.class);
        startActivity(intent);
    }

    public void timer(Long timer, Long interval) {
        final NumberPicker secondsPicker = (NumberPicker) findViewById(R.id.numberPickerSeconds);
        final NumberPicker minutesPicker = (NumberPicker) findViewById(R.id.numberPickerMinutes);
        final NumberPicker hoursPicker = (NumberPicker) findViewById(R.id.numberPickerHours);

        countDownTimer = new CountDownTimer(timer, interval) {

            public void onTick(long millisUntilFinished) {

                int displaySeconds = (int) (millisUntilFinished / 1000) % 60 ;
                int displayMinutes = (int) ((millisUntilFinished / (1000*60)) % 60);
                int displayHours   = (int) ((millisUntilFinished / (1000*60*60)) % 24);
                secondsPicker.setValue(displaySeconds);
                minutesPicker.setValue(displayMinutes);
                hoursPicker.setValue(displayHours);
                notifications.timer(MainActivity.this,"Play Stopper",Long.toString(displayHours)+
                        ":"+Long.toString(displayMinutes)+":"+Long.toString(displaySeconds));

            }
            public void onFinish() {
                secondsPicker.setValue(0);
                minutesPicker.setValue(0);
                hoursPicker.setValue(0);
                numberSeconds = 0;
                numberMinutes = 0;
                numberHours = 0;
                isTimerRunning = false;
                userChoice = true;
                notifications.timer(MainActivity.this,"Play Stopper","0:0:0");
                pauseAudio();
            }
        };
    }

    public void pauseAudio() {
        AudioManager mAudioManager = (AudioManager) this.getSystemService(Context.AUDIO_SERVICE);
        if(mAudioManager.isMusicActive()) {
            int result = mAudioManager.requestAudioFocus(this, AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN);
            if(result == AudioManager.AUDIOFOCUS_REQUEST_GRANTED){
            }
        }
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences,String key) {
        loadPreferences();
        needReset = true;
    }

    @Override
    public void onAudioFocusChange(int arg0) {}

}