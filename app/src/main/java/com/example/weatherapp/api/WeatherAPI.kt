package com.example.weatherapp.api

import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherAPI {
    @GET(RetrofitInstance.CURRENT_WEATHER_ENDPOINT)
    suspend fun getCurrentWeather(
        @Query("key") apiKey: String = RetrofitInstance.API_KEY,
        @Query("q") city: String
    )
}