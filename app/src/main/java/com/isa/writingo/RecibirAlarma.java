package com.isa.writingo;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;

import androidx.core.content.ContextCompat;

public class RecibirAlarma extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Intent servicio = new Intent(context, ServicioNotificacion.class);
        servicio.putExtra("mensaje", intent.getStringExtra("mensaje"));
        servicio.setData((Uri.parse("custom://" + System.currentTimeMillis())));
        ContextCompat.startForegroundService(context, servicio );
        Log.d("cosa", " Alarma recbiida");
    }


}
