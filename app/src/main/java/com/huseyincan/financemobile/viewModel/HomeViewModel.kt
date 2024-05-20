package com.huseyincan.financemobile.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.huseyincan.financemobile.data.model.SymbolPrice
import com.huseyincan.financemobile.data.repository.BinanceRetrofitInstance
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import retrofit2.Response

class HomeViewModel : ViewModel() {

    private var _averagePriceList: MutableLiveData<ArrayList<SymbolPrice>> = MutableLiveData()
    val averagePriceList: LiveData<ArrayList<SymbolPrice>> get() = _averagePriceList

    fun makeApiCall() {
        viewModelScope.launch {
            val response = BinanceRetrofitInstance.instance.getPrice()
            handleResponse(response)
        }
    }

    private fun handleResponse(response: Response<List<SymbolPrice>>) {
        if (response.isSuccessful) {
            removeFromList()
            response.body()?.let {
                if (it.isNotEmpty()) {
                    addInList(it)
                }
            }
        } else {
            println("Binance Gelmedi")
        }
    }

    fun addInList(data: List<SymbolPrice>) {
        val oldList = ArrayList(_averagePriceList.value.orEmpty())
        val newList = arrayListOf<SymbolPrice>()
        if (data.isNotEmpty() && oldList.isNotEmpty() && data.size == oldList.size) {
            for (i in 0 until data.size) {
                val tmp = data.get(i)
                val old = oldList.get(i)
                newList.add(SymbolPrice(tmp.symbol, tmp.price, old.price.toDouble()))
            }
        } else {
            newList.addAll(data)
        }
        _averagePriceList.value = newList
    }

    fun removeFromList() {
        val oldList = arrayListOf<SymbolPrice>()
        _averagePriceList.value = oldList
    }

    fun startUpdatingPrices() {
        viewModelScope.launch {
            updatePricesEveryMinute().collect { prices ->
                addInList(prices)
            }
        }
    }

    private fun updatePricesEveryMinute(): Flow<List<SymbolPrice>> = flow {
        while (true) {
            val response = BinanceRetrofitInstance.instance.getPrice()
            if (response.isSuccessful) {
                val tickerPrice = response.body()
                if (tickerPrice != null) {
                    emit(tickerPrice)
                } // Emit the new price
            }
            delay(3000) // Wait for a minute before the next update
        }
    }
}