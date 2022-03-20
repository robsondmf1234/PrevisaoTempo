package com.example.previsodotempo

import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.previsodotempo.databinding.ActivityMainBinding
import com.example.previsodotempo.extensions.*
import com.example.previsodotempo.model.WeatherMain
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import retrofit2.Response

const val TAG = "Response"

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: MainViewModel
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupListener()
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
    }

    private fun setupListener() {
        binding.iconGetLocation.setOnClickListener {
            fetchLocation()
            Toast.makeText(applicationContext, "Imagem clicada", Toast.LENGTH_SHORT).show()
        }
        binding.btnSearch.setOnClickListener {
            searchWeatherByCityName()
            hideKeyboard()
        }
    }

    private fun fetchLocation() {

        val task = fusedLocationProviderClient.lastLocation

        if (ActivityCompat.checkSelfPermission(
                this,
                android.Manifest.permission.ACCESS_FINE_LOCATION
            )
            != PackageManager.PERMISSION_GRANTED && ActivityCompat
                .checkSelfPermission(
                    this,
                    android.Manifest.permission.ACCESS_COARSE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),
                101
            )
            return
        }
        task.addOnSuccessListener {
            if (it != null) {
                Log.d(TAG, "Location ${it.latitude}")
                Toast.makeText(
                    applicationContext,
                    "Location is showing ${it.latitude}",
                    Toast.LENGTH_SHORT
                ).show()
            }
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
                if (response.body()?.weather != null) {
                    setIconOnImageView(response)
                }

                val tempInt = converter(tempDouble)
                val tempMaxInt = converter(tempMaxDouble)
                val tempMinInt = converter(tempMinDouble)
                val humidityInt = converter(humidityDouble)

                binding.success.txtCityName.text = response.body()?.name
                binding.success.txtTemp.text = formaterTempToString(tempInt)
                binding.success.txtMaxTemp.text = formaterMaxTempToString(tempMaxInt)
                binding.success.txtMinTemp.text = formaterMinTempToString(tempMinInt)

            } else {
                setErrorLayout(true)
            }
        })
    }

    private fun setIconOnImageView(response: Response<WeatherMain>) {
        when (response.body()?.weather!!.first().icon) {
            "04d" -> setImageIcon(R.drawable.icon_nublado_com_sol)
            "04n" -> setImageIcon(R.drawable.icon_nublado_sem_sol)
            "10d" -> setImageIcon(R.drawable.icon_chuva_com_sol)
            "11d" -> setImageIcon(R.drawable.icon_chuva)
            else -> setImageIcon(R.drawable.icon_unknow)
        }
    }

    private fun setImageIcon(icon: Int) {
        binding.success.imageIcon.setBackgroundResource(icon)
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