package com.rifftyo.healingkuy.ui.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.rifftyo.core.domain.usecase.destination.DestinationUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(private val destinationUseCase: DestinationUseCase) : ViewModel() {

    fun detailDestination(id: String) = destinationUseCase.getDetailDestination(id).asLiveData()

    fun getRateDestination(destinationId: String) = destinationUseCase.getRateDestination(destinationId).asLiveData()

    fun postRateDestination(destinationId: String, rating: Double) = destinationUseCase.postRate(destinationId, rating).asLiveData()

    fun putRateDestination(destinationId: String, rating: Double) = destinationUseCase.updateRate(destinationId, rating).asLiveData()

    fun addBookmark(destinationId: String) = destinationUseCase.addBookmark(destinationId).asLiveData()

    fun deleteBookmark(destinationId: String) = destinationUseCase.deleteBookmark(destinationId).asLiveData()
}