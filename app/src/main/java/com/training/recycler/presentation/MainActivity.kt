package com.training.recycler.presentation

import android.graphics.Color
import android.os.Bundle
import android.util.DisplayMetrics
import android.widget.TextView
import android.view.View
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import com.training.recycler.R
import com.training.recycler.data.ProductResponse
import com.training.recycler.domain.entities.CardItem
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val viewModel: CardViewModel by viewModels()


    private lateinit var recyclerViewLeft: RecyclerView
    private lateinit var adapter: CardAdapter

    private var cardItems: MutableList<CardItem> = mutableListOf()

    private lateinit var allProducts: List<ProductResponse>
    private lateinit var mensClothingProducts: List<ProductResponse>
    private lateinit var electronicsProducts: List<ProductResponse>
    private lateinit var womensClothingProducts: List<ProductResponse>
    private lateinit var jeweleryProducts: List<ProductResponse>


    private fun clearProducts() {
        cardItems.clear()
        adapter.notifyDataSetChanged()
    }

    private fun loadAllProducts(){
        CoroutineScope(Dispatchers.Main).launch {

            val product_list = allProducts

            product_list.forEach { product ->

                val newCard = CardItem(text = product.title, imageUrl = product.image)
                viewModel.addCardLeft(newCard)

                CoroutineScope(Dispatchers.Main).launch {
                    cardItems.add(newCard)
                    adapter.notifyDataSetChanged()
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_layout)
        supportActionBar?.hide();

        //Initializes products from the repository
        allProducts = viewModel.fetchProducts()
        mensClothingProducts = viewModel.fetchProducts()
        electronicsProducts = viewModel.fetchProducts()
        womensClothingProducts = viewModel.fetchProducts()
        jeweleryProducts = viewModel.fetchProducts()

        loadAllProducts()

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

        findViewById<Button>(R.id.clear).setOnClickListener {

            clearProducts()
        }


        findViewById<Button>(R.id.allProductsButton).setOnClickListener{

            clearProducts()
            loadAllProducts()

            //Nav button colors
            findViewById<Button>(R.id.womensProductsButton).setBackgroundColor(Color.parseColor("#1978A8"))
            findViewById<Button>(R.id.allProductsButton).setBackgroundColor(Color.parseColor("#1B91CC"))
            findViewById<Button>(R.id.mensProductsButton).setBackgroundColor(Color.parseColor("#1978A8"))
        }


        findViewById<Button>(R.id.mensProductsButton).setOnClickListener{

            clearProducts()

            //Nav button colors
            findViewById<Button>(R.id.womensProductsButton).setBackgroundColor(Color.parseColor("#1978A8"))
            findViewById<Button>(R.id.allProductsButton).setBackgroundColor(Color.parseColor("#1978A8"))
            findViewById<Button>(R.id.mensProductsButton).setBackgroundColor(Color.parseColor("#1B91CC"))


            CoroutineScope(Dispatchers.Main).launch {

                //Add Mens Clothing Products
                viewModel.fetchMensCloths().forEach { it ->

                    val mensClothingProductCard = CardItem(text = it.title, imageUrl = it.image)
                    viewModel.addCardLeft(mensClothingProductCard)

                    CoroutineScope(Dispatchers.Main).launch {
                        cardItems.add(mensClothingProductCard)
                        adapter.notifyDataSetChanged()
                    }
                }

                //Add Electronics Products
                viewModel.fetchElectronics().forEach { it ->

                    val jeweleryProductCard = CardItem(text = it.title, imageUrl = it.image)
                    viewModel.addCardLeft(jeweleryProductCard)

                    CoroutineScope(Dispatchers.Main).launch {
                        cardItems.add(jeweleryProductCard)
                        adapter.notifyDataSetChanged()
                    }
                }
            }
        }


        findViewById<Button>(R.id.womensProductsButton).setOnClickListener{

            clearProducts()

            //Nav button colors
            findViewById<Button>(R.id.womensProductsButton).setBackgroundColor(Color.parseColor("#1B91CC"))
            findViewById<Button>(R.id.allProductsButton).setBackgroundColor(Color.parseColor("#1978A8"))
            findViewById<Button>(R.id.mensProductsButton).setBackgroundColor(Color.parseColor("#1978A8"))


            CoroutineScope(Dispatchers.Main).launch {

                //cardItemsLeft.removeFirst()

                //Add Womens Clothing Products
                viewModel.fetchWomensCloths().forEach { it ->

                    val womensClothingProductCard = CardItem(text = it.title, imageUrl = it.image)
                    viewModel.addCardLeft(womensClothingProductCard)

                    CoroutineScope(Dispatchers.Main).launch {
                        cardItems.add(womensClothingProductCard)
                        adapter.notifyDataSetChanged()
                    }
                }

                //Add Jewelery Products
                viewModel.fetchJeweleryProducts().forEach { it ->

                    val jeweleryProductCard = CardItem(text = it.title, imageUrl = it.image)
                    viewModel.addCardLeft(jeweleryProductCard)

                    CoroutineScope(Dispatchers.Main).launch {
                        cardItems.add(jeweleryProductCard)
                        adapter.notifyDataSetChanged()
                    }
                }
            }
        }

        adapter = CardAdapter(cardItems)

        recyclerViewLeft = findViewById(R.id.recyclerViewLeft)
        recyclerViewLeft.layoutManager = GridLayoutManager(this, 2)
        recyclerViewLeft.adapter = adapter
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

class CardAdapter(private val cardItems: MutableList<CardItem>) : RecyclerView.Adapter<CardViewHolder>() {

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