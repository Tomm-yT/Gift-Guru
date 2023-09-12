package com.training.recycler.presentation

import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.training.recycler.R
import com.training.recycler.domain.entities.CardItem
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SavedListActivity : AppCompatActivity() {

    private val viewModel: CardViewModel by viewModels()

    private val cardItems: MutableList<CardItem> = mutableListOf()

    private lateinit var savedRecyclerView: RecyclerView
    private lateinit var adapter: CardAdapter

    var USERNAME = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.saved_list_layout)
        supportActionBar?.hide()

        val usernameFromLogin = intent.getStringExtra("USERNAME_KEY") ?: "DefaultUsername"
        USERNAME = usernameFromLogin

        adapter = CardAdapter(USERNAME, cardItems, viewModel, true)
        savedRecyclerView = findViewById(R.id.savedRecyclerView)
        savedRecyclerView.layoutManager = GridLayoutManager(this, 2)
        savedRecyclerView.adapter = adapter

        loadSavedItems()

        findViewById<Button>(R.id.backButton).setOnClickListener {
            finish()
        }
    }


    private fun loadSavedItems() {
        CoroutineScope(Dispatchers.IO).launch {

            Log.d("", "Showing saved with $USERNAME")

            viewModel.loadSaved().forEach { it ->
                if (it.username == USERNAME) {

                    CoroutineScope(Dispatchers.Main).launch {
                        cardItems.add(it)
                        adapter.notifyDataSetChanged()

                        val viewHolder = savedRecyclerView.findViewHolderForAdapterPosition(cardItems.size - 1)
                        if (viewHolder is CardViewHolder) {
                            viewHolder.adjustSaveView()
                        }
                    }
                }
            }
        }
    }
}
