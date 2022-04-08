package com.gristerm.app.service

import com.gristerm.app.data.SignUpRequest
import com.gristerm.app.domain.UserModel
import com.gristerm.app.mapper.toUserModel
import com.gristerm.app.repository.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service

@Service
class AuthService {
    @Autowired lateinit var passwordEncoder: PasswordEncoder
    @Autowired lateinit var userRepository: UserRepository

    fun createUser(signUpRequest: SignUpRequest): UserModel {
        val userModel = signUpRequest.toUserModel()
        userModel.password = passwordEncoder.encode(userModel.password)
        userModel.lastModifiedDate = userModel.createdDate
        return userRepository.save(userModel)
    }
}
