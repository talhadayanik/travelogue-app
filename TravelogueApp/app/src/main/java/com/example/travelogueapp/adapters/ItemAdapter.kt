package com.example.contactsapp.adapters

import android.app.Activity
import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.example.travelogueapp.R
import com.example.travelogueapp.models.Item

class ItemAdapter(private val context: Context, private var list: MutableList<Item>) : ArrayAdapter<Item>(context, R.layout.custom_item_layout, list) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = (context as Activity).layoutInflater.inflate(R.layout.custom_item_layout, null, true)

        val title = view.findViewById<TextView>(R.id.txtTitle)
        val city = view.findViewById<TextView>(R.id.txtCity)
        val notes = view.findViewById<TextView>(R.id.txtNotes)

        val item = list.get(position)

        title.text = "${item.title}"
        city.text = "${item.city}"
        notes.text = "${item.notes}"

        return view
    }

    public fun updateItemList(newList: List<Item>){
        list.clear()
        list.addAll(newList)
        this.notifyDataSetChanged()
    }
}