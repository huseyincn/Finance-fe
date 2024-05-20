package com.huseyincan.financemobile.data.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.huseyincan.financemobile.R
import com.huseyincan.financemobile.data.model.SymbolPrice

class HomeAdapter(private var items: List<SymbolPrice> = emptyList()) :
    RecyclerView.Adapter<HomeAdapter.ViewHolder>() {
    private var listenerItems: onItemClickListener? = null

    private var searchMode: Boolean = false
    private var searchText: String = ""

    class ViewHolder(view: View, private val listener: onItemClickListener?) :
        RecyclerView.ViewHolder(view), View.OnClickListener {
        val productSymbol: TextView
        val productPrice: TextView
        val imgStonks: ImageView

        init {
            // Define click listener for the ViewHolder's View
            productSymbol = view.findViewById(R.id.stonkssymbol)
            productPrice = view.findViewById(R.id.stonksprice)
            imgStonks = view.findViewById(R.id.imgstonks)
            view.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            val position = adapterPosition
            if (position != RecyclerView.NO_POSITION) {
                listener?.onItemClick(position)
            }
        }
    }


    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        // Create a new view, which defines the UI of the list item
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.product_card, viewGroup, false)

        return ViewHolder(view, listenerItems)
    }


    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {

        // Get element from your dataset at this position and replace the
        // contents of the view with that element
        val tmp: SymbolPrice = items.get(position)
        viewHolder.productSymbol.text = tmp.symbol
        viewHolder.productPrice.text = tmp.price
        if (tmp.oldPrice != 0.0) {
            if (tmp.oldPrice > tmp.price.toDouble()) {
                viewHolder.imgStonks.setImageResource(R.drawable.baseline_arrow_downward_24)
            } else if (tmp.oldPrice < tmp.price.toDouble()) {
                viewHolder.imgStonks.setImageResource(R.drawable.baseline_arrow_upward_24)
            }
        } else { // test amaçlı herkesi equal başlatıyorum
            viewHolder.imgStonks.setImageResource(R.drawable.equal)
        }
    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = items.size

    fun updateItems(newItems: List<SymbolPrice>) {
        if (searchMode == true) {
            val filteredList = arrayListOf<SymbolPrice>()
            for (symbol in newItems) {
                if (symbol.symbol.startsWith(searchText, true)) {
                    filteredList.add(symbol)
                }
            }

            if (filteredList.isEmpty()) {
                this.items = newItems
            } else {
                this.items = filteredList
            }
        } else {
            this.items = newItems
        }
        notifyDataSetChanged()
    }

    interface onItemClickListener {
        fun onItemClick(position: Int)
    }

    fun getItemsForFilteration(): List<SymbolPrice> {
        return this.items
    }

    fun setSearchModeOn(str: String) {
        this.searchMode = true
        this.searchText = str
    }

    fun setSearchModeOf() {
        this.searchMode = false
    }
}