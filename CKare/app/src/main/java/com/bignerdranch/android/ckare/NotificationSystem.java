package com.bignerdranch.android.ckare;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.support.v4.app.NotificationCompat;

import java.util.Random;

public class NotificationSystem {
    private static final String CHANNEL_ID = "channel_id";
    private static final String CHANNEL_NAME = "CKare";
    private static final String CHANNEL_DESC = "channel_desc";
    private static final String notificationTitle = "DELETION";
    private final int randomiserMAXVAL = 100000;
    private Random random = new Random();
    private int randomisedVal = randomiser(random);

    // creating a notification channel
    private void createNotificationChannel(Context context) {
        NotificationChannel channel = new NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT);
        channel.setDescription(CHANNEL_DESC);

        // register the channel with the system
        NotificationManager notificationManager = (NotificationManager) context.getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.createNotificationChannel(channel);
    }

    public void addNotification(Context context, String objectClass, String objectName) {
        createNotificationChannel(context);

        // setup the notification contents
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_main_logo)
                .setContentTitle(notificationTitle)
                .setContentText(objectClass + " - " + objectName + " got deleted")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setAutoCancel(true);

        // build the notification
        NotificationManager managerCompat = (NotificationManager) context.getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
        managerCompat.notify(randomisedVal, builder.build());
    }

    // randomise the build id to be unique
    // notification with same build id would result in ONLY 1 notification if an action (delete) is performed multiple times
    public int randomiser(Random random) {
        // range formula - (max - min + 1) + min
        int randomized = random.nextInt(randomiserMAXVAL - 0 + 1) + 0;
        return randomized;
    }

}
