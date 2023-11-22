package com.stepanov.mychat

import android.graphics.drawable.BitmapDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.database
import com.squareup.picasso.Picasso
import com.stepanov.mychat.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        auth = Firebase.auth
        setUpActionBar()

        // Write a message to the database
        val database = Firebase.database
        val myRef = database.getReference("message")

        binding.sendButton.setOnClickListener {
            myRef.setValue(binding.textMessage.text.toString())
        }
        onChangeListener(myRef)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu,menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.signOut){
            auth.signOut()
            finish()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun onChangeListener(databaseReference: DatabaseReference){
        databaseReference.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                binding.apply {
                    recyclerView.append("\n")
                    recyclerView.append("Roman: ${snapshot.value.toString()}")
                }
            }

            override fun onCancelled(error: DatabaseError) {

            }

        })
    }

    private fun setUpActionBar(){
        val actionBar = supportActionBar
        Thread{
            val bMap = Picasso.get().load(auth.currentUser?.photoUrl).get()
            val drawableIcon = BitmapDrawable(resources,bMap)
            runOnUiThread {
                actionBar?.setDisplayHomeAsUpEnabled(true)
                actionBar?.setHomeAsUpIndicator(drawableIcon)
                actionBar?.title=auth.currentUser?.displayName
            }
        }.start()

    }
}