package com.example.a7laboratorinisandroid;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.widget.Switch;

public class MainActivity extends AppCompatActivity {
    Switch switch1;
    private BroadcastReceiver mReceiver;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        switch1 = findViewById(R.id.switch1);
        NotificationChannel channel = new NotificationChannel("1", "name", NotificationManager.IMPORTANCE_DEFAULT);
        channel.setDescription("aaa");
        NotificationManager notificationManager = getSystemService(NotificationManager.class);
        notificationManager.createNotificationChannel(channel);
        mReceiver = new someReceiver();
    }

    @Override
    protected void onStart() {
        registerReceiver(mReceiver, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
        super.onStart();
    }

    public class someReceiver extends BroadcastReceiver {
        private final static String BATTERY_LEVEL = "level";

        @Override
        public void onReceive(Context context, Intent intent) {
            int level = intent.getIntExtra(BATTERY_LEVEL, 0);
            if (switch1.isChecked()) {
                if (level < 25) {
                    showNotification(level);
                }
            }
        }
    }

    public void showNotification(int level) {
        Notification.Builder notif;
        int num = 1;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notif = new Notification.Builder(this, "1");
        } else {
            notif = new Notification.Builder(this);
        }
        notif.setAutoCancel(true)
                .setSmallIcon(android.R.drawable.stat_sys_download_done)
                .setContentTitle("Akumuliatoriaus lygis")
                .setContentText(String.valueOf(level));
        Intent intent = new Intent(this, MainActivity.class);
        PendingIntent pi = PendingIntent.getActivity(this,
                num, intent, PendingIntent.FLAG_CANCEL_CURRENT);
        notif.setContentIntent(pi);
        NotificationManager nm = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        nm.notify(num, notif.build());
    }

    public MainActivity getInstance() {
        return this;
    }

    @Override
    protected void onStop() {
        unregisterReceiver(mReceiver);
        super.onStop();
    }
}