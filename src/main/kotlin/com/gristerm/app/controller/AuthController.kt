package com.gristerm.app.controller

import com.gristerm.app.config.TokenProvider
import com.gristerm.app.data.SignInRequest
import com.gristerm.app.data.SignUpRequest
import com.gristerm.app.data.TokenResponse
import com.gristerm.app.data.UserResponse
import com.gristerm.app.mapper.toUserResponse
import com.gristerm.app.service.AuthService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*

@CrossOrigin(origins = ["*"])
@RestController
@RequestMapping("/auth")
class AuthController {
  @Autowired lateinit var authService: AuthService
  @Autowired lateinit var authenticationManager: AuthenticationManager
  @Autowired lateinit var tokenProvider: TokenProvider

  @PostMapping("/login")
  fun signIn(@RequestBody @Validated signInRequest: SignInRequest): ResponseEntity<TokenResponse> {
    val authentication =
        authenticationManager.authenticate(
            UsernamePasswordAuthenticationToken(signInRequest.username, signInRequest.password))
    SecurityContextHolder.getContext().authentication = authentication
    val token: String = tokenProvider.generate(authentication)
    return ResponseEntity.status(HttpStatus.OK).body(TokenResponse(token))
  }

  @PostMapping("/signup")
  fun signUp(@RequestBody @Validated signUpRequest: SignUpRequest): ResponseEntity<UserResponse> {
    val userModel = authService.createUser(signUpRequest)
    return ResponseEntity.status(HttpStatus.OK).body(userModel.toUserResponse())
  }
}
