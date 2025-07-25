package com.rifftyo.healingkuy.ui.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.rifftyo.core.domain.usecase.user.UserUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(val userUseCase: UserUseCase): ViewModel() {

    private val _profileName = MutableLiveData<String>()
    val profileName: LiveData<String> get() = _profileName

    private val _profileBytes = MutableLiveData<ByteArray>()
    val profileBytes: LiveData<ByteArray> get() = _profileBytes

    fun setProfileImage(name: String, bytes: ByteArray) {
        _profileName.value = name
        _profileBytes.value = bytes
    }

    val user = userUseCase.getUser().asLiveData()

    fun updateProfile(userName: String, profileName: String?, profileBytes: ByteArray?) =
        userUseCase.updateUser(userName, profileName, profileBytes).asLiveData()

    fun deleteUser() {
        viewModelScope.launch {
            userUseCase.deleteUser()
        }
    }
}