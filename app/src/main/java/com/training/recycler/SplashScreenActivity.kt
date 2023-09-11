package com.training.recycler


import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LifecycleCoroutineScope
import androidx.lifecycle.lifecycleScope
import com.training.recycler.data.ProductResponse
import com.training.recycler.domain.repositories.ProductsRepository
import com.training.recycler.presentation.CardViewModel
import com.training.recycler.presentation.LoginActivity
import com.training.recycler.presentation.MainActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SplashScreenActivity : AppCompatActivity() {

    val viewModel: CardViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.splash_screen)

        Log.d("", "SPLASH!")
        CoroutineScope(Dispatchers.IO).launch {
            viewModel.loadProductsToRepo()
        }

        val buildInfoTextView: TextView = findViewById(R.id.buildInfoTextView)
        buildInfoTextView.text = "Build Type: ${BuildConfig.BUILD_TYPE}\nFlavor: ${BuildConfig.FLAVOR}"

        // Optionally, after a delay or some conditions, move to the main activity
        Handler(Looper.getMainLooper()).postDelayed({
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }, 5000)
    }
}
