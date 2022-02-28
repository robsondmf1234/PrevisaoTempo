package com.example.previsodotempo

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.previsodotempo.databinding.ActivityMainBinding
import com.example.previsodotempo.extensions.converter
import com.example.previsodotempo.extensions.formaterToString
import com.example.previsodotempo.extensions.formaterToStringPercenatge
import com.example.previsodotempo.extensions.hideKeyboard

const val TAG = "Response"

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupListener()
    }

    private fun setupListener() {
        binding.btnSearch.setOnClickListener {
            searchWeatherByCityName()
            hideKeyboard()
        }
    }

    private fun searchWeatherByCityName() {
        Toast.makeText(this, "Teste", Toast.LENGTH_SHORT).show()
        val cidade: String = binding.edtCityName.text.toString()
        val repository = Repository()
        val viewModelFactory = MainViewModelFactory(repository)
        viewModel = ViewModelProvider(this, viewModelFactory).get(MainViewModel::class.java)
        viewModel.getWeatherWithCityName(cidade)
        viewModel.myResponse2.observe(this, Observer { response ->
            if (response.isSuccessful && response.body() != null) {
                Log.d(TAG, response.body().toString())

                val tempDouble = response.body()?.main?.temp
                val tempMaxDouble = response.body()?.main?.temp_max
                val tempMinDouble = response.body()?.main?.temp_min
                val humidityDouble = response.body()?.main?.humidity

                val tempInt = converter(tempDouble)
                val tempMaxInt = converter(tempMaxDouble)
                val tempMinInt = converter(tempMinDouble)
                val humidityInt = converter(humidityDouble)

                binding.txtCityName.text = response.body()?.name
                binding.txtActualTemperature.text = formaterToString(tempInt)
                binding.txtMaxTemperature.text = formaterToString(tempMaxInt)
                binding.txtMinTemperature.text = formaterToString(tempMinInt)
                binding.txtHumidity.text = formaterToStringPercenatge(humidityInt)

            }
        })
    }
}