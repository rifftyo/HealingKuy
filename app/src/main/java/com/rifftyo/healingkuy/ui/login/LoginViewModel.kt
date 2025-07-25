package com.rifftyo.healingkuy.ui.login

import androidx.lifecycle.ViewModel
import com.rifftyo.core.domain.usecase.user.UserUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(private val userUseCase: UserUseCase) : ViewModel() {

    fun login(email: String, password: String) = userUseCase.login(email, password)
}