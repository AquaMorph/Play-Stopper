package com.aquamorph.playstopper;

import android.app.Activity;
import android.os.CountDownTimer;

public class Timer extends Activity {

    public static CharSequence convertTimeToString(CountDownTimer countDownTimer, long millisUntilFinished) {
        String time, minuteZero, secondZero;
        Long displaySeconds = (long) (millisUntilFinished / 1000) % 60 ;
        Long displayMinutes = (long) ((millisUntilFinished / (1000*60)) % 60);
        Long displayHours   = (long) ((millisUntilFinished / (1000*60*60)) % 24);
        String seconds = String.valueOf(displaySeconds);
        String minutes = String.valueOf(displayMinutes);
        if (seconds.length()<=1 ) {
            secondZero = "0";
        } else secondZero = "";
        if (minutes.length()<=1 ) {
            minuteZero = "0";
        } else minuteZero = "";
        time = (displayHours + ":" + minuteZero + displayMinutes + ":" + secondZero + displaySeconds);
        return time;
    }



}