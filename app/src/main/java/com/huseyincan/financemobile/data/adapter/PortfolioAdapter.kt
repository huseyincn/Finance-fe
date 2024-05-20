package com.huseyincan.financemobile.data.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.huseyincan.financemobile.R
import com.huseyincan.financemobile.data.model.Portfolio

class PortfolioAdapter(private var items: List<Portfolio> = emptyList()) :
    RecyclerView.Adapter<PortfolioAdapter.ViewHolder>() {
    private var listenerItems: onItemClickListener? = null
    fun setOnItemClickListener(listener: onItemClickListener) {
        this.listenerItems = listener
    }

    fun getItem(position: Int): Portfolio {
        return items[position]
    }

    class ViewHolder(view: View, private val listener: onItemClickListener?) :
        RecyclerView.ViewHolder(view), View.OnClickListener {
        val pUsername: TextView
        val pRevenue: TextView

        init {
            // Define click listener for the ViewHolder's View
            pUsername = view.findViewById(R.id.portfoliocreator)
            pRevenue = view.findViewById(R.id.prevenue)
            view.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            val position = adapterPosition
            if (position != RecyclerView.NO_POSITION) {
                listener?.onItemClick(position)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // Create a new view, which defines the UI of the list item
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.portfolio_card, parent, false)
        return ViewHolder(view, listenerItems)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val tmp: Portfolio = items.get(position)
        holder.pUsername.text = tmp.userName
        holder.pRevenue.text = String.format("%.6f", tmp.revenue)
    }

    interface onItemClickListener {
        fun onItemClick(position: Int)
    }

    fun updateItems(newItems: List<Portfolio>) {
        this.items = newItems
        notifyDataSetChanged()
    }
}