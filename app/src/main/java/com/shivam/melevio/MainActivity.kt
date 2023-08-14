package com.shivam.melevio

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.shivam.melevio.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestRuntimePermission()
        setTheme(R.style.Theme_Melevio)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.shuffleBtn.setOnClickListener(){
            val intent = Intent(this@MainActivity , PlayerActivity::class.java)
            startActivity(intent)
        }

        binding.favBtn.setOnClickListener(){
            startActivity(Intent(this@MainActivity , FavouriteActivity::class.java))
        }

        binding.playListBtn.setOnClickListener(){
            startActivity(Intent(this@MainActivity , PlaylistActivity::class.java))
        }
    }

//    for requesting permission
    fun requestRuntimePermission(){
        if(ActivityCompat.checkSelfPermission(this@MainActivity , android.Manifest.permission.READ_MEDIA_AUDIO) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this@MainActivity , arrayOf(android.Manifest.permission.READ_MEDIA_AUDIO) , 13)
        }
    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == 13){
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                Toast.makeText(this , "Permission Granted" , Toast.LENGTH_LONG).show()
            else
                ActivityCompat.requestPermissions(this , arrayOf(android.Manifest.permission.READ_MEDIA_AUDIO) , 13)
        }
    }
}