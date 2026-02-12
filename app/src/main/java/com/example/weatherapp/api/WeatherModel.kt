package com.example.weatherapp.api

import com.google.gson.annotations.SerializedName

data class WeatherModel(
    val location : Location,
    @SerializedName("current") val currentWeather : CurrentWeather,
)


data class Location(
    @SerializedName("name")
    val cityName : String,
    val country : String,
)

data class CurrentWeather(
    @SerializedName("temp_c")
    val temperature : Double,
    val condition : Condition,
    @SerializedName("feelslike_c") val feelsLike: Double,
    val humidity : Int,
    @SerializedName("wind_kph") val windSpeed : Double,
    val pressure_mb : Double
)

data class Condition(
    val text : String,
    val icon : String,
)
