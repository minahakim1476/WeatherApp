package com.example.weatherapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModelProvider
import com.example.weatherapp.ui.theme.WeatherAppTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val viewModel = ViewModelProvider(this)[WeatherViewModel::class.java]
        setContent {
            val modifier = Modifier.fillMaxSize()
                .background(brush = Brush.linearGradient(
                    listOf(
                        Color(14 , 38 , 75),
                        Color(43 , 28 , 75)
                    )
                ))
                .fillMaxSize()
                .statusBarsPadding()
            WeatherAppTheme {
                Scaffold(
                    modifier = modifier
                ) { innerPadding ->
                    WeatherPage(
                        modifier.padding(innerPadding),
                        viewModel
                    )
                }
            }
        }
    }

}
