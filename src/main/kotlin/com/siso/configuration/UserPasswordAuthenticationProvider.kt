package com.siso.configuration

import com.siso.repository.UserRepository
import io.micronaut.http.HttpRequest
import io.micronaut.security.authentication.AuthenticationException
import io.micronaut.security.authentication.AuthenticationFailed
import io.micronaut.security.authentication.AuthenticationProvider
import io.micronaut.security.authentication.AuthenticationRequest
import io.micronaut.security.authentication.AuthenticationResponse
import jakarta.inject.Singleton
import org.reactivestreams.Publisher
import reactor.core.publisher.Flux
import reactor.core.publisher.FluxSink

@Singleton
class UserPasswordAuthenticationProvider (private val passwordEncoder: BCryptPasswordEncoderService,
                                          private val userRepository: UserRepository
                                          ): AuthenticationProvider {
    override fun authenticate(httpRequest: HttpRequest<*>?,
                              authenticationRequest: AuthenticationRequest<*, *>
    ): Publisher<AuthenticationResponse>? {

        var user = userRepository.findByEmail(authenticationRequest.identity as String).orElseThrow {
            throw AuthenticationException(AuthenticationFailed("Usuário não encontrado com e-mail: ${authenticationRequest.identity} informado."))
        }

        var userRoles = user.roles.map { it.name }

        return Flux.create({ emitter: FluxSink<AuthenticationResponse> ->
            if (authenticationRequest.identity == user.email &&
                passwordEncoder.matches(authenticationRequest.secret as String, user.password)) {
                emitter.next(AuthenticationResponse.success(authenticationRequest.identity as String, userRoles))
                emitter.complete()
            } else {
                emitter.error(AuthenticationResponse.exception())
            }
        }, FluxSink.OverflowStrategy.ERROR)
    }
}