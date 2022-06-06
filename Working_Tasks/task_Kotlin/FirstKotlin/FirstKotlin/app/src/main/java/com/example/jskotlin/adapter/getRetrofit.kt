package com.example.jskotlin.adapter

import com.example.jskotlin.DataModel
import retrofit2.Call
import retrofit2.http.GET




interface getRetrofit {
    @GET("currentprice.json")
    fun getPDFs(modal: DataModel): Call<DataModel?>?
}