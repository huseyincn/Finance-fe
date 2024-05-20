package com.huseyincan.financemobile.data.repository

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

object BackendRetrofitInstance {
    private const val BASE_URL = "https://finance-mobile.azurewebsites.net"
    //private const val BASE_URL = "http://10.0.2.2:8080/"
    private val moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()

    val instance: BackendAPI by lazy {
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()

        retrofit.create(BackendAPI::class.java)
    }
}