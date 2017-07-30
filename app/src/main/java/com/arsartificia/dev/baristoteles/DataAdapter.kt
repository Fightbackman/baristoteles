package com.arsartificia.dev.baristoteles

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RatingBar
import android.widget.TextView

class DataAdapter
(private val values: MutableList<Entry>) : RecyclerView.Adapter<DataAdapter.ViewHolder>() {

    inner class ViewHolder(var layout: View) : RecyclerView.ViewHolder(layout) {
        var txtName: TextView = layout.findViewById(R.id.coffeeNameTextView)
        var txtWeight: TextView = layout.findViewById(R.id.weightTextView)
        var txtGrind: TextView = layout.findViewById(R.id.grindTextView)
        var txtTime: TextView = layout.findViewById(R.id.timeTextView)
        var txtRating: RatingBar = layout.findViewById(R.id.ratingBar)
        var txtNote: TextView = layout.findViewById(R.id.noteTextView)
    }

    fun add(position: Int, item: Entry) {
        values.add(position, item)
        notifyItemInserted(position)
    }

    override fun onCreateViewHolder(parent: ViewGroup,
                                    viewType: Int): DataAdapter.ViewHolder {
        val inflater = LayoutInflater.from(
                parent.context)
        val v = inflater.inflate(R.layout.row_layout, parent, false)
        val vh = ViewHolder(v)
        return vh
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val e: Entry = values[position]
        holder.txtName.text = e.name
        holder.txtWeight.text = e.weight
        holder.txtTime.text = e.time
        holder.txtGrind.text = e.grind
        holder.txtRating.rating = e.rating
        holder.txtNote.text = e.note
    }

    override fun getItemCount(): Int {
        return values.size
    }

}