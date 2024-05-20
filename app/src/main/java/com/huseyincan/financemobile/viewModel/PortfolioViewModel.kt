package com.huseyincan.financemobile.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.huseyincan.financemobile.data.model.Portfolio
import com.huseyincan.financemobile.data.repository.BackendRetrofitInstance
import kotlinx.coroutines.launch
import retrofit2.Response

class PortfolioViewModel : ViewModel() {

    private val _portfoliolist: MutableLiveData<ArrayList<Portfolio>> = MutableLiveData()
    val portfoliolist: LiveData<ArrayList<Portfolio>> = _portfoliolist

    fun makeApiCall() {
        viewModelScope.launch {
            val response = BackendRetrofitInstance.instance.fetchPortfolios()
            handleResponse(response)
        }
    }

    private fun handleResponse(response: Response<List<Portfolio>>) {
        if (response.isSuccessful) {
            removeFromList()
            response.body()?.let {
                if (it.isNotEmpty()) {
                    addInList(it)
                }
            }
            println("veri eklendi")
        } else {
            println("Portfolio Gelmedi")
        }
    }

    fun addInList(data: List<Portfolio>) {
        val oldList = ArrayList(_portfoliolist.value.orEmpty())
        oldList.addAll(data) // add the new data to the old list
        _portfoliolist.value = oldList
    }

    fun removeFromList() {
        val oldList = arrayListOf<Portfolio>()
        _portfoliolist.value = oldList
    }
}