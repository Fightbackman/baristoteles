package com.arsartificia.dev.baristoteles

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView


class DataAdapter
(private val values: MutableList<String>) : RecyclerView.Adapter<DataAdapter.ViewHolder>() {

    inner class ViewHolder(var layout: View) : RecyclerView.ViewHolder(layout) {
        var txtName: TextView
        var txtWeight: TextView
        var txtRating: TextView

        init {
            txtName = layout.findViewById(R.id.coffeeNameTextView)
            txtWeight = layout.findViewById(R.id.weightTextView)
            txtRating = layout.findViewById(R.id.ratingTextView)
        }
    }

    fun add(position: Int, item: String) {
        values.add(position, item)
        notifyItemInserted(position)
    }

    fun remove(position: Int) {
        values.removeAt(position)
        notifyItemRemoved(position)
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
        val name = values[position]
        holder.txtName.text = name
    }

    override fun getItemCount(): Int {
        return values.size
    }

}