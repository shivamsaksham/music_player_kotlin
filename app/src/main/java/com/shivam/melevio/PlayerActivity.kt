package com.shivam.melevio

import android.media.MediaPlayer
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.shivam.melevio.databinding.ActivityPlayerBinding

class PlayerActivity : AppCompatActivity() {

    companion object{
        lateinit var musicListPA : ArrayList<Music>
        var songPosition : Int = 0
        var mediaPlayer : MediaPlayer? = null
        var isPlaying : Boolean = false
    }

    private lateinit var binding: ActivityPlayerBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.coolPink)

        binding = ActivityPlayerBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initalizeLayout()

        binding.playPauseBtnPA.setOnClickListener{
            if (isPlaying){
                pauseMusic()
            }else{
                playMusic()
            }
        }

        binding.previousBtnPA.setOnClickListener{
            prevNextSong(increment = false)
        }

        binding.nextBtnPA.setOnClickListener { prevNextSong(increment = true) }




    }

    private fun setLayout(){
        Glide.with(this)
            .load(musicListPA[songPosition].artUri)
            .apply(RequestOptions().placeholder(R.drawable.ic_guitar_splash).centerCrop())
            .into(binding.imagePV)

        binding.songNamePV.text = musicListPA[songPosition].title

    }

    private fun createMediaPlayer(){
        try {
            if (mediaPlayer == null) mediaPlayer = MediaPlayer();

            mediaPlayer!!.reset()

            mediaPlayer!!.setDataSource(musicListPA[songPosition].path)

            mediaPlayer!!.prepare()

            mediaPlayer!!.start()
            isPlaying = true
            binding.playPauseBtnPA.setIconResource(R.drawable.pause_ic)

        }catch (e: Exception){
            return
        }
    }

    private fun initalizeLayout(){
        songPosition =  intent.getIntExtra("index" , 0)

        when(intent.getStringExtra("class")){
            "MusicAdapter" -> {
                musicListPA = ArrayList()
                musicListPA.addAll(MainActivity.MusicListMA)
                setLayout()
                createMediaPlayer()

            }

            "MainActivity"-> {
                musicListPA = ArrayList()
                musicListPA.addAll(MainActivity.MusicListMA)
                musicListPA.shuffle()

                setLayout()
                createMediaPlayer()
            }
        }
    }

    private fun playMusic(){
        binding.playPauseBtnPA.setIconResource(R.drawable.pause_ic)
        isPlaying = true

        mediaPlayer!!.start()

    }
    private fun pauseMusic(){
        binding.playPauseBtnPA.setIconResource(R.drawable.play_ic)
        isPlaying = false

        mediaPlayer!!.pause()
    }

    private fun prevNextSong(increment:Boolean){
        setSongPosition(increment)
        setLayout()
        createMediaPlayer()
    }

    private fun setSongPosition(increment: Boolean){
        if (increment){
            if (musicListPA.size - 1 == songPosition){
                songPosition = 0
            }else{
                ++songPosition
            }
        }else{
            if (songPosition == 0){
                songPosition = musicListPA.size - 1
            }else{
                --songPosition
            }
        }
    }
}