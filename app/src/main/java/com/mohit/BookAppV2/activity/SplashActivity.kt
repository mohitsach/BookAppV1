package com.mohit.BookAppV2.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.mohit.BookAppV2.R

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        Handler().postDelayed({
            val startAct= Intent(this@SplashActivity,
                LoginActivity::class.java)
            startActivity(startAct)
            finish()
        }, 1000)
    }
}
