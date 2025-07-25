package com.rifftyo.core.domain.usecase.destination

import com.rifftyo.core.data.Resource
import com.rifftyo.core.domain.model.Destinations
import kotlinx.coroutines.flow.Flow

interface DestinationUseCase {
    fun getPopularDestination(): Flow<Resource<List<Destinations>>>

    fun getDestinationsByCity(city: String): Flow<Resource<List<Destinations>>>

    fun getDetailDestination(id: String): Flow<Resource<Destinations>>

    fun postRate(destinationId: String, rating: Double): Flow<Resource<Double>>

    fun getRateDestination(destinationId: String): Flow<Resource<Double>>

    fun updateRate(destinationId: String, rating: Double): Flow<Resource<Double>>

    fun getDestinationBySearch(query: String): Flow<Resource<List<Destinations>>>

    fun getDestinationsByCategory(category: String): Flow<Resource<List<Destinations>>>

    fun getBookmarks(): Flow<Resource<List<Destinations>>>

    fun addBookmark(destinationId: String): Flow<Resource<String>>

    fun deleteBookmark(destinationId: String): Flow<Resource<String>>
}