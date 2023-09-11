package com.training.recycler.presentation

import android.os.Bundle
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.saved_list_layout)
        supportActionBar?.hide()

        adapter = CardAdapter(cardItems, viewModel)
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
            viewModel.loadSaved().forEach { it ->
                if (it.username == "RYAN") {
                    CoroutineScope(Dispatchers.Main).launch {
                        cardItems.add(it)
                        adapter.notifyDataSetChanged()
                    }
                }
            }
        }
    }
}
