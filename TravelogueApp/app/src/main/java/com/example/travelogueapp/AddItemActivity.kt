package com.example.travelogueapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.travelogueapp.databinding.ActivityAddItemBinding
import com.example.travelogueapp.models.Item
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class AddItemActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddItemBinding
    private lateinit var db: DatabaseReference
    lateinit var currentUser: FirebaseUser

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddItemBinding.inflate(layoutInflater)
        setContentView(binding.root)

        db = FirebaseDatabase.getInstance().getReference("Users")
        currentUser = FirebaseAuth.getInstance().currentUser!!

        binding.btnAdd.setOnClickListener {
            addNote()
        }
    }

    fun addNote(){
        val title = binding.txtAddTitle.text.toString()
        val city = binding.txtAddCity.text.toString()
        val notes = binding.txtAddNotes.text.toString()

        if(title.isNotEmpty() && city.isNotEmpty() && notes.isNotEmpty()){
            val id = db.child(currentUser.uid).push().key.toString()
            val item = Item(id,title, city, notes)
            db.child(currentUser.uid).child(id).setValue(item)
            val i = Intent(this, HomeActivity::class.java)
            startActivity(i)
            finish()
        }else{
            if (title.isEmpty()){
                binding.txtAddTitle.setError("This field cannot be empty")
            }
            if(city.isEmpty()){
                binding.txtAddCity.setError("This field cannot be empty")
            }
            if(notes.isEmpty()){
                binding.txtAddNotes.setError("This field cannot be empty")
            }
        }
    }
}