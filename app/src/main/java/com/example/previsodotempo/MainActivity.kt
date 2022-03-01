package com.example.previsodotempo

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
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
        setLoadingLayout(true)
        val cidade: String = binding.edtCityName.text.toString()
        val repository = Repository()
        val viewModelFactory = MainViewModelFactory(repository)
        viewModel = ViewModelProvider(this, viewModelFactory).get(MainViewModel::class.java)
        viewModel.getWeatherWithCityName(cidade)
        viewModel.myResponse2.observe(this, Observer { response ->
            if (response.isSuccessful && response.body() != null) {
                Log.d(TAG, response.body().toString())
                setLayoutSuccess(true)

                val tempDouble = response.body()?.main?.temp
                val tempMaxDouble = response.body()?.main?.temp_max
                val tempMinDouble = response.body()?.main?.temp_min
                val humidityDouble = response.body()?.main?.humidity

                val tempInt = converter(tempDouble)
                val tempMaxInt = converter(tempMaxDouble)
                val tempMinInt = converter(tempMinDouble)
                val humidityInt = converter(humidityDouble)

                binding.success.txtCityName.text = response.body()?.name
                binding.success.txtActualTemperature.text = formaterToString(tempInt)
                binding.success.txtMaxTemperature.text = formaterToString(tempMaxInt)
                binding.success.txtMinTemperature.text = formaterToString(tempMinInt)
                binding.success.txtHumidity.text = formaterToStringPercenatge(humidityInt)

            }
        })
    }

    private fun setLoadingLayout(visibility: Boolean) {
        binding.loading.root.isVisible = visibility
        binding.success.root.isVisible = !visibility
        //binding.error.root.isVisible = !visibility

    }

    private fun setLayoutSuccess(visibility: Boolean) {
        binding.success.root.isVisible = visibility
        binding.loading.root.isVisible = !visibility
        //binding.error.root.isVisible = !visibility
    }
}