package com.rifftyo.healingkuy.ui.category

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.rifftyo.core.domain.usecase.destination.DestinationUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class CategoryViewModel @Inject constructor(private val destinationUseCase: DestinationUseCase): ViewModel() {
    fun getDestinationsByCity(city: String) = destinationUseCase.getDestinationsByCity(city).asLiveData()

    fun getDestinationsByCategory(category: String) = destinationUseCase.getDestinationsByCategory(category).asLiveData()
}