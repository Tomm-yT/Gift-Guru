package com.training.recycler.presentation

import android.os.Bundle
import android.util.DisplayMetrics
import android.util.Log
import android.widget.TextView
import android.view.View
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Switch
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.squareup.picasso.Picasso
import com.training.recycler.R
import com.training.recycler.domain.entities.CardItem
import com.training.recycler.domain.usecases.GetProducts
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import android.view.ViewGroup.LayoutParams




@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val viewModel: CardViewModel by viewModels()


    private lateinit var recyclerViewLeft: RecyclerView
    private lateinit var adapterLeft: CardAdapter

    private var cardItemsLeft: MutableList<CardItem> = mutableListOf(CardItem(text ="", side = "L", imageUrl = "TODO"))


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_layout)

        viewModel.clearAllCards()

        //Load card history
//        CoroutineScope(Dispatchers.Main).launch {
//            // Load left cards
//            cardItemsLeft.addAll(viewModel.getLeftCards())
//            adapterLeft.notifyDataSetChanged()
//
//            // Load right cards
//            cardItemsRight.addAll(viewModel.getRightCards())
//            adapterRight.notifyDataSetChanged()
//        }



        findViewById<Button>(R.id.apiButton).setOnClickListener{
            populateWithProducts()
        }



        adapterLeft = CardAdapter(cardItemsLeft)

        recyclerViewLeft = findViewById(R.id.recyclerViewLeft)
        recyclerViewLeft.layoutManager = GridLayoutManager(this, 2)
        recyclerViewLeft.adapter = adapterLeft
    }


    fun populateWithProducts(){
        CoroutineScope(Dispatchers.Main).launch {
            cardItemsLeft.removeFirst()
            var product_list = viewModel.fetchProducts()
            //Log.d("API RESULT: ", product.image)

            product_list.forEach { product ->

                val newCard = CardItem(text = product.title, side = "L", imageUrl = product.image)
                viewModel.addCardLeft(newCard)

                CoroutineScope(Dispatchers.Main).launch {
                    cardItemsLeft.add(newCard)
                    adapterLeft.notifyDataSetChanged()
                }
            }
        }
    }

}




class CardViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    private val textView: TextView = view.findViewById(R.id.cardTextView)
    private val imageView: ImageView = view.findViewById(R.id.productImage)

    fun bind(cardItem: CardItem) {
        textView.text = cardItem.text

        if(cardItem.imageUrl != "TODO") {
            Picasso.get()
                .load(cardItem.imageUrl)
                .into(imageView)
        }
    }


}

class CardAdapter(private val cardItems: MutableList<CardItem> = mutableListOf()) : RecyclerView.Adapter<CardViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.card_item, parent, false)

        // Get the existing layout params for the view
        val existingParams = view.layoutParams as ViewGroup.MarginLayoutParams // Cast to MarginLayoutParams

        // Get the device screen metrics
        val displayMetrics = DisplayMetrics()
        (parent.context as AppCompatActivity).windowManager.defaultDisplay.getMetrics(displayMetrics)

        // Calculate the width and margin based on the screen size
        val screenWidth = displayMetrics.widthPixels
        val width = screenWidth / 2.15
        val margin = (screenWidth * 0.01).toInt()  // Margin as 1% of screen width, you can adjust as needed

        // Modify the width and margins of the existing layout params
        existingParams.width = width.toInt()
        existingParams.setMargins(margin, margin, margin, margin)

        // Set the modified layout params back to the view
        view.layoutParams = existingParams

        return CardViewHolder(view)
    }




    override fun onBindViewHolder(holder: CardViewHolder, position: Int) {
        holder.bind(cardItems[position])
    }

    override fun getItemCount() = cardItems.size
}