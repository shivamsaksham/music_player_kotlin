package com.shivam.melevio

import android.app.Service
import android.content.Intent
import android.graphics.BitmapFactory
import android.media.MediaPlayer
import android.os.Binder
import android.os.IBinder
import android.support.v4.media.session.MediaSessionCompat
import androidx.core.app.NotificationCompat


class MusicService: Service() {

    private var myBinder = MyBinder()
    var mediaPlayer: MediaPlayer? = null
    private lateinit var mediaSession : MediaSessionCompat

    override fun onBind(p0: Intent?): IBinder {
        mediaSession = MediaSessionCompat(baseContext , "Melevio Music")
        return myBinder
    }

    inner class MyBinder : Binder(){
        fun currentService(): MusicService{
            return this@MusicService
        }
    }

    fun showNotification(){
        val notification = NotificationCompat.Builder(baseContext , ApplicationClass.CHANNEL_ID)
            .setContentTitle(PlayerActivity.musicListPA[PlayerActivity.songPosition].title)
            .setContentText(PlayerActivity.musicListPA[PlayerActivity.songPosition].artist)
            .setSmallIcon(R.drawable.play_ic)
            .setLargeIcon(BitmapFactory.decodeResource(resources , R.drawable.ic_guitar_splash))
            .setStyle(androidx.media.app.NotificationCompat.MediaStyle().setMediaSession(mediaSession.sessionToken))
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
            .setOnlyAlertOnce(true)
            .addAction(R.drawable.previous_ic , "Previous" , null)
            .addAction(R.drawable.pause_ic , "Pause" , null)
            .addAction(R.drawable.next_ic , "Next", null)
            .addAction(R.drawable.exit_ic , "Exit" , null)
            .build()

        startForeground(13 , notification)
    }
}












