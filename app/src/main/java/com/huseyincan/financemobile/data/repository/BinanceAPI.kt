package com.huseyincan.financemobile.data.repository

import com.huseyincan.financemobile.data.model.SymbolPrice
import retrofit2.Response
import retrofit2.http.GET

interface BinanceAPI {
    @GET(value = "/api/v3/ticker/price")
    suspend fun getPrice(): Response<List<SymbolPrice>>
}