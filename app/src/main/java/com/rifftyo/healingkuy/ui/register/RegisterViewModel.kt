package com.rifftyo.healingkuy.ui.register

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.rifftyo.core.domain.usecase.user.UserUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(private val userUseCase: UserUseCase) : ViewModel() {

    private val _profileName = MutableLiveData<String>()
    val profileName: LiveData<String> get() = _profileName

    private val _profileBytes = MutableLiveData<ByteArray>()
    val profileBytes: LiveData<ByteArray> get() = _profileBytes

    fun setProfileImage(name: String, bytes: ByteArray) {
        _profileName.value = name
        _profileBytes.value = bytes
    }

    fun register(username: String, email: String, password: String) = userUseCase.register(username, email, password, _profileName.value ?: "", _profileBytes.value ?: ByteArray(0)).asLiveData()
}