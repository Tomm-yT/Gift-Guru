package com.training.recycler.presentation

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.training.recycler.R
import com.training.recycler.data.UserProfileDao
import com.training.recycler.domain.entities.UserProfile
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {

    @Inject
    lateinit var userProfileDao: UserProfileDao

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login_screen)
        supportActionBar?.hide();



        val userProfiles = mutableListOf<UserProfile>()
        val profileAdapter = ProfileAdapter(userProfiles)

        val profilesRecyclerView: RecyclerView = findViewById(R.id.profilesRecyclerView)
        profilesRecyclerView.layoutManager = GridLayoutManager(this, 2)  // 2 columns in grid
        profilesRecyclerView.adapter = profileAdapter






        // When you add a new profile:
//        userProfiles.add(UserProfile(username = "NewUser", icon = ""))
//        profileAdapter.notifyDataSetChanged()

        fun loadProfilesOnStartup() {
            CoroutineScope(Dispatchers.IO).launch {
                val profilesFromDb = userProfileDao.getAllProfiles()

                withContext(Dispatchers.Main) {
                    userProfiles.clear()
                    userProfiles.addAll(profilesFromDb)
                    profileAdapter.notifyDataSetChanged()
                }
            }
        }


        CoroutineScope(Dispatchers.IO).launch {
            //userProfileDao.deleteAllProfiles()
        }

        loadProfilesOnStartup()




        findViewById<Button>(R.id.createButton).setOnClickListener {
            GlobalScope.launch(Dispatchers.IO) { // Use Dispatchers.IO for background thread operations
                userProfileDao.insertProfile(UserProfile(username = "JohnDoe3", icon = "J"))
            }
            userProfiles.add(UserProfile(username = "JohnDoe", icon = "J"))
            profileAdapter.notifyDataSetChanged()
        }

        val loginButton = findViewById<TextView>(R.id.loginButton)
        loginButton.setOnClickListener {
            // Here you can perform your login logic (validation, API calls, etc.)
            // Once the login is successful, start the MainActivity




            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish() // Close LoginActivity so it's not on the back stack
        }
    }
}


class ProfileAdapter(private var profiles: List<UserProfile>) :
    RecyclerView.Adapter<ProfileAdapter.ProfileViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProfileViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.login_profile, parent, false)
        return ProfileViewHolder(view)
    }

    override fun onBindViewHolder(holder: ProfileViewHolder, position: Int) {
        val userProfile = profiles[position]
        holder.profileName.text = userProfile.username
    }

    override fun getItemCount(): Int = profiles.size

    class ProfileViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val cardView: CardView = itemView.findViewById(R.id.cardView)
        val profileName: TextView = itemView.findViewById(R.id.profileName)
    }
}





