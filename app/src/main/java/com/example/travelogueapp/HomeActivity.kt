package com.example.travelogueapp

import android.content.Intent
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.appcompat.app.AlertDialog
import com.example.contactsapp.adapters.ItemAdapter
import com.example.travelogueapp.databinding.ActivityHomeBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.example.travelogueapp.models.Item
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class HomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding
    lateinit var currentUser: FirebaseUser
    lateinit var db: DatabaseReference
    lateinit var list: MutableList<Item>
    lateinit var itemAdapter: ItemAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        currentUser = FirebaseAuth.getInstance().currentUser!!
        db = FirebaseDatabase.getInstance().getReference("Users/${currentUser.uid}")

        list = mutableListOf()
        itemAdapter = ItemAdapter(this,list)
        binding.listItem.adapter = itemAdapter

        binding.fabAdd.setOnClickListener {
            val i = Intent(this, AddItemActivity::class.java)
            startActivity(i)
        }

        binding.listItem.setOnItemLongClickListener { parent, view, position, id ->
            val selectedItem = list[position]
            val builder = AlertDialog.Builder(this)
            val view = layoutInflater.inflate(R.layout.dialog_remove, null)
            builder.setView(view)
            val dialog = builder.create()

            view.findViewById<Button>(R.id.btnYes).setOnClickListener {
                db.child(selectedItem.itemId).removeValue()
                loadList {
                    itemAdapter.updateItemList(it)
                }
                dialog.dismiss()
            }

            view.findViewById<Button>(R.id.btnCancel).setOnClickListener {
                dialog.dismiss()
            }
            if (dialog.window != null){
                dialog.window!!.setBackgroundDrawable(ColorDrawable(0))
            }
            dialog.show()
            false
        }
    }

    override fun onStart() {
        super.onStart()
        loadList {
            itemAdapter.updateItemList(it)
        }
    }

    fun loadList(callback: (list: List<Item>) -> Unit) {
        db.addListenerForSingleValueEvent(object: ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                val list: MutableList<Item> = mutableListOf()
                snapshot.children.forEach {
                    var item = Item(it.key.toString(),it.child("title").getValue().toString(), it.child("city").getValue().toString(), it.child("notes").getValue().toString())
                    list.add(item)
                }
                callback(list)
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
    }
}