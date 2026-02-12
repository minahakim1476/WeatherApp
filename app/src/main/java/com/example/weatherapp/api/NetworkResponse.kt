package com.example.weatherapp.api

sealed class NetworkResponse {
    data class Success(val data: WeatherModel) : NetworkResponse()
    data class Error(val message: String) : NetworkResponse()
    object Loading : NetworkResponse()
}