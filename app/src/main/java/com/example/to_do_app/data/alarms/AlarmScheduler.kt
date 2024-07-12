package com.example.to_do_app.data.alarms

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.util.Log

class AlarmScheduler(
    private val context: Context
) {
    private val alarmManager = context.getSystemService(AlarmManager::class.java)

    @SuppressLint("ScheduleExactAlarm")
    fun schedule(noteId : Long,alarmTime: Long, message : String) {
        Log.d("note id from schedule method",noteId.toString())
        val intent = Intent(context, AlarmReceiver::class.java).apply {
            putExtra("EXTRA_MESSAGE", message)
            putExtra("EXTRA_NOTE_ID", noteId)
        }
        alarmManager.setExactAndAllowWhileIdle(
            AlarmManager.RTC_WAKEUP,
            alarmTime,
            PendingIntent.getBroadcast(
                context,
                noteId.toInt(),
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )
        )
    }
    fun cancel(noteId : Long) {
        alarmManager.cancel(
            PendingIntent.getBroadcast(
                context,
                noteId.toInt(),
                Intent(context, AlarmReceiver::class.java),
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )
        )
    }
}