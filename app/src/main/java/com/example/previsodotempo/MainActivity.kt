package com.example.previsodotempo

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.previsodotempo.databinding.ActivityMainBinding
import com.example.previsodotempo.extensions.*

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
                binding.success.txtTemp.text = formaterTempToString(tempInt)
                binding.success.txtMaxTemp.text = formaterMaxTempToString(tempMaxInt)
                binding.success.txtMinTemp.text = formaterMinTempToString(tempMinInt)
                // binding.success.txtHumidity.text = formaterToStringPercenatge(humidityInt)

            } else {
                setErrorLayout(true)
            }
        })
    }

    private fun setLoadingLayout(visibility: Boolean) {
        binding.loading.root.isVisible = visibility
        binding.success.root.isVisible = !visibility
        binding.backgroundSuccess.isVisible = !visibility
        binding.error.root.isVisible = !visibility
    }

    private fun setErrorLayout(visibility: Boolean) {
        binding.loading.root.isVisible = !visibility
        binding.success.root.isVisible = !visibility
        binding.backgroundSuccess.isVisible = !visibility
        binding.error.root.isVisible = visibility
    }

    private fun setLayoutSuccess(visibility: Boolean) {
        binding.backgroundSuccess.isVisible = visibility
        binding.success.root.isVisible = visibility
        binding.loading.root.isVisible = !visibility
        binding.error.root.isVisible = !visibility
    }
}