package com.world4tech.godsdecision

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import com.world4tech.godsdecision.databinding.ActivityMainBinding
import com.world4tech.godsdecision.fragments.HomeFragment

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        val homeFrag = HomeFragment()
        makeCurrentFragment(homeFrag)
    }

    private fun makeCurrentFragment(fragment: Fragment)  = supportFragmentManager.beginTransaction().apply {
        replace(R.id.fr_wrapper,fragment)
        commit()
    }
}