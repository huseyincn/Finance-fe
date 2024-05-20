package com.huseyincan.financemobile.data.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.huseyincan.financemobile.R
import com.huseyincan.financemobile.data.model.SymbolPriceSave

class ContentAdapter(private var items: List<SymbolPriceSave> = emptyList()) :
    RecyclerView.Adapter<ContentAdapter.ViewHolder>() {
    private var listenerItems: onItemClickListener? = null

    interface onItemClickListener {
        fun onItemClick(position: Int)
    }

    fun setOnItemClickListener(listener: onItemClickListener) {
        this.listenerItems = listener
    }

    fun getItem(position: Int): SymbolPriceSave {
        return items[position]
    }

    class ViewHolder(view: View, private val listener: onItemClickListener?) :
        RecyclerView.ViewHolder(view), View.OnClickListener {
        val productSymbol: TextView
        val productPrice: TextView
        val productQuantity: TextView

        init {
            // Define click listener for the ViewHolder's View
            productSymbol = view.findViewById(R.id.contentsymbol)
            productPrice = view.findViewById(R.id.contentprice)
            productQuantity = view.findViewById(R.id.contentquantity)
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
        val view = LayoutInflater.from(parent.context).inflate(R.layout.pcontent, parent, false)
        return ViewHolder(view, listenerItems)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val tmp: SymbolPriceSave = items[position]
        holder.productSymbol.text = tmp.symbol
        holder.productPrice.text = String.format("%.6f", tmp.price)
        holder.productQuantity.text = tmp.quantity.toString()
    }

    fun updateItems(newItems: List<SymbolPriceSave>) {
        this.items = newItems
        notifyDataSetChanged()
    }
}