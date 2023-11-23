package com.example.agendasmart;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import java.util.Date;

public class TaskReminder {

    public static void scheduleNotification(Context context, String title, String message, String taskDate, long repeatIntervalMillis) {
        try {
            // Calcula el tiempo restante hasta la fecha de finalización de la tarea
            long timeDiffMillis = DateUtils.calculateTimeDiff(taskDate);

            Log.d("TaskReminder", "Tiempo restante hasta la tarea: " + timeDiffMillis);

            if (timeDiffMillis > 0) {
                // Ajusta el tiempo restante para notificar 15 horas antes
                long notificationTimeMillis = timeDiffMillis - (14 * 60 * 60 * 1000); // Resta 15 horas

                Intent notificationIntent = new Intent(context, NotificationReceiver.class);
                notificationIntent.putExtra("title", title);
                notificationIntent.putExtra("message", message);

                PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

                long futureMillis = System.currentTimeMillis() + notificationTimeMillis;

                Log.d("TaskReminder", "Programando notificación para: " + new Date(futureMillis));

                AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

                // Configura la repetición con un intervalo específico
                alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, futureMillis, repeatIntervalMillis, pendingIntent);
            } else {
                // La fecha de la tarea está en el pasado, maneja este caso si es necesario
                Log.d("TaskReminder", "La fecha de la tarea está en el pasado");
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("TaskReminder", "Excepción al programar notificación: " + e.getMessage());
        }
    }
}
