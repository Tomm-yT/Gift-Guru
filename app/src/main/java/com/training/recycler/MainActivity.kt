package com.training.recycler

import android.os.Bundle
import android.widget.TextView
import android.view.View
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Switch
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.training.recycler.R

class MainActivity : AppCompatActivity() {

    private lateinit var recyclerViewRight: RecyclerView
    private lateinit var adapterRight: CardAdapter

    private lateinit var recyclerViewLeft: RecyclerView
    private lateinit var adapterLeft: CardAdapter

    private var cardItemsRight: MutableList<CardItem> = mutableListOf(CardItem("Default Right Card"))
    private var cardItemsLeft: MutableList<CardItem> = mutableListOf(CardItem("Default Left Card"))


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_layout)

        findViewById<FloatingActionButton>(R.id.addFAB).setOnClickListener{

            val builder = AlertDialog.Builder(this)
            val inflater = layoutInflater
            val dialogView = inflater.inflate(R.layout.new_card_dialog, null)

            builder.setView(dialogView)
            builder.setTitle("Add a Card")



            var leftSideSelected = true

            //"Side-picker" switch/
            dialogView.findViewById<Switch>(R.id.sidePicker).setOnCheckedChangeListener { _, isChecked ->
                if (isChecked) {
                    leftSideSelected = false
                }
            }

            builder.setPositiveButton("Add Card") { dialog, which ->

                val editText = dialogView.findViewById<EditText>(R.id.nameEditText)
                val inputName = editText.text.toString()

                if(leftSideSelected){

                    cardItemsLeft.add(CardItem(inputName))
                    adapterLeft.notifyItemInserted(cardItemsLeft.size -1)
                }
                else{

                    cardItemsRight.add(CardItem(inputName))
                    adapterRight.notifyItemInserted(cardItemsRight.size -1)
                }
            }

            builder.setNegativeButton("Cancel") { dialog, which ->
                dialog.dismiss()
            }

            builder.create().show()
        }



        adapterRight = CardAdapter(cardItemsRight)

        recyclerViewRight = findViewById(R.id.recyclerViewRight)
        recyclerViewRight.layoutManager = LinearLayoutManager(this)
        recyclerViewRight.adapter = adapterRight


        adapterLeft = CardAdapter(cardItemsLeft)

        recyclerViewLeft = findViewById(R.id.recyclerViewLeft)
        recyclerViewLeft.layoutManager = LinearLayoutManager(this)
        recyclerViewLeft.adapter = adapterLeft



    }
}


data class CardItem(val text: String)

class CardViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    private val textView: TextView = view.findViewById(R.id.cardTextView)

    fun bind(cardItem: CardItem) {
        textView.text = cardItem.text
    }
}

class CardAdapter(private val cardItems: List<CardItem>) : RecyclerView.Adapter<CardViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.card_item, parent, false)
        return CardViewHolder(view)
    }

    override fun onBindViewHolder(holder: CardViewHolder, position: Int) {
        holder.bind(cardItems[position])
    }

    override fun getItemCount() = cardItems.size
}