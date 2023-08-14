package com.shivam.melevio

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.shivam.melevio.databinding.ActivityPlayerBinding

class PlayerActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPlayerBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.Theme_Melevio)

        binding = ActivityPlayerBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}