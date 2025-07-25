package com.rifftyo.healingkuy.ui.home

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.switchMap
import com.rifftyo.core.domain.usecase.destination.DestinationUseCase
import com.rifftyo.core.domain.usecase.user.UserUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(userUseCase: UserUseCase, private val destinationUseCase: DestinationUseCase ): ViewModel() {

    val user = userUseCase.getUser().asLiveData()

    val popularDestinations = destinationUseCase.getPopularDestination().asLiveData()

    private val _selectedCity = MutableLiveData<String>()

    val cityDestinations = _selectedCity.switchMap { city ->
        destinationUseCase.getDestinationsByCity(city).asLiveData()
    }

    fun setCity(city: String) {
        if (_selectedCity.value != city) {
            _selectedCity.value = city
        }
    }
}