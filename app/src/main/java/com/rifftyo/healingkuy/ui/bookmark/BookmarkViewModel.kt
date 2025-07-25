package com.rifftyo.healingkuy.ui.bookmark

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rifftyo.core.data.Resource
import com.rifftyo.core.domain.model.Destinations
import com.rifftyo.core.domain.usecase.destination.DestinationUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class BookmarkViewModel @Inject constructor(val destinationUseCase: DestinationUseCase): ViewModel() {

    private val _bookmarkDestinations = MutableLiveData<Resource<List<Destinations>>>()
    val bookmarkDestinations: LiveData<Resource<List<Destinations>>> = _bookmarkDestinations

    init {
        refreshBookmarks()
    }

    fun refreshBookmarks() {
        destinationUseCase.getBookmarks()
            .onEach { result ->
                _bookmarkDestinations.postValue(Resource.Loading())
                _bookmarkDestinations.postValue(result)
            }
            .launchIn(viewModelScope)
    }
}