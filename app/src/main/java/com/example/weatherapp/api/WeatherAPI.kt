package com.example.weatherapp.api

import androidx.lifecycle.LiveData
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherAPI {
    @GET(RetrofitInstance.CURRENT_WEATHER_ENDPOINT)
    suspend fun getCurrentWeather(
        @Query("q") city: String,
        @Query("key") apiKey: String = RetrofitInstance.API_KEY
    ): Response<WeatherModel>
}