package com1032.cw1.ld00245.mobilecomputingassignment2;

/**
 * Created by Punlife on 21/05/2016.
 */

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.BitmapDrawable;
import android.os.Handler;
import android.os.IBinder;
import android.widget.CursorAdapter;
import android.widget.Toast;

import java.util.Timer;
import java.util.TimerTask;

import android.support.v4.app.NotificationCompat;

public class SummonerRefreshService extends Service {
    public static final long REFRESH_INTERVAL = 10 * (60 * 1000); // 10 minutes
    private DBController summonerDatabase;
    private Cursor summoner = null;
    private CursorAdapter adapter;
    private SQLiteDatabase db;
    private String username = null;
    private Handler handler = new Handler();
    private Timer timer = null;

    @Override
    public IBinder onBind(Intent intent) {
        username = intent.getStringExtra("name");
        return null;
    }

    public void onStart(Intent intent, int startID) {
        super.onStart(intent, startID);
        username = intent.getStringExtra("name");
    }

    @Override
    public void onCreate() {
        if (timer != null) {
            timer.cancel();
        } else {
            timer = new Timer();
        }
        timer.schedule(new TimeDisplayTimerTask(), REFRESH_INTERVAL);
    }

    class TimeDisplayTimerTask extends TimerTask {
        @Override
        public void run() {
            handler.post(new Runnable() {

                @Override
                public void run() {
                    Toast.makeText(getApplicationContext(), "You can now refresh Summoner " + username, Toast.LENGTH_SHORT).show();
                    SQLiteDatabase.CursorFactory factory = null;
                    summonerDatabase = new DBController(getApplicationContext(), username, factory);
                    summonerDatabase.dropTable();


                    NotificationCompat.Builder builder =
                            new NotificationCompat.Builder(getApplicationContext())
                                    .setLargeIcon(((BitmapDrawable) getApplicationContext().getResources().getDrawable(R.mipmap.ic_launcher)).getBitmap())
                                    .setSmallIcon(R.drawable.logo1)
                                    .setContentTitle("Rifter")
                                    .setContentText("Summoner " + username + " can now be refreshed.")
                                    .setColor(getResources().getColor(R.color.DarkTheme_accent));

                    int NOTIFICATION_ID = 7890;

                    Intent targetIntent = new Intent(getApplicationContext(), HomeScreen.class);
                    PendingIntent contentIntent = PendingIntent.getActivity(getApplicationContext(), 0, targetIntent, PendingIntent.FLAG_UPDATE_CURRENT);
                    builder.setContentIntent(contentIntent);
                    NotificationManager nManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                    nManager.notify(NOTIFICATION_ID, builder.build());
                }

            });
        }

    }
}
