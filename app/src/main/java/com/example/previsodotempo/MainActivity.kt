package com.example.previsodotempo

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.previsodotempo.databinding.ActivityMainBinding

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
                binding.txtCityName.text = response.body().toString()
            }
        })
    }

    fun Activity.hideKeyboard() {
        hideKeyboard(currentFocus ?: View(this))
    }

    fun Context.hideKeyboard(view: View) {
        val inputMethodManager =
            getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
    }
}