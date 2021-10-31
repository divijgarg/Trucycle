package com.example.recycleapplicationv3;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;

import androidx.annotation.ColorInt;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

public class NotificationBroadcast extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {

        NotificationManager mNotificationManager;

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context, "notifyRecycle");

        PendingIntent pendingIntent2 = PendingIntent.getActivity(context, 0, intent, 0);
        mBuilder.setContentIntent(pendingIntent2);
        mBuilder.setSmallIcon(R.drawable.logo);
        mBuilder.setContentTitle("Recycle App");
        mBuilder.setContentText("Time to take out the trash!");
        mBuilder.setPriority(Notification.PRIORITY_MAX);
        mBuilder.setAutoCancel(true);


        @ColorInt int color = Color.parseColor("#61E49E");
        mBuilder.setColor(color);
        mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(0, mBuilder.build());

        AlarmManager alarmManager= (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        long targetTime= System.currentTimeMillis()+604800000;
        alarmManager.set(AlarmManager.RTC_WAKEUP,targetTime, pendingIntent2);

    }
}

