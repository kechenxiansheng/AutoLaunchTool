package com.cm.launchtool.utils

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Build
import androidx.core.app.NotificationCompat
import com.cm.launchtool.ui.MainActivity
import com.cm.launchtool.R

object NotificationUtil {
    /** 通知  */
    fun sendNotification(
        context: Context,
        channelId: String?,
        title: String?,
        contentText: String?,
        notificationId: Int
    ) {
        val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val builder = if (Build.VERSION.SDK_INT >= 26) {
            val channel = NotificationChannel(channelId, channelId, NotificationManager.IMPORTANCE_HIGH)
            manager.createNotificationChannel(channel)
            //api 大于26 必须加上channelId
            Notification.Builder(context, channelId)
        } else {
            Notification.Builder(context)
        }
        builder.setContentTitle(title)
        builder.setContentText(contentText)
        builder.setWhen(System.currentTimeMillis())
        builder.setSmallIcon(R.mipmap.app_icon)
        builder.setLargeIcon(BitmapFactory.decodeResource(context.resources, R.mipmap.app_icon))
        builder.setAutoCancel(true)
        builder.setDefaults(NotificationCompat.DEFAULT_ALL)     //通知使用系统默认配置（音效，震动，呼吸灯等）
        builder.setPriority(Notification.PRIORITY_MAX)          //重要级别（共5个。此处设置了最大）
        //设置点击跳转
        val intent = Intent(context, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK        //相当于将activity设置为 singleTask
        val hangPendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT)
        builder.setContentIntent(hangPendingIntent) //点击跳转
        /** 大于21，使用悬挂式通知  */
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            builder.setFullScreenIntent(hangPendingIntent, true)
//        }
        manager.notify(notificationId, builder.build())
    }

    /**
     * 创建通知
     */
    fun createNotification(
        context: Context,
        channelId: String?,
        channelName: String?,
        title: String?,
        contentText: String?,
    ):Notification {
        //设置点击跳转
        val intent = Intent(context, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK        //相当于将activity设置为 singleTask
        /** 针对后台服务在android10以上无法启动activity问题，官方建议可使用fullScreenIntent */
        val fullScreenIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)

        val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val builder = if (Build.VERSION.SDK_INT >= 26) {
            val channel = NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_HIGH)
            manager.createNotificationChannel(channel)
            //api 大于26 必须加上channelId
            Notification.Builder(context, channelId)
        } else {
            Notification.Builder(context)
        }
        builder.setContentTitle(title)
        builder.setContentText(contentText)
        builder.setWhen(System.currentTimeMillis())
        builder.setSmallIcon(R.mipmap.app_icon)
        builder.setLargeIcon(BitmapFactory.decodeResource(context.resources, R.mipmap.app_icon))
        builder.setAutoCancel(true)
        builder.setDefaults(NotificationCompat.DEFAULT_ALL)     //通知使用系统默认配置（音效，震动，呼吸灯等）
        //关键的三个设置
        builder.setCategory(NotificationCompat.CATEGORY_CALL)
        builder.setPriority(Notification.PRIORITY_HIGH)          //重要级别（共5个）
        builder.setFullScreenIntent(fullScreenIntent,true) //点击跳转
//        manager.notify(1,builder.build())
       return builder.build()
    }

}