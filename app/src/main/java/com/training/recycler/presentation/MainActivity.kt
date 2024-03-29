package com.training.recycler.presentation

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.DisplayMetrics
import android.util.Log
import android.widget.TextView
import android.view.View
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.ToggleButton
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

    var USERNAME = ""
    private val viewModel: CardViewModel by viewModels()

    private var cardItems: MutableList<CardItem> = mutableListOf()

    private lateinit var recyclerViewLeft: RecyclerView
    lateinit var adapter: CardAdapter

    //Product object lists
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


            Log.d("", "Using $USERNAME")

            val product_list = allProducts

            product_list.forEach { it ->

                val newCard = CardItem(username = USERNAME, title = it.title, price = it.price, imageUrl = it.image)

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

        val usernameFromLogin = intent.getStringExtra("USERNAME_KEY") ?: "DefaultUsername"
        USERNAME = usernameFromLogin

        viewModel.setSelectedProfile(USERNAME)

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

        findViewById<Button>(R.id.saved).setOnClickListener {

            val intent = Intent(this, SavedListActivity::class.java)
            intent.putExtra("USERNAME_KEY", USERNAME)
            startActivity(intent)
        }

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

                    val mensClothingProductCard = CardItem(username = USERNAME, title = it.title, price = it.price, imageUrl = it.image)

                    CoroutineScope(Dispatchers.Main).launch {
                        cardItems.add(mensClothingProductCard)
                        adapter.notifyDataSetChanged()
                    }
                }

                //Add Electronics Products
                viewModel.fetchElectronics().forEach { it ->

                    val jeweleryProductCard = CardItem(username = USERNAME,title = it.title, price = it.price, imageUrl = it.image)

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

                //Add Womens Clothing Products
                viewModel.fetchWomensCloths().forEach { it ->

                    val womensClothingProductCard = CardItem(username = USERNAME,title = it.title, price = it.price, imageUrl = it.image)

                    CoroutineScope(Dispatchers.Main).launch {
                        cardItems.add(womensClothingProductCard)
                        adapter.notifyDataSetChanged()
                    }
                }

                //Add Jewelery Products
                viewModel.fetchJeweleryProducts().forEach { it ->

                    val jeweleryProductCard = CardItem(username = USERNAME, title = it.title, price = it.price, imageUrl = it.image)

                    CoroutineScope(Dispatchers.Main).launch {
                        cardItems.add(jeweleryProductCard)
                        adapter.notifyDataSetChanged()
                    }
                }
            }
        }

        adapter = CardAdapter(USERNAME, cardItems, viewModel)

        recyclerViewLeft = findViewById(R.id.recyclerViewLeft)
        recyclerViewLeft.layoutManager = GridLayoutManager(this, 2)
        recyclerViewLeft.adapter = adapter
    }
}




class CardViewHolder(private val username: String, view: View, private val viewModel: CardViewModel) : RecyclerView.ViewHolder(view) {
    private val titleView: TextView = view.findViewById(R.id.productTitle)
    private val priceView: TextView = view.findViewById(R.id.productPrice)
    private val imageView: ImageView = view.findViewById(R.id.productImage)
    private val saveView: ToggleButton = view.findViewById(R.id.saveProduct)

    fun adjustSaveView() {
        saveView.setBackgroundResource(R.drawable.wishlist_icon)
    }


    fun bind(cardItem: CardItem, isSavedList: Boolean = false) {
        titleView.text = cardItem.title
        priceView.text = "Price: $"+cardItem.price.toString()

        if(cardItem.imageUrl != "TODO") {
            Picasso.get()
                .load(cardItem.imageUrl)
                .into(imageView)
        }

        itemView.setOnClickListener {
            Log.d("", "You clicked: ${cardItem.title}")
            //viewModel.saveCard(cardItem)
        }

        saveView.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                Log.d("", "You saved!: ${cardItem.title}")
                saveView.setBackgroundResource(R.drawable.closed_heart_icon)
                viewModel.saveCard(cardItem)
            } else {
                saveView.setBackgroundResource(R.drawable.open_heart_icon)

                // Remove from Room database
                viewModel.deleteCard(cardItem)
            }
        }

        if(isSavedList){
            saveView.setBackgroundResource(R.drawable.closed_heart_icon)
        }

        if(isSavedList){
            saveView.setOnCheckedChangeListener { buttonView, isChecked ->
//                    saveView.setBackgroundResource(R.drawable.closed_heart_icon)
//                    viewModel.saveCard(cardItem)
                  Log.d("", "You DELETED!: ${cardItem.title}")
                  viewModel.deleteCardByName(username, cardItem)

                // Remove from the RecyclerView
                (itemView.parent as? RecyclerView)?.adapter?.let {
                    if (it is CardAdapter) {
                        it.removeItem(cardItem)
                    }
                }
            }
        }

    }
}

class CardAdapter(
    private val username: String,
    private val items: List<CardItem>,
    private val viewModel: CardViewModel,
    private val isSavedList: Boolean = false) : RecyclerView.Adapter<CardViewHolder>() {

//    fun removeAt(position: Int) {
//        cardItems.removeAt(position)
//        notifyItemRemoved(position)
//    }

    fun removeItem(cardItem: CardItem) {
        val index = items.indexOf(cardItem)
        if (index != -1) {
            (items as MutableList<CardItem>).removeAt(index)
            notifyItemRemoved(index)
        }
    }

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

        return CardViewHolder(username, view, viewModel)
    }

    override fun onBindViewHolder(holder: CardViewHolder, position: Int) {
        holder.bind(items[position], isSavedList)
    }

    override fun getItemCount() = items.size
}