package com.example.pedal.data.network.modelresponse

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface Endpoint {

    @GET("autocomplete")
    //?text=    Mosco   &apiKey=    85c3f1a79407478ba8592a38dd0d41d3
    fun getPosts(@Query("text") text: String, @Query("apiKey") key: String): Call<Posts>
}