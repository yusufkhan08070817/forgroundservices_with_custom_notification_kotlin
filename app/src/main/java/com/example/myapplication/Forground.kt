package com.example.myapplication

import android.annotation.SuppressLint
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import android.widget.RemoteViews
import android.widget.Toast
import androidx.core.app.NotificationCompat


class Forground:Service() {
    val notification_channel="Insta"
    val service_id=404
    lateinit var notification: Notification
    lateinit var nortificationManager:NotificationManager
    val handler:Handler= Handler(Looper.getMainLooper())
    var count=0
    var requstcount=0
    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onCreate() {
        super.onCreate()
        createchannel()
        createnotification()
    }

    @SuppressLint("ForegroundServiceType")
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val toastrunnable=object :Runnable{
            override fun run() {
                Toast.makeText(this@Forground, "$count", Toast.LENGTH_SHORT).show()
                count++
                handler.postDelayed(this,1000)
            }

        }
        handler.postDelayed(toastrunnable,1000)
        startForeground(service_id,notification)
        return START_STICKY
    }
    fun createchannel(){
        if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.O)
        {
            val channel=NotificationChannel(notification_channel,"Forground",NotificationManager.IMPORTANCE_HIGH)
            nortificationManager=getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            nortificationManager.createNotificationChannel(channel)

        }
    }
    @SuppressLint("RemoteViewLayout")
    fun  createnotification(){
        val remoteview=RemoteViews(packageName,R.layout.noti)
        remoteview.setTextViewText(R.id.text1,"title")
        remoteview.setTextViewText(R.id.text2,"sub")
        remoteview.setOnClickPendingIntent(R.id.btn1,getintent("button1"))
        remoteview.setOnClickPendingIntent(R.id.btn2,getintent("button2"))
        remoteview.setOnClickPendingIntent(R.id.btn3,getintent("button3"))
        val build=NotificationCompat.Builder(this,notification_channel)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContent(remoteview)
        notification=build.build()


    }
    fun getintent(msg:Any):PendingIntent{
        val intent=Intent(this,buttonrecevier::class.java).apply {
            action="BUUTTENACCTION"
            putExtra("msg","$msg")
        }
        requstcount++
  return  PendingIntent.getBroadcast(this,requstcount,intent,PendingIntent.FLAG_UPDATE_CURRENT)
    }
}