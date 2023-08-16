package com.training.recycler


import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.training.recycler.MainActivity
import com.training.recycler.R
import com.training.recycler.BuildConfig


class SplashScreenActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.splash_screen)


        val buildInfoTextView: TextView = findViewById(R.id.buildInfoTextView)
        buildInfoTextView.text = "Build Type: ${BuildConfig.BUILD_TYPE}\nFlavor: ${BuildConfig.FLAVOR}"

        // Optionally, after a delay or some conditions, move to the main activity
        Handler(Looper.getMainLooper()).postDelayed({
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }, 8000)
    }
}
