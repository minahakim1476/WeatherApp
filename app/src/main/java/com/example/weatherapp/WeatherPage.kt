package com.example.weatherapp

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.example.weatherapp.api.NetworkResponse
import com.example.weatherapp.api.WeatherModel
import kotlin.math.round

// Design tokens
private val BackgroundStart = Color(0xFF0A0F1E)
private val BackgroundEnd = Color(0xFF1A1035)
private val AccentBlue = Color(0xFF4FC3F7)
private val AccentPurple = Color(0xFF9C6FDE)
private val AccentTeal = Color(0xFF26C6DA)
private val GlassWhite = Color(0x1AFFFFFF)
private val GlassBorder = Color(0x33FFFFFF)
private val TextPrimary = Color(0xFFEEF2FF)
private val TextSecondary = Color(0xFF9BAFD0)
private val CardBackground = Color(0x14FFFFFF)

@Composable
fun WeatherPage(
    modifier: Modifier = Modifier,
    viewModel: WeatherViewModel
) {
    var city by remember { mutableStateOf("") }
    val weatherData by viewModel.weatherData.observeAsState()
    val keyboardController = LocalSoftwareKeyboardController.current

    Box(
        modifier = modifier
            .background(
                Brush.radialGradient(
                    colors = listOf(
                        Color(0xFF1B2B5E),
                        Color(0xFF0A0F1E)
                    ),
                    radius = 1200f
                )
            )
    ) {
        // Decorative background blobs
        Box(
            modifier = Modifier
                .size(300.dp)
                .offset(x = (-60).dp, y = 80.dp)
                .blur(80.dp)
                .background(
                    Color(0x284FC3F7),
                    CircleShape
                )
        )
        Box(
            modifier = Modifier
                .size(250.dp)
                .align(Alignment.TopEnd)
                .offset(x = 60.dp, y = 200.dp)
                .blur(80.dp)
                .background(
                    Color(0x289C6FDE),
                    CircleShape
                )
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp, vertical = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(8.dp))

            // Search Bar
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedTextField(
                    modifier = Modifier.weight(1f),
                    value = city,
                    onValueChange = { city = it },
                    placeholder = {
                        Text(
                            "Search city...",
                            color = TextSecondary,
                            fontSize = 15.sp
                        )
                    },
                    singleLine = true,
                    shape = RoundedCornerShape(16.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = TextPrimary,
                        unfocusedTextColor = TextPrimary,
                        focusedBorderColor = AccentBlue.copy(alpha = 0.7f),
                        unfocusedBorderColor = GlassBorder,
                        focusedContainerColor = CardBackground,
                        unfocusedContainerColor = CardBackground,
                        cursorColor = AccentBlue
                    )
                )
                Spacer(modifier = Modifier.width(12.dp))
                Box(
                    modifier = Modifier
                        .size(56.dp)
                        .background(
                            Brush.linearGradient(
                                listOf(AccentBlue, AccentPurple)
                            ),
                            RoundedCornerShape(16.dp)
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    IconButton(onClick = {
                        viewModel.getData(city)
                        keyboardController?.hide()
                    }) {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = "Search",
                            tint = Color.White,
                            modifier = Modifier.size(22.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            when (weatherData) {
                is NetworkResponse.Success -> {
                    WeatherDetails((weatherData as NetworkResponse.Success).data)
                }
                is NetworkResponse.Error -> {
                    ErrorCard((weatherData as NetworkResponse.Error).message)
                }
                is NetworkResponse.Loading -> {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(300.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(
                            color = AccentBlue,
                            strokeWidth = 3.dp,
                            modifier = Modifier.size(48.dp)
                        )
                    }
                }
                else -> {
                    EmptyState()
                }
            }
        }
    }
}

@Composable
fun EmptyState() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 80.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("🌍", fontSize = 64.sp)
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            "Discover the Weather",
            color = TextPrimary,
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            "Search for any city to get\ncurrent weather conditions",
            color = TextSecondary,
            fontSize = 15.sp,
            textAlign = TextAlign.Center,
            lineHeight = 22.sp
        )
    }
}

@Composable
fun ErrorCard(message: String) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 32.dp)
            .background(
                Color(0x33FF5252),
                RoundedCornerShape(20.dp)
            )
            .border(1.dp, Color(0x66FF5252), RoundedCornerShape(20.dp))
            .padding(20.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "⚠️  $message",
            color = Color(0xFFFF8A80),
            fontSize = 14.sp,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun WeatherDetails(weatherModel: WeatherModel) {
    val temp = round(weatherModel.currentWeather.temperature).toInt()
    val feelsLike = round(weatherModel.currentWeather.feelsLike).toInt()

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Location Row
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = Icons.Default.LocationOn,
                contentDescription = "Location",
                tint = AccentBlue,
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = "${weatherModel.location.cityName}, ${weatherModel.location.country}",
                color = TextPrimary,
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Main Hero Card
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    Brush.linearGradient(
                        colors = listOf(
                            Color(0x33FFFFFF),
                            Color(0x10FFFFFF)
                        )
                    ),
                    RoundedCornerShape(28.dp)
                )
                .border(
                    1.dp,
                    Brush.linearGradient(
                        listOf(
                            Color(0x55FFFFFF),
                            Color(0x11FFFFFF)
                        )
                    ),
                    RoundedCornerShape(28.dp)
                )
                .padding(28.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                // Weather icon
                AsyncImage(
                    model = "https:${weatherModel.currentWeather.condition.icon}"
                        .replace("64x64", "128x128"),
                    contentDescription = "Weather Icon",
                    modifier = Modifier.size(100.dp)
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Big temperature
                Text(
                    text = "$temp°",
                    fontSize = 88.sp,
                    fontWeight = FontWeight.Thin,
                    color = TextPrimary,
                    letterSpacing = (-4).sp
                )

                // Condition text
                Text(
                    text = weatherModel.currentWeather.condition.text,
                    color = AccentBlue,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Medium,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Feels like $feelsLike°C",
                    color = TextSecondary,
                    fontSize = 14.sp
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Stats Grid Card
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(CardBackground, RoundedCornerShape(24.dp))
                .border(1.dp, GlassBorder, RoundedCornerShape(24.dp))
                .padding(20.dp)
        ) {
            Column {
                Text(
                    "Current Conditions",
                    color = TextSecondary,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold,
                    letterSpacing = 1.sp,
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    StatItem(
                        emoji = "💧",
                        label = "Humidity",
                        value = "${weatherModel.currentWeather.humidity}%",
                        modifier = Modifier.weight(1f)
                    )
                    VerticalDivider()
                    StatItem(
                        emoji = "💨",
                        label = "Wind",
                        value = "${weatherModel.currentWeather.windSpeed} km/h",
                        modifier = Modifier.weight(1f)
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))
                HorizontalDivider()
                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    StatItem(
                        emoji = "🌡️",
                        label = "Feels Like",
                        value = "$feelsLike°C",
                        modifier = Modifier.weight(1f)
                    )
                    VerticalDivider()
                    StatItem(
                        emoji = "⏱️",
                        label = "Pressure",
                        value = "${weatherModel.currentWeather.pressure_mb} mb",
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

//        // Accent bottom bar
//        Box(
//            modifier = Modifier
//                .width(60.dp)
//                .height(4.dp)
//                .background(
//                    Brush.horizontalGradient(
//                        listOf(AccentBlue, AccentPurple)
//                    ),
//                    RoundedCornerShape(2.dp)
//                )
//        )
        Spacer(modifier = Modifier.height(16.dp))
    }
}

@Composable
fun StatItem(
    emoji: String,
    label: String,
    value: String,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.padding(horizontal = 8.dp, vertical = 4.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(emoji, fontSize = 24.sp)
        Spacer(modifier = Modifier.height(6.dp))
        Text(
            value,
            color = TextPrimary,
            fontSize = 16.sp,
            fontWeight = FontWeight.SemiBold
        )
        Spacer(modifier = Modifier.height(2.dp))
        Text(
            label,
            color = TextSecondary,
            fontSize = 12.sp
        )
    }
}

@Composable
fun VerticalDivider() {
    Box(
        modifier = Modifier
            .width(1.dp)
            .height(64.dp)
            .background(GlassBorder)
    )
}

@Composable
fun HorizontalDivider() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(1.dp)
            .background(GlassBorder)
    )
}

// Keep backward compat
@Composable
fun RowOfConditions(weatherModel: WeatherModel) {}

@Composable
fun SingleCondition(key: String, value: String) {}