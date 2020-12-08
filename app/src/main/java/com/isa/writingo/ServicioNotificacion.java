package com.isa.writingo;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

public class ServicioNotificacion extends IntentService {
    //
    private NotificationManager notMan;
    private PendingIntent penIntent;
    private static int IdNotificacion = 1;
    Notification notificacion;
    //


    /**
     * @param name
     * @deprecated
     */
    public ServicioNotificacion(String name) {
        super(name);
    }
    public ServicioNotificacion() {
        super("SERVICE");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        String NOTIFICATION_CHANNEL_ID = getApplicationContext().getString(R.string.app_name);
        Context context = this.getApplicationContext();
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        Intent mIntent = new Intent(this, MainActivity.class);
        Resources res = this.getResources();
        Uri soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);

        String message = intent.getStringExtra("mensaje");

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            final int NOTIFY_ID = 0; // ID of notification
            String id = NOTIFICATION_CHANNEL_ID; // default_channel_id
            String title = NOTIFICATION_CHANNEL_ID; // Default Channel
            PendingIntent pendingIntent;
            NotificationCompat.Builder builder;
            NotificationManager notifManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            if (notifManager == null) {
                notifManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            }
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel mChannel = notifManager.getNotificationChannel(id);
            if (mChannel == null) {
                mChannel = new NotificationChannel(id, title, importance);
                mChannel.enableVibration(false);
                notifManager.createNotificationChannel(mChannel);
            }
            builder = new NotificationCompat.Builder(context, id);
            mIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            pendingIntent = PendingIntent.getActivity(context, 0, mIntent, PendingIntent.FLAG_UPDATE_CURRENT);
            builder.setContentTitle(getString(R.string.app_name)).setCategory(Notification.CATEGORY_SERVICE)
                    .setSmallIcon(R.drawable.calendar1)   // required
                    .setContentText(message)
                    .setLargeIcon(BitmapFactory.decodeResource(res, R.drawable.calendar1))
                    .setDefaults(Notification.DEFAULT_ALL)
                    .setAutoCancel(true)
                    .setSound(soundUri)
                    .setContentIntent(pendingIntent);
            Notification notification = builder.build();
            notifManager.notify(NOTIFY_ID, notification);

            startForeground(1, notification);

        } else {
            penIntent = PendingIntent.getActivity(context, 1, mIntent, PendingIntent.FLAG_UPDATE_CURRENT);
            notificacion = new NotificationCompat.Builder(this)
                    .setContentIntent(penIntent)
                    .setSmallIcon(R.drawable.calendar1)
                    .setLargeIcon(BitmapFactory.decodeResource(res, R.drawable.calendar1))
                    .setSound(soundUri)
                    .setAutoCancel(true)
                    .setContentTitle(getString(R.string.app_name)).setCategory(Notification.CATEGORY_SERVICE)
                    .setContentText(message).build();
            notificationManager.notify(IdNotificacion, notificacion);
        }
    }
}
