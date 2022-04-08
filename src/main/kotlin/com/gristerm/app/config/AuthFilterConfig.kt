package com.gristerm.app.config

import io.jsonwebtoken.JwtException
import io.jsonwebtoken.io.IOException
import lombok.extern.slf4j.Slf4j
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter
import org.springframework.web.servlet.HandlerExceptionResolver
import javax.servlet.FilterChain
import javax.servlet.ServletException
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Component
@Slf4j
class AuthFilterConfig : OncePerRequestFilter() {
    @Autowired lateinit var tokenProvider: TokenProvider

    @Autowired lateinit var userDetails: UserDetailsServiceImpl

    @Autowired
    @Qualifier("handlerExceptionResolver")
    lateinit var exceptionResolver: HandlerExceptionResolver

    @Throws(IOException::class, ServletException::class)
    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        chain: FilterChain
    ) {
        try {
            var token: String? = request.getHeader("Authorization")
            if (token != null && token.startsWith("Bearer ")) {
                token = token.substring(7)
                tokenProvider.validate(token).let {
                    val user: UserDetails =
                        tokenProvider.subject(token).let { id ->
                            userDetails.loadUserById(id.orEmpty())
                        }
                    val auth =
                        UsernamePasswordAuthenticationToken(
                            user,
                            null,
                            user.authorities ?: throw SecurityException()
                        )
                    auth.details = WebAuthenticationDetailsSource().buildDetails(request)
                    SecurityContextHolder.getContext().authentication = auth
                }
            }
            chain.doFilter(request, response)
        } catch (ex: RuntimeException) {
            exceptionResolver.resolveException(
                request,
                response,
                null,
                JwtException(if (ex is JwtException) ex.message else "is invalid or nonexistent")
            )
        }
    }
}
