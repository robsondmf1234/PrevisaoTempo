package com.example.previsodotempo.model


data class WeatherMain(
    val coord: Coord,
    val weather: List<Weather>,
    val base: String,
    val main: Main,
    val visibility: Int,
    val wind: Wind,
    val cloud: Cloud,
    val sys: Sys,
    val timezone: Int,
    val id: Int,
    val name: String,
    val cod: Int
)
