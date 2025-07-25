package com.rifftyo.core.domain.usecase.user

import com.rifftyo.core.domain.repository.IUserRepository
import javax.inject.Inject

class UserInteractor @Inject constructor(private val userRepository: IUserRepository): UserUseCase {

    override fun register(username: String, email: String, password: String, profileName: String, profileBytes: ByteArray) = userRepository.register(username, email, password, profileName, profileBytes)

    override fun login(emailOrUsername: String, password: String) = userRepository.login(emailOrUsername, password)

    override fun getUser() = userRepository.getUser()

    override fun updateUser(username: String, profileName: String?, profileBytes: ByteArray?) = userRepository.updateUser(username, profileName, profileBytes)

    override suspend fun deleteUser() = userRepository.deleteUser()
}