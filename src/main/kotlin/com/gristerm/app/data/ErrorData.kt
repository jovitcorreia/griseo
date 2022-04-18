package com.gristerm.app.data

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonInclude
import java.time.LocalDateTime
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus
import org.springframework.web.context.request.ServletWebRequest

interface ResponseContract {
  val timestamp: LocalDateTime
  val remoteAddress: String
  val path: String
  val method: HttpMethod?
  val body: Any
}

@JsonInclude(JsonInclude.Include.NON_EMPTY)
data class ErrorResponse(val status: HttpStatus, @JsonIgnore val request: ServletWebRequest) :
    ResponseContract {
  override val timestamp: LocalDateTime = LocalDateTime.now()
  override val remoteAddress: String = request.request.remoteAddr
  override val path: String = request.request.requestURI
  override val method: HttpMethod? = request.httpMethod
  override val body: MutableMap<String, String?> = HashMap()
  override fun toString(): String {
    return "$remoteAddress got $status on $method $path with $body"
  }
}
