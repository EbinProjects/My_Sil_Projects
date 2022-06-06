package com.example.firstkotlin.adapter
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat.startActivity
import com.example.firstkotlin.R
import com.example.firstkotlin.activities.HomeActivity
import com.example.firstkotlin.activities.MainActivity

import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class FireBAseService : FirebaseMessagingService() {
    val TAG = "Service"
    override fun onMessageReceived(message: RemoteMessage) {
        Log.d(TAG, "From:------ " + message!!.from)
        Log.d(TAG, "Notification Message Body:------ " + message.notification!!.body!!)
        sendNotification(message)
        val intent = Intent(this@FireBAseService, HomeActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        intent.putExtra("message", message.notification!!.body!!)
        startActivity(intent)
    }
    private fun sendNotification(remoteMessage: RemoteMessage) {
        val intent = Intent(this, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        val pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
            PendingIntent.FLAG_ONE_SHOT)
        val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val notificationBuilder = NotificationCompat.Builder(this)
            .setContentText(remoteMessage.notification!!.body)
            .setAutoCancel(true)
            .setSmallIcon(R.mipmap.s)
            .setSound(defaultSoundUri)
            .setContentIntent(pendingIntent)
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build())
    }
}