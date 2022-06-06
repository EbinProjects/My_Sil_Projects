package com.example.jskotlin.adapter

import retrofit2.Call
import retrofit2.http.Body

import retrofit2.http.POST




interface RetrofitApi {
    @POST("users")
    fun getCategories(@Body dataModal: DataNodel?): Call<DataNodel?>?
}