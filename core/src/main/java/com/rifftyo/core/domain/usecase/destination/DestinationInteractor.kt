package com.rifftyo.core.domain.usecase.destination

import com.rifftyo.core.data.Resource
import com.rifftyo.core.domain.model.Destinations
import com.rifftyo.core.domain.repository.IDestinationRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class DestinationInteractor @Inject constructor(private val destinationRepository: IDestinationRepository) : DestinationUseCase {

    override fun getPopularDestination(): Flow<Resource<List<Destinations>>> = destinationRepository.getPopularDestinations()

    override fun getDestinationsByCity(city: String): Flow<Resource<List<Destinations>>> = destinationRepository.getDestinationsByCity(city)

    override fun getDetailDestination(id: String): Flow<Resource<Destinations>> = destinationRepository.getDetailDestination(id)

    override fun postRate(destinationId: String, rating: Double): Flow<Resource<Double>> = destinationRepository.postRate(destinationId, rating)

    override fun getRateDestination(destinationId: String): Flow<Resource<Double>> = destinationRepository.getRateDestination(destinationId)

    override fun updateRate(destinationId: String, rating: Double): Flow<Resource<Double>> = destinationRepository.updateRate(destinationId, rating)

    override fun getDestinationBySearch(query: String): Flow<Resource<List<Destinations>>> = destinationRepository.getDestinationBySearch(query)

    override fun getDestinationsByCategory(category: String): Flow<Resource<List<Destinations>>> = destinationRepository.getDestinationsByCategory(category)

    override fun getBookmarks(): Flow<Resource<List<Destinations>>> = destinationRepository.getBookmarks()

    override fun addBookmark(destinationId: String): Flow<Resource<String>> = destinationRepository.addBookmark(destinationId)

    override fun deleteBookmark(destinationId: String): Flow<Resource<String>> = destinationRepository.deleteBookmark(destinationId)
}