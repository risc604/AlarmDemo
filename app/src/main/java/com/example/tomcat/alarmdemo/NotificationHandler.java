package com.example.tomcat.alarmdemo;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import java.net.URI;
import java.util.Random;


//https://github.com/saulmm/Android-Notification-Example

/**
 * Created by tomcat on 2017/9/12.
 */

public class NotificationHandler
{
    private static final String TAG = NotificationHandler.class.getSimpleName();
    private static NotificationHandler  nHandler;
    private static NotificationManager  mNotificationManager;

    private NotificationHandler()   {}

    public static NotificationHandler getInstance(Context context)
    {
        if (nHandler == null)
        {
            nHandler = new NotificationHandler();
            mNotificationManager = (NotificationManager)context.getApplicationContext()
                    .getSystemService(Context.NOTIFICATION_SERVICE);
        }
        return nHandler;
    }

    public void createSimpleNotification(Context context, boolean flag)
    {
        // Creates an explicit intent for an Activity
        Intent resultIntent = new Intent(context, MainActivity.class);

        // Creating a artifical activity stack for the notification activity
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        stackBuilder.addParentStack(MainActivity.class);
        stackBuilder.addNextIntent(resultIntent);

        // Pending intent to the notification manager
        PendingIntent resultPending = stackBuilder.getPendingIntent(0,
                PendingIntent.FLAG_UPDATE_CURRENT);

        // Building the notification
        NotificationCompat.Builder  mBuilder = new NotificationCompat.Builder(context)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("I'm a simple notification")
                .setContentText("I'm the next of the simple notification")
                .setContentIntent(resultPending)
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM))
                //.setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                .setVibrate(new long[]{0, 5000, 50000, 5000});
                //.setVibrate(new long[]{1000, 1000, 1000, 1000});

        Log.d(TAG, "createSimpleNotification(), flag: " + flag);
        if (flag)
        {
            // mId allows you to update the notification later on.
            mNotificationManager.notify(10, mBuilder.build());
        }
        else
        {
            // sample.
            Notification notification = mBuilder.build();
            notification.flags |= Notification.FLAG_INSISTENT;
            mNotificationManager.notify(10, notification);
        }
    }

    public void createExpandableNotification(Context context)
    {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
        {
            // Building the expandable content
            NotificationCompat.InboxStyle inboxStyle = new NotificationCompat.InboxStyle();
            String lorem = context.getResources().getString(R.string.long_lorem);
            String[] content = lorem.split("\\.");

            inboxStyle.setBigContentTitle("This is a big title");
            for (String line : content)
            {
                inboxStyle.addLine(line);
            }

            // Building the notification
            NotificationCompat.Builder nBuilder = new NotificationCompat.Builder(context)
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setContentTitle("Expandable notification")
                    .setContentText("This is an example of an expandable notification")
                    .setStyle(inboxStyle);

            // mId allows you to update the notification later on.
            mNotificationManager.notify(11, nBuilder.build());
        }
        else
        {
            Toast.makeText(context, "Can't show", Toast.LENGTH_LONG).show();
        }
    }

    public void createProgressNotification(final Context context)
    {
        // user to update the progress notification
        final int progressID = new Random().nextInt(1000);

        //building the notification
        final NotificationCompat.Builder nBuilder = new NotificationCompat.Builder(context)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("Progress notification")
                .setContentText("Now waiting")
                .setTicker("Progress notification created")
                .setUsesChronometer(true)
                .setProgress(100, 0, true);

        @SuppressLint("StaticFieldLeak")
        AsyncTask<Integer, Integer, Integer> downloadTask = new AsyncTask<Integer, Integer, Integer>()
        {
            @Override
            protected void onPreExecute()
            {
                super.onPreExecute();
                mNotificationManager.notify(progressID, nBuilder.build());
            }

            @Override
            protected Integer doInBackground(Integer... integers)
            {
                try
                {
                    // Slepp 2 seconds to show the undeterminated progress
                    Thread.sleep(5000);

                    //update th progress
                    for (int i=0; i<101; i+=5)
                    {
                        nBuilder.setContentTitle("Progress running...")
                                .setContentText("Now running...")
                                .setProgress(100, i, false)
                                .setSmallIcon(R.mipmap.ic_launcher)
                                .setContentInfo(i + " %");
                        mNotificationManager.notify(progressID, nBuilder.build());
                        Thread.sleep(500);
                    }
                }
                catch (InterruptedException e)
                {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(Integer integer)
            {
                super.onPostExecute(integer);

                nBuilder.setContentText("Progress finished :D")
                        .setContentTitle("Progreee finished !!")
                        .setTicker("Progress finished !!!")
                        .setSmallIcon(R.mipmap.ic_launcher_round)
                        .setUsesChronometer(false);

                mNotificationManager.notify(progressID, nBuilder.build());
            }
        };

        downloadTask.execute();
    }

    @SuppressLint("ObsoleteSdkInt")
    public void createButtonNotification(Context context)
    {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN)
        {
            // Prepare intent which is triggerd if the notification button is pressed
            Intent xIntent = new Intent(context, MainActivity.class);
            PendingIntent pIntent = PendingIntent.getActivities(context, 0,
                    new Intent[]{xIntent}, 0);

            // Building the notification
            NotificationCompat.Builder nBuilder = new NotificationCompat.Builder(context)
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setContentTitle("Button notification")
                    .setContentText("Expend to show the buttons...")
                    .setTicker("showing button ontification")
                    .addAction(R.drawable.accept, "Accept", pIntent)
                    .addAction(R.drawable.cancel, "Cancel", pIntent);
            mNotificationManager.notify(1001, nBuilder.build());
        }
        else
        {
            Toast.makeText(context, "You need a higher version", Toast.LENGTH_LONG).show();
        }
    }


}

