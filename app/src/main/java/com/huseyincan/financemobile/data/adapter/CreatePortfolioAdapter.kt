package com.huseyincan.financemobile.data.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.viewModelScope
import androidx.recyclerview.widget.RecyclerView
import com.huseyincan.financemobile.R
import com.huseyincan.financemobile.data.model.SymbolPrice
import com.huseyincan.financemobile.data.repository.BackendRetrofitInstance
import com.huseyincan.financemobile.data.repository.BinanceRetrofitInstance
import kotlinx.coroutines.launch

class CreatePortfolioAdapter(private var items: List<SymbolPrice> = emptyList()) :
    RecyclerView.Adapter<CreatePortfolioAdapter.ViewHolder>() {
    private var listenerItems: onItemClickListener? = null
    private var searchMode: Boolean = false
    private var searchText: String = ""

    interface onItemClickListener {
        fun onItemClick(position: Int)
    }

    class ViewHolder(view: View, private val listener: onItemClickListener?) :
        RecyclerView.ViewHolder(view), View.OnClickListener {
        val productSymbol: TextView
        val productPrice: TextView

        init {
            // Define click listener for the ViewHolder's View
            productSymbol = view.findViewById(R.id.stonkssymbol)
            productPrice = view.findViewById(R.id.stonksprice)
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
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.product_card_create, parent, false)
        return ViewHolder(view, listenerItems)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val tmp: SymbolPrice = items[position]
        holder.productSymbol.text = tmp.symbol
        holder.productPrice.text = tmp.price
    }

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
    // Add this method to set the listener from outside
    fun setOnItemClickListener(listener: onItemClickListener) {
        this.listenerItems = listener
    }

    fun getItem(position: Int): SymbolPrice {
        return items[position]
    }

    fun setSearchModeOn(str: String) {
        this.searchMode = true
        this.searchText = str
    }

    fun setSearchModeOf() {
        this.searchMode = false
    }
}