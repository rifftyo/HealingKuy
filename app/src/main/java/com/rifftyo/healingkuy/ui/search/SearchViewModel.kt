package com.rifftyo.healingkuy.ui.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.rifftyo.core.data.Resource
import com.rifftyo.core.domain.model.Category
import com.rifftyo.core.domain.model.City
import com.rifftyo.core.domain.model.Destinations
import com.rifftyo.core.domain.usecase.destination.DestinationUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import javax.inject.Inject

@OptIn(FlowPreview::class)
@HiltViewModel
class SearchViewModel @Inject constructor(private val destinationUseCase: DestinationUseCase): ViewModel() {

    private val _categories = MutableLiveData<List<Category>>()
    val categories: LiveData<List<Category>> get() = _categories

    private val _cities = MutableLiveData<List<City>>()
    val cities: LiveData<List<City>> get() = _cities

    private val _queryChannel = MutableStateFlow("")
    val queryChannel: MutableStateFlow<String> = _queryChannel

    private val _searchResult = MutableStateFlow<Resource<List<Destinations>>>(Resource.Success(emptyList()))
    val searchResultLiveData = _searchResult.asLiveData()

    init {
        if (_categories.value.isNullOrEmpty()) {
            val allCategories = listOf(
                Category("Alam", "https://teyeitfftfhsydazinuy.supabase.co/storage/v1/object/public/category//alam-category.webp"),
                Category("Budaya", "https://teyeitfftfhsydazinuy.supabase.co/storage/v1/object/public/category//budaya-category.jpeg"),
                Category("Religi", "https://teyeitfftfhsydazinuy.supabase.co/storage/v1/object/public/category//religi-category.webp"),
                Category("Sejarah", "https://teyeitfftfhsydazinuy.supabase.co/storage/v1/object/public/category//sejarah-category.webp"),
                Category("Edukasi", "https://teyeitfftfhsydazinuy.supabase.co/storage/v1/object/public/category//edukasi-category.webp"),
                Category("Petualangan", "https://teyeitfftfhsydazinuy.supabase.co/storage/v1/object/public/category//petualang-category.jpg"),
                Category("Pantai", "https://teyeitfftfhsydazinuy.supabase.co/storage/v1/object/public/category//pantai-category.webp")
            )

            _categories.value = allCategories.shuffled()
        }

        if (_cities.value.isNullOrEmpty()) {
            _cities.value = listOf(
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
        }

        viewModelScope.launch {
            queryChannel
                .debounce(300)
                .distinctUntilChanged()
                .collectLatest { query ->
                    if (query.isNotBlank()) {
                        destinationUseCase.getDestinationBySearch(query).collect {
                            _searchResult.value = it
                        }
                    }
                }
        }
    }
}