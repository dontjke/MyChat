package com.stepanov.mychat

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.firebase.Firebase
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.database
import com.stepanov.mychat.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Write a message to the database
        val database = Firebase.database
        val myRef = database.getReference("message")

        binding.sendButton.setOnClickListener {
            myRef.setValue(binding.textMessage.text.toString())
        }
        onChangeListener(myRef)
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
}