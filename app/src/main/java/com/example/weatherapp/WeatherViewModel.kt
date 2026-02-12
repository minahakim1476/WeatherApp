package com.example.weatherapp

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherapp.api.NetworkResponse
import com.example.weatherapp.api.RetrofitInstance
import kotlinx.coroutines.launch

class WeatherViewModel : ViewModel() {

    private val weatherAPI = RetrofitInstance.weatherAPI
    private val _weatherData = MutableLiveData<NetworkResponse>()
    val weatherData: LiveData<NetworkResponse> = _weatherData

    fun getData(city: String) {
        _weatherData.value = NetworkResponse.Loading
        try {
            viewModelScope.launch {
                val response = weatherAPI.getCurrentWeather(city)
                if (response.isSuccessful) {
                    response.body().let {
                        _weatherData.value = NetworkResponse.Success(it!!)
                    }
                } else {
                    _weatherData.value = NetworkResponse.Error("Error: ${response.message()}")
                    Log.d("WeatherViewModel", "Error: ${response.message()}")
                }
            }
        } catch (e: Exception) {
            _weatherData.value = NetworkResponse.Error("Error: ${e.message}")
        }
    }


}