package com.world4tech.godsdecision

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.world4tech.godsdecision.databinding.ActivitySplashBinding

class SplashActivity : AppCompatActivity() {
    private lateinit var binding:ActivitySplashBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.versioin.animate().setDuration(1500).alpha(1f).withEndAction{
            val i = Intent(this,MainActivity::class.java)
            startActivity(i)
            overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out)
            finish()
        }
    }
}