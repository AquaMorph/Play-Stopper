package com.aquamorph.playstopper;

import android.app.Activity;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;

public class Notifications {

    public void timer(Activity activity,String title,String text) {
        int notificationId = 001;
        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(activity)
                        .setSmallIcon(R.drawable.ic_launcher)
                        .setContentTitle(title)
                        .setContentText(text);
        NotificationManagerCompat notificationManager =
                NotificationManagerCompat.from(activity);
        notificationManager.notify(notificationId, notificationBuilder.build());
    }
}
