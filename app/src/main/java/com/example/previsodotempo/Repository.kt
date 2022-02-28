package com.example.previsodotempo

import com.example.previsodotempo.api.RetrofitInstance
import com.example.previsodotempo.model.WeatherMain
import retrofit2.Response

class Repository {

    suspend fun getPost(): Response<WeatherMain> {
        return RetrofitInstance.api.getWeatherMain()
    }

    suspend fun getWeatherWithCityName(cidade: String): Response<WeatherMain> {
        return RetrofitInstance.api.getWeatherWithCityName(cidade)
    }
}