package com.example.previsodotempo

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.previsodotempo.model.WeatherMain
import kotlinx.coroutines.launch
import retrofit2.Response

class MainViewModel(private val repository: Repository) : ViewModel() {

    val myResponse: MutableLiveData<Response<WeatherMain>> = MutableLiveData()
    val myResponse2: MutableLiveData<Response<WeatherMain>> = MutableLiveData()

    fun getPost() {
        viewModelScope.launch {
            val response = repository.getPost()
            myResponse.value = response
        }
    }

    fun getWeatherWithCityName(cidade: String) {
        viewModelScope.launch {
            val response = repository.getWeatherWithCityName(cidade)
            myResponse2.value = response
        }
    }
}