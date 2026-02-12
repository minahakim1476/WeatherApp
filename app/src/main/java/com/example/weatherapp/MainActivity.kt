package com.example.weatherapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModelProvider
import com.example.weatherapp.ui.theme.WeatherAppTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val viewModel = ViewModelProvider(this)[WeatherViewModel::class.java]
        setContent {
            val modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFF0A0F1E))
                .statusBarsPadding()
                .navigationBarsPadding()

            WeatherAppTheme {
                Scaffold(
                    modifier = modifier,
                    containerColor = Color.Transparent
                ) { innerPadding ->
                    WeatherPage(
                        modifier,
                        viewModel
                    )
                }
            }
        }
    }
}