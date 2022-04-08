package com.gristerm.app.controller

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonInclude.Include.NON_EMPTY
import com.gristerm.app.util.getDupKey
import io.jsonwebtoken.JwtException
import java.time.LocalDateTime
import lombok.ToString
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.dao.DuplicateKeyException
import org.springframework.http.HttpStatus
import org.springframework.http.HttpStatus.*
import org.springframework.validation.FieldError
import org.springframework.validation.ObjectError
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.context.request.ServletWebRequest

@JsonInclude(NON_EMPTY)
@ToString
data class ErrorResponse(@JsonIgnore val http: HttpStatus, @JsonIgnore val req: ServletWebRequest) {
  val time: LocalDateTime = LocalDateTime.now()
  val status = http.value()
  val error = http.reasonPhrase
  val path = req.request.requestURI
  val method = req.httpMethod
  val messages: MutableMap<String, String?> = HashMap()
}

@RestControllerAdvice
class ErrorController {
  companion object {
    private val log: Logger = LoggerFactory.getLogger(this::class.java)
  }

  @ExceptionHandler(MethodArgumentNotValidException::class)
  @ResponseStatus(BAD_REQUEST)
  fun handleBadRequest(ex: MethodArgumentNotValidException, req: ServletWebRequest): ErrorResponse {
    val response = ErrorResponse(BAD_REQUEST, req)
    ex.bindingResult.allErrors.forEach { error: ObjectError ->
      response.messages[(error as FieldError).field] = error.defaultMessage
    }
    log.warn(response.toString())
    return response
  }

  @ExceptionHandler(DuplicateKeyException::class)
  @ResponseStatus(CONFLICT)
  fun handleConflict(ex: DuplicateKeyException, req: ServletWebRequest): ErrorResponse {
    val response = ErrorResponse(CONFLICT, req)
    ex.message?.let { getDupKey(it) }?.let { response.messages.putAll(it) }
    log.warn(response.toString())
    return response
  }

  @ExceptionHandler(NoSuchElementException::class)
  @ResponseStatus(NOT_FOUND)
  fun handleNotFound(ex: NoSuchElementException, req: ServletWebRequest): ErrorResponse {
    val response = ErrorResponse(NOT_FOUND, req)
    log.warn(response.toString())
    return response
  }

  @ExceptionHandler(JwtException::class)
  @ResponseStatus(UNAUTHORIZED)
  fun handleUnauthorized(ex: JwtException, req: ServletWebRequest): ErrorResponse {
    val response = ErrorResponse(UNAUTHORIZED, req)
    ex.message?.let { response.messages.put("token", it) }
    log.warn(response.toString())
    return response
  }
}
