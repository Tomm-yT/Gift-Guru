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
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.training.recycler.R
import androidx.room.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

//VIEW MODEL ---------------------------------------------------------------------------------------
class CardViewModel : ViewModel() {
    private val _cardItemsRight = MutableLiveData<MutableList<CardItem>?>(mutableListOf(CardItem(text = "Default Right Card", side = "R")))
    val cardItemsRight: MutableLiveData<MutableList<CardItem>?> get() = _cardItemsRight

    private val _cardItemsLeft = MutableLiveData<MutableList<CardItem>?>(mutableListOf(CardItem(text = "Default Left Card", side = "L")))
    val cardItemsLeft: MutableLiveData<MutableList<CardItem>?> get() = _cardItemsLeft

    fun addCardRight(card: CardItem) {
        val updatedList = _cardItemsRight.value?.toMutableList()
        updatedList?.add(card)
        _cardItemsRight.postValue(updatedList)
    }

    fun addCardLeft(card: CardItem) {
        val updatedList = _cardItemsLeft.value?.toMutableList()
        updatedList?.add(card)
        _cardItemsLeft.postValue(updatedList)
    }
}

//PERSISTENCE --------------------------------------------------------------------------------------
    @Entity
    data class CardItem(
        @PrimaryKey(autoGenerate = true) val id: Int = 0,
        val text: String,
        val side: String,
    )

    @Dao
    interface CardDao {
        @Query("SELECT * FROM CardItem")
        fun getAllCards(): List<CardItem>

        @Query("DELETE FROM CardItem")
        fun deleteAllCards()

        @Insert
        fun insert(card: CardItem): Long

        @Delete
        fun delete(card: CardItem)

        @Update
        fun update(card: CardItem)
    }

    @Database(entities = [CardItem::class], version = 2)
    abstract class AppDatabase : RoomDatabase() {
        abstract fun cardDao(): CardDao
    }


class MainActivity : AppCompatActivity() {

    private lateinit var viewModel: CardViewModel

    private lateinit var db: AppDatabase

    private lateinit var recyclerViewRight: RecyclerView
    private lateinit var adapterRight: CardAdapter

    private lateinit var recyclerViewLeft: RecyclerView
    private lateinit var adapterLeft: CardAdapter

    private var cardItemsRight: MutableList<CardItem> = mutableListOf(CardItem(text = "Default Right Card", side = "R"))
    private var cardItemsLeft: MutableList<CardItem> = mutableListOf(CardItem(text ="Default Left Card", side = "L"))


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_layout)

        db = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java, "database-name"
        )
            .fallbackToDestructiveMigration()
            .allowMainThreadQueries()
            .build()
        //db.cardDao().deleteAllCards()



//USING ROOM DATABASE ------------------------------------------------------------------------------
//        CoroutineScope(Dispatchers.IO).launch {
//            val cards = db.cardDao().getAllCards()
//
//            //Load left cards
//            val leftFilteredCards = cards.filter { it.side == "L" } as MutableList<CardItem>
//            withContext(Dispatchers.Main) {
//                cardItemsLeft.addAll(leftFilteredCards)
//                adapterLeft.notifyDataSetChanged()
//            }
//
//            //Load left cards
//            val rightFilteredCards = cards.filter { it.side == "R" } as MutableList<CardItem>
//            withContext(Dispatchers.Main) {
//                cardItemsRight.addAll(rightFilteredCards)
//                adapterRight.notifyDataSetChanged()
//            }
//        }

//USING VIEW MODEL ---------------------------------------------------------------------------------
        viewModel = ViewModelProvider(this).get(CardViewModel::class.java)

        adapterRight = CardAdapter(viewModel.cardItemsRight.value ?: mutableListOf())
        adapterLeft = CardAdapter(viewModel.cardItemsLeft.value ?: mutableListOf())

        viewModel.cardItemsRight.observe(this, Observer { items ->
            items?.let { adapterRight.updateItems(it) }
        })

        viewModel.cardItemsLeft.observe(this, Observer { items ->
            items?.let { adapterLeft.updateItems(it) }
        })
//USING VIEW MODEL ^ -------------------------------------------------------------------------------



        findViewById<FloatingActionButton>(R.id.addFAB).setOnClickListener{

            val builder = AlertDialog.Builder(this)
            val inflater = layoutInflater
            val dialogView = inflater.inflate(R.layout.new_card_dialog, null)

            builder.setView(dialogView)
            builder.setTitle("Add a Card")


            var leftSideSelected = true

            //"Side-picker" switch//
            dialogView.findViewById<Switch>(R.id.sidePicker).setOnCheckedChangeListener { _, isChecked ->
                if (isChecked) {
                    leftSideSelected = false
                }
            }

            builder.setPositiveButton("Add Card") { dialog, which ->

                val editText = dialogView.findViewById<EditText>(R.id.nameEditText)
                val inputName = editText.text.toString()

                if(leftSideSelected){
                    val newCard = CardItem(text = inputName, side = "L")
                    db.cardDao().insert(newCard)
                    viewModel.addCardLeft(newCard)
                    adapterRight.notifyDataSetChanged()
                } else {
                    val newCard = CardItem(text = inputName, side = "R")
                    db.cardDao().insert(newCard)
                    viewModel.addCardRight(newCard)
                    adapterLeft.notifyDataSetChanged()
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



class CardViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    private val textView: TextView = view.findViewById(R.id.cardTextView)

    fun bind(cardItem: CardItem) {
        textView.text = cardItem.text
    }
}

class CardAdapter(private val cardItems: MutableList<CardItem> = mutableListOf()) : RecyclerView.Adapter<CardViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.card_item, parent, false)
        return CardViewHolder(view)
    }

    override fun onBindViewHolder(holder: CardViewHolder, position: Int) {
        holder.bind(cardItems[position])
    }

    override fun getItemCount() = cardItems.size

    fun updateItems(newItems: List<CardItem>) {
        this.cardItems.clear()
        this.cardItems.addAll(newItems)
        notifyDataSetChanged()
    }
}