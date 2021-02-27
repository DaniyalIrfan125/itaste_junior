package com.techbayportal.itaste.fcm

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.os.Build
import android.os.Vibrator
import android.widget.RemoteViews
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.techbayportal.itaste.MainApplication
import com.techbayportal.itaste.R
import com.techbayportal.itaste.ui.activities.mainactivity.MainActivity


class FirebaseMessageReceiver : FirebaseMessagingService() {

    override fun onNewToken(p0: String) {
        super.onNewToken(p0)
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)

//        handle when receive notification via data event
        if (remoteMessage.getData().size > 0) {
            showNotification(
                remoteMessage.data["title"],
                remoteMessage.data["message"],
                remoteMessage.data["body"]
            )
        }

        //handle when receive notification
        if (remoteMessage.getNotification() != null) {
            showNotification(
                remoteMessage.notification!!.title,
                remoteMessage.data["message"],
                remoteMessage.notification!!.body
            )
        }

    }

    fun showNotification(title: String?, message: String?, body: String?) {
        val intent = Intent(this, MainActivity::class.java)
        val channel_id = "iTasteApp"
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        intent.putExtra("data", "fromoutside")
        val pendingIntent =
            PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)
        val uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        var builder =
            NotificationCompat.Builder(applicationContext, channel_id)
                .setSmallIcon(R.mipmap.ic_launcher_itaste_round)
                .setSound(uri)
                .setAutoCancel(true) //    setVibrate(new long[]{1000, 1000, 1000, 1000, 1000})
                //  .setOnlyAlertOnce(true)
                .setContentIntent(pendingIntent)
        val vibrator = (MainApplication.context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator)
        //  mBuilder.setOngoing(true);
        builder = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            builder.setContent(getCustomDesign(title, message, body))
        } else {
            builder.setContentTitle(title)
                .setContentText(
                    """
                        $message
                        $body
                        """.trimIndent()
                )
                .setSmallIcon(R.mipmap.ic_launcher_itaste_round)
        }
        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel =
                NotificationChannel(channel_id, "iTasteApp", NotificationManager.IMPORTANCE_HIGH)
            notificationChannel.setSound(uri, null)
            notificationManager.createNotificationChannel(notificationChannel)
        }
        notificationManager.notify(0, builder.build())
    }
    private fun getCustomDesign(title:String?,message:String?,body:String?) : RemoteViews {
        val remoteViews =  RemoteViews(applicationContext.packageName, R.layout.notification);
        remoteViews.setTextViewText(R.id.title, title);
        remoteViews.setTextViewText(R.id.message, body);
        remoteViews.setTextViewText(R.id.body, message);
        remoteViews.setImageViewResource(R.id.icon, R.mipmap.ic_launcher_itaste_round);
        return remoteViews;
    }


}
