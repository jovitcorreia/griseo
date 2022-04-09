package com.gristerm.app.data

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonInclude
import java.time.LocalDateTime
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus
import org.springframework.web.context.request.ServletWebRequest

interface ResponseContract {
  val time: LocalDateTime
  val client: String
  val path: String
  val method: HttpMethod?
  val body: Any
}

@JsonInclude(JsonInclude.Include.NON_EMPTY)
data class ErrorResponse(val status: HttpStatus, @JsonIgnore val request: ServletWebRequest) :
    ResponseContract {
  override val time: LocalDateTime = LocalDateTime.now()
  override val client: String = request.request.remoteAddr
  override val path: String = request.request.requestURI
  override val method: HttpMethod? = request.httpMethod
  override val body: MutableMap<String, String?> = HashMap()
  override fun toString(): String {
    return "$client got $status on $method $path with $body"
  }
}
