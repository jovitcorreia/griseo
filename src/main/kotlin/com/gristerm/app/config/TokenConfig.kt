package com.gristerm.app.config

import com.gristerm.app.domain.UserCredentials
import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jws
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import java.lang.System.currentTimeMillis
import java.security.Key
import java.util.*
import java.util.stream.Collectors
import javax.crypto.spec.SecretKeySpec
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.core.Authentication
import org.springframework.security.core.GrantedAuthority
import org.springframework.stereotype.Component

fun key(secret: String): Key {
  return SecretKeySpec(secret.encodeToByteArray(), SignatureAlgorithm.HS512.jcaName)
}

@Component
class TokenProvider(
    @Value("\${jwt.secret}") val secret: String,
    @Value("\${jwt.expiration}") val expiration: Long = 0
) {
  fun generate(authentication: Authentication): String {
    val user = authentication.principal as UserCredentials
    val roles =
        user.authorities
            .stream()
            .map { obj: GrantedAuthority -> obj.authority }
            .collect(Collectors.joining(","))
    return Jwts.builder()
        .setSubject(user.id)
        .claim("roles", roles)
        .claim("username", user.username)
        .setIssuedAt(Date())
        .setExpiration(Date(currentTimeMillis().plus(expiration.times(1000))))
        .signWith(key(secret))
        .compact()
  }

  fun subject(token: String?): String? {
    return Jwts.parserBuilder()
        .setSigningKey(key(secret))
        .build()
        .parseClaimsJws(token)
        .body
        .subject
  }

  fun validate(token: String?): Jws<Claims>? {
    return Jwts.parserBuilder().setSigningKey(key(secret)).build().parseClaimsJws(token)
  }
}
