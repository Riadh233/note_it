package com.example.to_do_app.data.alarms

import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import com.example.to_do_app.R
import com.example.to_do_app.di.RepositoryEntryPoint
import dagger.hilt.android.EntryPointAccessors
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext

class AlarmReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) = goAsync {
        val message = intent?.getStringExtra("EXTRA_MESSAGE") ?: return@goAsync
        val noteId = intent.getLongExtra("EXTRA_NOTE_ID",0L)
        val channelId = "alarm_id"
        context?.let { ctx ->
            val notificationManager =
                ctx.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            val hiltEntryPoint = EntryPointAccessors.fromApplication(
                ctx.applicationContext,
                RepositoryEntryPoint::class.java
            )
            val noteRepository = hiltEntryPoint.getNoteRepository()

            val builder = NotificationCompat.Builder(ctx, channelId)
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentTitle("Note It!")
                .setContentText("check you note titled $message")
                .setPriority(NotificationCompat.PRIORITY_HIGH)
            notificationManager.notify(1, builder.build())
            noteRepository.resetNoteAlarm(noteId)
        }
    }

    private fun BroadcastReceiver.goAsync(
        context: CoroutineContext = EmptyCoroutineContext,
        block: suspend CoroutineScope.() -> Unit
    ) {
        val pendingResult = goAsync()
        @OptIn(DelicateCoroutinesApi::class)
        GlobalScope.launch(context) {
            try {
                block()
            } finally {
                pendingResult.finish()
            }
        }
    }
}