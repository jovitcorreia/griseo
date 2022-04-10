package com.gristerm.app.controller

import com.gristerm.app.IntegrationTestSupport
import com.gristerm.app.repository.UserRepository
import com.gristerm.app.service.AuthService
import com.intuit.karate.junit5.Karate
import org.junit.jupiter.api.BeforeEach
import org.springframework.beans.factory.annotation.Autowired

class AuthControllerTest : IntegrationTestSupport() {
  @Autowired lateinit var userRepository: UserRepository

  @BeforeEach
  fun setup() {
    userRepository.deleteAll()
  }

  @Karate.Test
  fun scenario(): Karate? {
    return Karate.run("classpath:integration/auth-controller.feature")
  }
}
