package com.huseyincan.financemobile.data.repository

import com.huseyincan.financemobile.data.model.PhotoResponse
import com.huseyincan.financemobile.data.model.Portfolio
import com.huseyincan.financemobile.data.model.Token
import com.huseyincan.financemobile.data.model.UserResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface BackendAPI {
    @POST(value = "/auth/login")
    suspend fun postLogin(@Body body: RequestBody): Response<Token>

    @POST(value = "/auth/register")
    suspend fun postRegister(@Body body: RequestBody): Response<Token>

    @GET(value = "/portfolio/fetch")
    suspend fun fetchPortfolios(): Response<List<Portfolio>>

    @POST(value = "/portfolio/save")
    suspend fun savePortfolio(
        @Header("Authorization") token: String,
        @Body body: RequestBody
    ): Response<String>

    @GET(value = "/user/fetch")
    suspend fun fetchUserData(@Header("Authorization") token: String): Response<UserResponse>

    @Multipart
    @POST(value = "/user/savePhoto")
    suspend fun savePhoto(
        @Header("Authorization") authHeader: String,
        @Part file: MultipartBody.Part
    ): Response<PhotoResponse>
}