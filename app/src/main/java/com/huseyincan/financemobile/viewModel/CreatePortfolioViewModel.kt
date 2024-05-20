package com.huseyincan.financemobile.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.huseyincan.financemobile.data.model.SymbolPriceSave
import com.huseyincan.financemobile.data.model.Token
import com.huseyincan.financemobile.data.repository.BackendRetrofitInstance
import com.huseyincan.financemobile.view.portfolio.DataPass
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.launch
import okhttp3.MediaType
import okhttp3.RequestBody
import org.json.JSONArray
import org.json.JSONObject
import retrofit2.Response


class CreatePortfolioViewModel : ViewModel() {
    private var _averagePriceList: MutableLiveData<ArrayList<SymbolPriceSave>> = MutableLiveData()
    val averagePriceList: LiveData<ArrayList<SymbolPriceSave>> get() = _averagePriceList

    fun addToList(item: SymbolPriceSave) {
        val oldList = ArrayList(_averagePriceList.value.orEmpty())
        oldList.add(item)
        _averagePriceList.value = oldList
    }


    fun makeApiCall(token: String) {
        viewModelScope.launch {
            val token: String = "Bearer " + token
            println("TOKEN = $token")
            val reqBod = createPortfolioRequestBody()
            if (reqBod != null) {
                val postResponse =
                    BackendRetrofitInstance.instance.savePortfolio(token, reqBod)
                notifyThePortfolioCall(postResponse)
            } else
                println("Request Body couldn't initiliazed")
        }
    }

    private fun notifyThePortfolioCall(response: Response<String>) {
        if (response.isSuccessful) {
            response.body()?.let {
                println("Portfolio KAYIT EDİLDİ")
            }
        } else {
            val errorBody = response.errorBody()?.string()
            println("Error: $errorBody")
            println("Portfolio KAYIT EDİLMEDİ HATA ${response.code()}")
        }
    }

    private fun handleResponse(response: Response<Token>): String {
        if (response.isSuccessful) {
            response.body()?.let {
                _averagePriceList.value = arrayListOf<SymbolPriceSave>()
                return it.token
            }
        } else {
            println("LOGİN Gelmedi ${response.code()}")
        }
        return ""
    }

    fun createRequestBody(username: String, password: String): RequestBody {
        val json = JSONObject()
        json.put("email", username)
        json.put("password", password)
        val mediaType = MediaType.parse("application/json; charset=utf-8")
        return RequestBody.create(mediaType, json.toString())
    }


    fun createPortfolioRequestBody(): RequestBody? {
        val liste = DataPass.data // arrayList of SymbolPriceSave
        if (!liste.isNullOrEmpty()) {
            val moshi = Moshi.Builder()
                .add(KotlinJsonAdapterFactory())  // Add this line
                .build()
            val adapter = moshi.adapter(SymbolPriceSave::class.java)
            val jsonArray = JSONArray()
            for (item in liste) {
                val jsonString = adapter.toJson(item)
                jsonArray.put(JSONObject(jsonString))
            }
            val json = JSONObject()
            json.put("elements", jsonArray)
            DataPass.clear()
            val mediaType = MediaType.parse("application/json; charset=utf-8")
            return RequestBody.create(mediaType, json.toString())
        } else return null
    }


}