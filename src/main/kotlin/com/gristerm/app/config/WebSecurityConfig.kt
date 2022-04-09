package com.gristerm.app.config

import com.gristerm.app.mapper.toUserCredentials
import com.gristerm.app.repository.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.builders.WebSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.config.core.GrantedAuthorityDefaults
import org.springframework.security.config.http.SessionCreationPolicy.STATELESS
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import org.springframework.stereotype.Service

@Primary
@Service
class UserDetailsServiceImpl : UserDetailsService {
  @Autowired lateinit var userRepository: UserRepository

  fun loadUserById(id: String): UserDetails {
    return id.let { userRepository.findById(id).get().toUserCredentials() }
  }

  override fun loadUserByUsername(username: String): UserDetails {
    return username.let { userRepository.findByUsername(username).get().toUserCredentials() }
  }
}

@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
@EnableWebSecurity
class WebSecurityConfig : WebSecurityConfigurerAdapter() {
  @Autowired lateinit var authEntryPoint: AuthEntryPoint

  @Autowired lateinit var userDetailsService: UserDetailsService

  override fun configure(auth: AuthenticationManagerBuilder?) {
    auth?.userDetailsService(userDetailsService)?.passwordEncoder(passwordEncoder())
  }

  override fun configure(http: HttpSecurity?) {
    http
        ?.httpBasic { it.disable() }
        ?.csrf { it.disable() }
        ?.exceptionHandling { it.authenticationEntryPoint((authEntryPoint)) }
        ?.sessionManagement { it.sessionCreationPolicy(STATELESS) }
        ?.authorizeRequests { it.antMatchers("/auth/**").permitAll() }
        ?.authorizeRequests { it.anyRequest().authenticated() }
    http?.addFilterBefore(
        authenticationTokenFilterBean(), UsernamePasswordAuthenticationFilter::class.java)
  }

  override fun configure(web: WebSecurity?) {
    super.configure(web)
  }

  @Bean
  @Throws(Exception::class)
  fun authenticationTokenFilterBean(): AuthFilterConfig {
    return AuthFilterConfig()
  }

  @Bean
  @Throws(Exception::class)
  override fun authenticationManagerBean(): AuthenticationManager {
    return super.authenticationManagerBean()
  }

  @Bean
  fun passwordEncoder(): PasswordEncoder {
    return BCryptPasswordEncoder()
  }

  @Bean
  fun grantedAuthorityDefaults(): GrantedAuthorityDefaults? {
    return GrantedAuthorityDefaults("")
  }
}
