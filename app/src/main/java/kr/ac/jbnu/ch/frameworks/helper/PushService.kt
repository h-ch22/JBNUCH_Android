package kr.ac.jbnu.ch.frameworks.helper

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import kr.ac.jbnu.ch.R
import kr.ac.jbnu.ch.frameworks.view.MainActivity
import kr.ac.jbnu.ch.frameworks.view.StartActivity

class PushService : FirebaseMessagingService() {
    override fun onNewToken(p0: String) {
        super.onNewToken(p0)
    }

    override fun onMessageReceived(p0: RemoteMessage) {
        super.onMessageReceived(p0)

        if(p0.data.isNotEmpty()){
            val title = p0.data["title"].toString()
            val message = p0.data["content"].toString()

            Log.d("PushService", p0.data.toString())

            for((key, value) in p0.data){
                Log.d("PushService", "Key : ${key}, Value : ${value}")
            }



            val dataValue = p0.data.get("message")

            val valueMap = p0.data.values

            valueMap.forEach {
                Log.d("PushService", "In Value Map : ${it}")
            }

            Log.d("PushService", "title : ${p0.data.get("title").toString()}, body : ${p0.data.get("content").toString()}")

            sendNotification(message, title)
        }

        else{
            p0.notification?.let{
                sendNotification(p0.notification!!.body.toString(),
                                p0.notification!!.title.toString())
            }
        }

    }

    private fun sendNotification(message : String?, title : String?){
        val notifyID = (System.currentTimeMillis() / 7).toInt()

        val intent = Intent(this, StartActivity :: class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)

        val pendingIntent = PendingIntent.getActivity(this, notifyID, intent, PendingIntent.FLAG_IMMUTABLE)

        val channelID = "10"

        val notificationBuilder = NotificationCompat.Builder(this, channelID)
            .setSmallIcon(R.drawable.appstore)
            .setContentTitle(title)
            .setContentText(message)
            .setPriority(NotificationManagerCompat.IMPORTANCE_HIGH)
            .setAutoCancel(true)
            .setDefaults(Notification.DEFAULT_VIBRATE)
            .setDefaults(Notification.DEFAULT_SOUND)
            .setContentIntent(pendingIntent)

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val channel = NotificationChannel(
            channelID,
            channelID,
            NotificationManager.IMPORTANCE_HIGH
        )

        notificationManager.createNotificationChannel(channel)

        notificationManager.notify(notifyID, notificationBuilder.build())
    }
}