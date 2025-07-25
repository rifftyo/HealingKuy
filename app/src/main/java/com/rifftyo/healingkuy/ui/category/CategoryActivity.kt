package com.rifftyo.healingkuy.ui.category

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.rifftyo.core.data.Resource
import com.rifftyo.core.ui.CityDestinationsAdapter
import com.rifftyo.healingkuy.databinding.ActivityCategoryBinding
import com.rifftyo.healingkuy.ui.detail.DetailActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CategoryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCategoryBinding

    private val viewModel: CategoryViewModel by viewModels()

    companion object {
        const val EXTRA_CITY = "extra_city"
        const val TYPE = "type"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityCategoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val city = intent.getStringExtra(EXTRA_CITY)
        val type = intent.getStringExtra(TYPE)
        if (city != null) {
            if (type == "City") {
                getDestinationsByCity(city)
            } else {
                getDestinationsByCategory(city)
            }
        }
    }

    private fun getDestinationsByCity(city: String) {
        val cityAdapter = CityDestinationsAdapter()

        with(binding) {
            rvCity.layoutManager =
                LinearLayoutManager(this@CategoryActivity, LinearLayoutManager.VERTICAL, false)
            rvCity.setHasFixedSize(false)
            rvCity.isNestedScrollingEnabled = false
            rvCity.adapter = cityAdapter
        }

        viewModel.getDestinationsByCity(city).observe(this) { resource ->
            when (resource) {
                is Resource.Loading -> {
                    binding.progressBar.visibility = View.VISIBLE
                }
                is Resource.Success -> {
                    binding.progressBar.visibility = View.GONE
                    binding.tvTitle.text = "Temukan Destinasi Wisata di $city"
                    cityAdapter.submitList(resource.data)
                }
                is Resource.Error -> {
                    binding.progressBar.visibility = View.GONE
                    Toast.makeText(this, resource.message, Toast.LENGTH_SHORT).show()
                }
            }
        }

        cityAdapter.onItemClick = { selectedData ->
            val intent = Intent(this, DetailActivity::class.java)
            intent.putExtra(DetailActivity.EXTRA_ID, selectedData.id)
            startActivity(intent)
        }
    }

    private fun getDestinationsByCategory(category: String) {
        val cityAdapter = CityDestinationsAdapter()

        with(binding) {
            rvCity.layoutManager =
                LinearLayoutManager(this@CategoryActivity, LinearLayoutManager.VERTICAL, false)
            rvCity.setHasFixedSize(false)
            rvCity.isNestedScrollingEnabled = false
            rvCity.adapter = cityAdapter
        }

        viewModel.getDestinationsByCategory(category).observe(this) { resource ->
            when (resource) {
                is Resource.Loading -> {
                    binding.progressBar.visibility = View.VISIBLE
                }
                is Resource.Success -> {
                    binding.progressBar.visibility = View.GONE
                    binding.tvTitle.text = "Temukan Destinasi Wisata $category"
                    cityAdapter.submitList(resource.data)
                }
                is Resource.Error -> {
                    binding.progressBar.visibility = View.GONE
                    Toast.makeText(this, resource.message, Toast.LENGTH_SHORT).show()
                }
            }
        }

        cityAdapter.onItemClick = { selectedData ->
            val intent = Intent(this, DetailActivity::class.java)
            intent.putExtra(DetailActivity.EXTRA_ID, selectedData.id)
            startActivity(intent)
        }
    }
}