package com.shivam.melevio

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.shivam.melevio.databinding.ActivityMainBinding
import kotlin.system.exitProcess

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var toggle: ActionBarDrawerToggle

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestRuntimePermission()
        setTheme(R.style.Theme_Melevio)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // for navbar
        toggle = ActionBarDrawerToggle(this , binding.root , R.string.open , R.string.close)
        binding.root.addDrawerListener(toggle)
        toggle.syncState()
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

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

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (toggle.onOptionsItemSelected(item)){
            return  true
        }
        return super.onOptionsItemSelected(item)
    }
}