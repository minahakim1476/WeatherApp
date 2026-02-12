package com.example.weatherapp

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.BrushPainter
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.example.weatherapp.api.NetworkResponse
import com.example.weatherapp.api.WeatherModel
import kotlin.math.round

@Composable
fun WeatherPage(
    modifier: Modifier = Modifier,
    viewModel: WeatherViewModel
) {
    var city by remember { mutableStateOf("") }

    val weatherData by viewModel.weatherData.observeAsState()

    val keyboardController = LocalSoftwareKeyboardController.current


    Column(
        modifier = modifier, horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            OutlinedTextField(
                modifier = Modifier.weight(1f),
                value = city,
                onValueChange = { city = it },
                label = { Text("Search...") })
            IconButton(
                onClick = {
                    viewModel.getData(city)
                    keyboardController?.hide()
                }) {
                Icon(
                    imageVector = Icons.Default.Search, contentDescription = "Search Icon"
                )
            }
        }

        when (weatherData) {
            is NetworkResponse.Success -> {
                WeatherDetails((weatherData as NetworkResponse.Success).data)
            }

            is NetworkResponse.Error -> {
                Text(text = (weatherData as NetworkResponse.Error).message)
            }

            is NetworkResponse.Loading -> {
                CircularProgressIndicator()
            }

            else -> {}
        }
    }
}

@Composable
fun WeatherDetails(
    weatherModel: WeatherModel
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.Bottom
        ) {
            Icon(
                imageVector = Icons.Default.LocationOn,
                contentDescription = "Location icon",
                modifier = Modifier.size(40.dp)
            )
            Text(text = weatherModel.location.cityName, fontSize = 32.sp)
            Spacer(Modifier.width(8.dp))
            Text(text = weatherModel.location.country, fontSize = 20.sp, color = Color.Gray)
        }
        Spacer(Modifier.width(32.dp))
        Text(
            text = "${round(weatherModel.currentWeather.temperature).toInt()}°c",
            fontSize = 64.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )

        AsyncImage(
            model = "https:${weatherModel.currentWeather.condition.icon}".replace("64x64", "128x128"),
            contentDescription = "Weather Icon",
            modifier = Modifier.size(128.dp)
        )

        Text(
            text = weatherModel.currentWeather.condition.text,
            fontSize = 24.sp,
            textAlign = TextAlign.Center,
            color = Color.Gray
        )

        Card(
            modifier = Modifier
                .padding(16.dp)
        ) {
            Column() {
                RowOfConditions(weatherModel)
            }
        }
    }
}

@Composable
fun RowOfConditions(weatherModel: WeatherModel) {
    Row() {
        SingleCondition("Feels like", "${round(weatherModel.currentWeather.feelsLike).toInt()}°c")
        SingleCondition("Humidity", "${weatherModel.currentWeather.humidity}%")
    }
    Row(){
        SingleCondition("Wind speed", "${weatherModel.currentWeather.windSpeed}km/h")
        SingleCondition("Pressure", "${weatherModel.currentWeather.pressure_mb}mb")
    }
}

@Composable
fun SingleCondition(
    key : String,
    value : String
) {
    Column(
        modifier = Modifier.padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(value)
        Text(key)
    }
}