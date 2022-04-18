package com.gristerm.app.controller

import com.gristerm.app.data.ErrorResponse
import com.gristerm.app.util.getDupKey
import io.jsonwebtoken.JwtException
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.dao.DuplicateKeyException
import org.springframework.http.HttpStatus.*
import org.springframework.security.access.AccessDeniedException as AuthenticationException
import org.springframework.validation.FieldError
import org.springframework.validation.ObjectError
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.context.request.ServletWebRequest

@RestControllerAdvice
class ErrorController {
  companion object {
    private val log: Logger = LoggerFactory.getLogger(this::class.java)
  }

  @ExceptionHandler(AuthenticationException::class)
  fun handleBadRequest(
      exception: AuthenticationException,
      request: ServletWebRequest
  ): ErrorResponse {
    val response: ErrorResponse
    if (exception.message?.contains("denied") == true) {
      response = ErrorResponse(FORBIDDEN, request)
      request.response?.status = FORBIDDEN.value()
    } else {
      response = ErrorResponse(UNAUTHORIZED, request)
      request.response?.status = UNAUTHORIZED.value()
    }
    exception.message?.let { response.body.put("user", it) }
    log.warn(response.toString())
    return response
  }

  @ExceptionHandler(MethodArgumentNotValidException::class)
  @ResponseStatus(BAD_REQUEST)
  fun handleBadRequest(
      exception: MethodArgumentNotValidException,
      request: ServletWebRequest
  ): ErrorResponse {
    val response = ErrorResponse(BAD_REQUEST, request)
    exception.bindingResult.allErrors.forEach { error: ObjectError ->
      response.body[(error as FieldError).field] = error.defaultMessage
    }
    log.warn(response.toString())
    return response
  }

  @ExceptionHandler(DuplicateKeyException::class)
  @ResponseStatus(CONFLICT)
  fun handleConflict(exception: DuplicateKeyException, request: ServletWebRequest): ErrorResponse {
    val response = ErrorResponse(CONFLICT, request)
    exception.message?.let { getDupKey(it) }?.let { response.body.putAll(it) }
    log.warn(response.toString())
    return response
  }

  @ExceptionHandler(NoSuchElementException::class)
  @ResponseStatus(NOT_FOUND)
  fun handleNotFound(exception: NoSuchElementException, request: ServletWebRequest): ErrorResponse {
    val response = ErrorResponse(NOT_FOUND, request)
    log.warn(response.toString())
    return response
  }

  @ExceptionHandler(JwtException::class)
  @ResponseStatus(UNAUTHORIZED)
  fun handleUnauthorized(exception: JwtException, request: ServletWebRequest): ErrorResponse {
    val response = ErrorResponse(UNAUTHORIZED, request)
    exception.message?.let { response.body.put("token", it) }
    log.warn(response.toString())
    return response
  }
}
