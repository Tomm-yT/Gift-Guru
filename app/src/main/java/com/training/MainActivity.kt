package com.training

import android.os.Bundle
import android.widget.Spinner
import android.widget.ArrayAdapter
import android.widget.TextView
import android.widget.LinearLayout
import android.widget.Toast
import android.widget.Button
import android.widget.AdapterView
import android.util.Log
import android.view.View
import android.view.Gravity
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_layout)

        findViewById<Button>(R.id.button).setOnClickListener{

            findViewById<TextView>(R.id.textView).text = "After Button Press"
        }
    }
}
