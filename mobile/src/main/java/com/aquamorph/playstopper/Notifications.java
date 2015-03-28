package com.aquamorph.playstopper;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;

public class Notifications {

	public void timer(Activity activity, String title, String text) {
		int notificationId = 001;
		Intent mapIntent = new Intent(Intent.ACTION_VIEW);
		PendingIntent mapPendingIntent = PendingIntent.getActivity(activity, 0, mapIntent, 0);
		NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(activity).setSmallIcon(R.drawable.ic_launcher).setContentTitle(title).setContentText(text).addAction(R.drawable.ic_pause_white_48dp, "Pause", mapPendingIntent);
		NotificationManagerCompat notificationManager = NotificationManagerCompat.from(activity);
		notificationManager.notify(notificationId, notificationBuilder.build());
	}
}
