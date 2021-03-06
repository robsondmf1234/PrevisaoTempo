package com.example.previsodotempo.api

import com.example.previsodotempo.model.WeatherMain
import com.example.previsodotempo.util.Constants.Companion.APP_KEY
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface SimpleApi {

    @GET("data/2.5/weather")
    suspend fun getWeatherMain(
        @Query("lat") lat: Double = -22.858191872761903,
        @Query("lon") lon: Double = -47.212808251223585,
        @Query("appid") appid: String = APP_KEY
    ): Response<WeatherMain>

    @GET("data/2.5/weather?&units=metric")
    suspend fun getWeatherWithCityName(
        @Query("q") cityName: String,
        @Query("appid") appid: String = APP_KEY
    ): Response<WeatherMain>


}