package com.rifftyo.healingkuy.ui.home

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.rifftyo.core.data.Resource
import com.rifftyo.core.domain.model.City
import com.rifftyo.core.ui.CityAdapter
import com.rifftyo.core.ui.CityDestinationsAdapter
import com.rifftyo.core.ui.PopularDestinationsAdapter
import com.rifftyo.healingkuy.databinding.FragmentHomeBinding
import com.rifftyo.healingkuy.ui.category.CategoryActivity
import com.rifftyo.healingkuy.ui.detail.DetailActivity
import com.rifftyo.healingkuy.ui.login.LoginActivity
import dagger.hilt.android.AndroidEntryPoint
import java.util.Locale

@Suppress("DEPRECATION")
@AndroidEntryPoint
class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val viewModel: HomeViewModel by viewModels()

    private lateinit var fusedLocationClient: FusedLocationProviderClient

    private var userDone = false
    private var popularDone = false
    private var cityDone = false

    private val permissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            getUserCity()
        } else {
            viewModel.setCity("Jakarta")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.main.visibility = View.GONE
        binding.progressBar.visibility = View.VISIBLE

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())

        getUser()
        cityRecyclerView()
        popularDestinationsRecyclerView()
        cityDestinationsRecyclerView()

        getUserCity()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun checkAllDataLoaded() {
        if (userDone && popularDone && cityDone) {
            binding.progressBar.visibility = View.GONE
            binding.main.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.VISIBLE
            binding.main.visibility = View.GONE
        }
    }

    private fun getUser() {
        viewModel.user.observe(viewLifecycleOwner) { resource ->
            when (resource) {
                is Resource.Loading -> {
                    userDone = false
                }

                is Resource.Success -> {
                    userDone = true
                    binding.tvUsername.text = resource.data?.username
                    Glide.with(this)
                        .load(resource.data?.profile)
                        .into(binding.imgProfile)
                    checkAllDataLoaded()
                }

                is Resource.Error -> {
                    userDone = true

                    if (resource.message?.contains("expired", true) == true ||
                        resource.message?.contains("invalid", true) == true ||
                        resource.message?.contains("unauthorized", true) == true
                    ) {
                        val intent = Intent(requireContext(), LoginActivity::class.java)
                        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                        startActivity(intent)
                    } else {
                        Toast.makeText(context, resource.message, Toast.LENGTH_SHORT).show()
                    }

                    checkAllDataLoaded()
                }
            }
        }
    }

    private fun cityRecyclerView() {
        val cityAdapter = CityAdapter()

        val cityList = listOf(
            City(
                "Bandung",
                "https://teyeitfftfhsydazinuy.supabase.co/storage/v1/object/public/city//bandung-icon.webp"
            ),
            City(
                "Bogor",
                "https://teyeitfftfhsydazinuy.supabase.co/storage/v1/object/public/city//bogor-icon.jpg"
            ),
            City(
                "Jakarta",
                "https://teyeitfftfhsydazinuy.supabase.co/storage/v1/object/public/city//jakarta-icon.webp"
            ),
            City(
                "Purwokerto",
                "https://teyeitfftfhsydazinuy.supabase.co/storage/v1/object/public/city//purwokerto-icon.webp"
            ),
            City(
                "Sleman",
                "https://teyeitfftfhsydazinuy.supabase.co/storage/v1/object/public/city//sleman-icon.jpg"
            ),
            City(
                "Surabaya",
                "https://teyeitfftfhsydazinuy.supabase.co/storage/v1/object/public/city//surabaya-icon.png"
            ),
            City(
                "Yogyakarta",
                "https://teyeitfftfhsydazinuy.supabase.co/storage/v1/object/public/city//yogyakarta-icon.png"
            )
        )

        with(binding) {
            rvCity.layoutManager =
                LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            rvCity.setHasFixedSize(true)
            rvCity.adapter = cityAdapter
        }

        cityAdapter.submitList(cityList)

        cityAdapter.onItemClick = { selectedData ->
            val intent = Intent(context, CategoryActivity::class.java)
            intent.putExtra(CategoryActivity.EXTRA_CITY, selectedData.name)
            intent.putExtra(CategoryActivity.TYPE, "City")
            startActivity(intent)
        }
    }

    private fun popularDestinationsRecyclerView() {
        val popularDestinationsAdapter = PopularDestinationsAdapter()

        with(binding) {
            rvPopular.layoutManager =
                LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            rvPopular.setHasFixedSize(true)
            rvPopular.adapter = popularDestinationsAdapter
        }

        viewModel.popularDestinations.observe(viewLifecycleOwner) { resource ->
            when (resource) {
                is Resource.Loading -> {
                    popularDone = false
                }

                is Resource.Success -> {
                    popularDone = true
                    popularDestinationsAdapter.submitList(resource.data)
                    checkAllDataLoaded()
                }

                is Resource.Error -> {
                    popularDone = true
                    Toast.makeText(context, resource.message, Toast.LENGTH_SHORT).show()
                    checkAllDataLoaded()
                }
            }
        }

        popularDestinationsAdapter.onItemClick = { selectedData ->
            val intent = Intent(context, DetailActivity::class.java)
            intent.putExtra(DetailActivity.EXTRA_ID, selectedData.id)
            startActivity(intent)
        }
    }

    private fun cityDestinationsRecyclerView() {
        val cityDestinationsAdapter = CityDestinationsAdapter()

        with(binding) {
            rvNearby.layoutManager =
                LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            rvNearby.adapter = cityDestinationsAdapter
        }

        viewModel.cityDestinations.observe(viewLifecycleOwner) { resource ->
            when (resource) {
                is Resource.Loading -> {
                    cityDone = false
                }
                is Resource.Success -> {
                    cityDone = true
                    if (resource.data.isNullOrEmpty()) {
                        binding.imgNotFound.visibility = View.VISIBLE
                        binding.tvNotFound.visibility = View.VISIBLE
                        binding.rvNearby.visibility = View.GONE
                    } else {
                        cityDestinationsAdapter.submitList(resource.data)
                    }
                    checkAllDataLoaded()
                }
                is Resource.Error -> {
                    cityDone = true
                    Toast.makeText(context, resource.message, Toast.LENGTH_SHORT).show()
                    checkAllDataLoaded()
                }
            }
        }

        cityDestinationsAdapter.onItemClick = { selectedData ->
            val intent = Intent(context, DetailActivity::class.java)
            intent.putExtra(DetailActivity.EXTRA_ID, selectedData.id)
            startActivity(intent)
        }
    }

    private fun getUserCity() {
        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            permissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
            return
        }

        fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
            if (location != null) {
                val geocoder = Geocoder(requireContext(), Locale.getDefault())

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    geocoder.getFromLocation(
                        location.latitude,
                        location.longitude,
                        1,
                        object : Geocoder.GeocodeListener {
                            override fun onGeocode(addresses: MutableList<Address>) {
                                val cityName = addresses.firstOrNull()?.subAdminArea
                                    ?: addresses.firstOrNull()?.locality
                                    ?: addresses.firstOrNull()?.adminArea
                                    ?: "Jakarta"

                                val cleanCityName = cleanCityName(cityName)
                                requireActivity().runOnUiThread {
                                    viewModel.setCity(cleanCityName)
                                    setCityName(cleanCityName)
                                }
                            }

                            override fun onError(errorMessage: String?) {
                                Log.e("Geocoder", "Error: $errorMessage")
                                requireActivity().runOnUiThread {
                                    fallbackToJakarta()
                                }
                            }
                        }
                    )
                } else {
                    try {
                        val addresses = geocoder.getFromLocation(location.latitude, location.longitude, 1)
                        val cityName = addresses?.firstOrNull()?.subAdminArea
                            ?: addresses?.firstOrNull()?.locality
                            ?: addresses?.firstOrNull()?.adminArea
                            ?: "Jakarta"

                        val cleanCityName = cleanCityName(cityName)
                        viewModel.setCity(cleanCityName)
                        setCityName(cleanCityName)
                    } catch (e: Exception) {
                        Log.e("Geocoder", "Exception: ${e.message}")
                        fallbackToJakarta()
                    }
                }
            } else {
                fallbackToJakarta()
            }
        }.addOnFailureListener {
            Log.e("Location", "Failed to get last location: ${it.message}")
            fallbackToJakarta()
        }
    }

    private fun fallbackToJakarta() {
        val fallbackCity = "Jakarta"
        viewModel.setCity(fallbackCity)
        setCityName(fallbackCity)
    }


    private fun cleanCityName(rawName: String?): String {
        return rawName
            ?.replace("Kota ", "", ignoreCase = true)
            ?.replace("Kabupaten ", "", ignoreCase = true)
            ?.replace(" City", "", ignoreCase = true)
            ?.replace(" Regency", "", ignoreCase = true)
            ?.replace(" Municipality", "", ignoreCase = true)
            ?.trim()
            ?: "Jakarta"
    }

    private fun setCityName(name: String) {
        binding.tvCity.text = name
    }
}