package com.shivam.melevio

import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.view.MenuItem
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.shivam.melevio.databinding.ActivityMainBinding
import java.io.File
import kotlin.system.exitProcess

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var toggle: ActionBarDrawerToggle
    private lateinit var musicAdapter: MusicAdapter

    companion object{
        lateinit var MusicListMA : ArrayList<Music>
    }

    @RequiresApi(Build.VERSION_CODES.P)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.coolPinkNav)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // for navbar
        toggle = ActionBarDrawerToggle(this , binding.root , R.string.open , R.string.close)
        binding.root.addDrawerListener(toggle)
        toggle.syncState()
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        if (requestRuntimePermission()){
            initializeLayout()
        }

        binding.shuffleBtn.setOnClickListener(){
            val intent = Intent(this@MainActivity , PlayerActivity::class.java)
            intent.putExtra("index", 0)
            intent.putExtra("class" , "MainActivity")
            startActivity(intent)
        }

        binding.favBtn.setOnClickListener(){
            startActivity(Intent(this@MainActivity , FavouriteActivity::class.java))
        }

        binding.playListBtn.setOnClickListener(){
            startActivity(Intent(this@MainActivity , PlaylistActivity::class.java))
        }

        binding.navView.setNavigationItemSelectedListener(){
            when(it.itemId){
                R.id.navFeedback -> Toast.makeText(this , "FeedBack Clicked" , Toast.LENGTH_SHORT).show()
                R.id.navSettings -> Toast.makeText(this , "Settings Clicked" , Toast.LENGTH_SHORT).show()
                R.id.navAbout -> Toast.makeText(this , "About Clicked" , Toast.LENGTH_SHORT).show()
                R.id.navExit -> exitProcess(1)
            }

            true
        }
    }

//    for requesting permission
@RequiresApi(Build.VERSION_CODES.P)
fun requestRuntimePermission() : Boolean{


        if(ActivityCompat.checkSelfPermission(this@MainActivity , android.Manifest.permission.READ_MEDIA_AUDIO) != PackageManager.PERMISSION_GRANTED
            || ActivityCompat.checkSelfPermission(this@MainActivity , android.Manifest.permission.FOREGROUND_SERVICE) != PackageManager.PERMISSION_GRANTED
            ){

            ActivityCompat.requestPermissions(this@MainActivity , arrayOf(android.Manifest.permission.READ_MEDIA_AUDIO, android.Manifest.permission.FOREGROUND_SERVICE) , 13)
            return false
        }

    return true
    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == 13){
            if (grantResults.isNotEmpty() && (grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED)){
                Toast.makeText(this , "Permission Granted" , Toast.LENGTH_LONG).show()
                initializeLayout()
            }

            else
                ActivityCompat.requestPermissions(this , arrayOf(android.Manifest.permission.READ_MEDIA_AUDIO , android.Manifest.permission.FOREGROUND_SERVICE) , 13)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (toggle.onOptionsItemSelected(item)){
            return  true
        }
        return super.onOptionsItemSelected(item)
    }


    private fun initializeLayout(){
        MusicListMA = getAllAudios()

        binding.musicRV.setHasFixedSize(true)
        binding.musicRV.setItemViewCacheSize(13)
        binding.musicRV.layoutManager = LinearLayoutManager(this@MainActivity)
        musicAdapter = MusicAdapter(this@MainActivity , MusicListMA)
        binding.musicRV.adapter = musicAdapter

        binding.totalSong.text = "Total Songs : " + musicAdapter.itemCount
    }


    @SuppressLint("Range")
    private fun getAllAudios(): ArrayList<Music>{
        val tempList = ArrayList<Music>()
        val selection = MediaStore.Audio.Media.IS_MUSIC + "!= 0"
        val projection = arrayOf(
            MediaStore.Audio.Media._ID ,
            MediaStore.Audio.Media.TITLE ,
            MediaStore.Audio.Media.ALBUM,
            MediaStore.Audio.Media.ARTIST,
            MediaStore.Audio.Media.DURATION,
            MediaStore.Audio.Media.DATE_ADDED,
            MediaStore.Audio.Media.DATA,
            MediaStore.Audio.Media.ALBUM_ID
            )

        val cursor = this@MainActivity.contentResolver.query(
            MediaStore.Audio.Media.EXTERNAL_CONTENT_URI ,
            projection ,
            selection ,
            null ,
            MediaStore.Audio.Media.DATE_ADDED + " DESC" ,
            null
        )

        if (cursor != null){
            if (cursor.moveToFirst()){
                do {
                    val titleC = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.TITLE))
                    val idC = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media._ID))
                    val albumC = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM))
                    val artistC = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST))
                    val pathC = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DATA))
                    val durationC = cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Media.DURATION))
                    val albumIdC = cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM_ID)).toString()
                    val uri = Uri.parse("content://media/external/audio/albumart")
                    val artUriC = Uri.withAppendedPath(uri , albumIdC).toString()

                    val music = Music(
                        id=idC ,
                        title = titleC ,
                        album =  albumC ,
                        artist = artistC ,
                        duration = durationC ,
                        path = pathC,
                        artUri= artUriC
                    )
                    val file = File(music.path)
                    if (file.exists()){
                        tempList.add(music)
                    }

                }while (cursor.moveToNext())
            }

            cursor.close()
        }

        return tempList
    }
}