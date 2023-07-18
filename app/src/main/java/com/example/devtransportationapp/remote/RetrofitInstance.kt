package com.example.devtransportationapp.remote

import com.example.devtransportationapp.api.ApiInterface
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {
//    val okHttpClient = OkHttpClient.Builder().apply {
//            this.addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
//    }.build()

    val api = Retrofit.Builder()
    .baseUrl("https://maps.googleapis.com/maps/api/")
    .addConverterFactory(GsonConverterFactory.create())
//    .client(okHttpClient)
    .build()

}