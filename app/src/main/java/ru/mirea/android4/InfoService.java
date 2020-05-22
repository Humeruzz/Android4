package ru.mirea.android4;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.IBinder;

import androidx.annotation.Nullable;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.concurrent.TimeUnit;

public class InfoService extends Service {
    InfoRepository repository = InfoRepository.getInstance();
    String channelId = "channel_id";
    NotificationManager manager;
    Thread thread = new Thread(){
        @Override
        public void run() {
            Date date = repository.getDate();
            try {

                final long timetoNotify = date.getTime() - Calendar.getInstance().getTime().getTime();
                TimeUnit.MILLISECONDS.sleep(timetoNotify);
                Notification.Builder builder = new Notification.Builder(InfoService.this, channelId)
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setContentTitle("Notification")
                        .setContentText("Notification!");
                Notification notification = builder.build();
                manager.notify(1, notification);
            } catch (InterruptedException e) {
                return;
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
    };
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        System.out.println("Run");
        thread.start();
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onCreate() {
        manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        String name = "chnl";
        int importance = NotificationManager.IMPORTANCE_HIGH;
        NotificationChannel channel = new NotificationChannel(channelId, name, importance);
        channel.setDescription("Notifications for service");
        channel.enableLights(true);
        channel.setLightColor(Color.BLUE);
        channel.enableVibration(true);
        channel.setShowBadge(true);
        manager.createNotificationChannel(channel);
        Notification.Builder builder = new Notification.Builder(InfoService.this, channelId)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("Notification")
                .setContentText("Service started");
        Notification notification = builder.build();
        super.onCreate();
        startForeground(1, notification);
    }

    @Override
    public void onDestroy() {
        thread.interrupt();
        super.onDestroy();
    }
}
