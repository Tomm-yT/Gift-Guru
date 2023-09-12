package com.training.recycler.presentation

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat.startActivity
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

    fun launchAppWithProfile(username : String){

        val intent = Intent(this, MainActivity::class.java)
        intent.putExtra("USERNAME_KEY", username)
        startActivity(intent)
        finish() // Closes LoginActivity so it's not on the back stack
    }

    lateinit var userProfiles : MutableList<UserProfile>
    lateinit var profileAdapter : ProfileAdapter


    fun deleteProfile(usernameToDelete: String, userProfiles: MutableList<UserProfile>, adapter: ProfileAdapter, userProfileDao: UserProfileDao) {

        //Remove from the data source
        val iterator = userProfiles.iterator()
        while(iterator.hasNext()) {
            val userProfile = iterator.next()
            if(userProfile.username == usernameToDelete) {
                iterator.remove()

                //Delete from the database
                CoroutineScope(Dispatchers.IO).launch {
                    userProfileDao.deleteProfile(userProfile) // Assuming this is the correct DAO method
                }
                break
            }
        }

        //Notify adapter of changes
        adapter.notifyDataSetChanged()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login_screen)
        supportActionBar?.hide();



        userProfiles = mutableListOf<UserProfile>()
        profileAdapter = ProfileAdapter(this, userProfiles)


        val profilesRecyclerView: RecyclerView = findViewById(R.id.profilesRecyclerView)
        profilesRecyclerView.layoutManager = GridLayoutManager(this, 2)  // 2 columns in grid
        profilesRecyclerView.adapter = profileAdapter

        profileAdapter.notifyDataSetChanged()

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

        //CLEAR PROFILES FOR TESTING
        CoroutineScope(Dispatchers.IO).launch {
            //userProfileDao.deleteAllProfiles()
        }

        loadProfilesOnStartup()

        findViewById<CardView>(R.id.defaultProfile).setOnClickListener {


            val editText = EditText(this)
            val dialog = AlertDialog.Builder(this)
                .setTitle("Creating New Profile")
                .setMessage("Enter the name of the new profile.")
                .setView(editText)
                .setPositiveButton("OK") { dialog, _ ->
                    val inputText = editText.text.toString()
                    Toast.makeText(applicationContext, "Profile created: $inputText", Toast.LENGTH_LONG).show()

                    GlobalScope.launch(Dispatchers.IO) { // Use Dispatchers.IO for background thread operations
                        userProfileDao.insertProfile(UserProfile(username = inputText, icon = inputText[0].toString()))
                    }
                    userProfiles.add(UserProfile(username = inputText, icon = inputText[0].toString()))
                    profileAdapter.notifyDataSetChanged()

                    dialog.dismiss()
                }
                .setNegativeButton("Cancel") { dialog, _ -> dialog.dismiss() }
                .create()
            dialog.show()
        }
    }
}


class ProfileAdapter(val context: Context, private var profiles: List<UserProfile>) :
    RecyclerView.Adapter<ProfileAdapter.ProfileViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProfileViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.login_profile, parent, false)
        return ProfileViewHolder(context, view)
    }

    override fun onBindViewHolder(holder: ProfileViewHolder, position: Int) {
        val userProfile = profiles[position]
        holder.profileName.text = userProfile.username
        holder.profileIcon.text = userProfile.icon
    }

    override fun getItemCount(): Int = profiles.size

    class ProfileViewHolder(context: Context, itemView: View) : RecyclerView.ViewHolder(itemView) {
        val profileName: TextView = itemView.findViewById(R.id.profileName)
        val profileIcon: TextView = itemView.findViewById(R.id.profile_icon)
        val deleteProfile: ImageView = itemView.findViewById(R.id.deleteProfile)

        init {
            val activity = context as LoginActivity

            itemView.setOnClickListener {

                Log.d("Login Activity:", "Launching app under profile: ${profileName.text}")
                activity.launchAppWithProfile(profileName.text.toString())
            }

            deleteProfile.setOnClickListener {
                activity.deleteProfile(profileName.text.toString(), activity.userProfiles, activity.profileAdapter, activity.userProfileDao)
                Log.d("Login Activity:", "Deleting profile: ${profileName.text}")
            }
        }
    }
}





