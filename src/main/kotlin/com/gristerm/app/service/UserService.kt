package com.gristerm.app.service

import com.gristerm.app.domain.UserModel
import com.gristerm.app.repository.UserRepository
import java.util.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service

@Service
class UserService(@Autowired val userRepository: UserRepository) {
  fun indexUsers(pageable: Pageable): Page<UserModel> {
    return userRepository.findAll(pageable)
  }

  fun retrieveUserById(id: String): Optional<UserModel> {
    return userRepository.findById(id)
  }

  fun deleteUserById(id: String) {
    if (userRepository.existsById(id)) {
      return userRepository.deleteById(id)
    }
    throw NoSuchElementException()
  }

  fun persistUser(userModel: UserModel): UserModel {
    return userRepository.save(userModel)
  }
}
