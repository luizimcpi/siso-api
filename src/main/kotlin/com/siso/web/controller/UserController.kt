package com.siso.web.controller

import com.siso.model.entity.toUserResponse
import com.siso.service.UserService
import com.siso.web.dto.request.UserRequest
import com.siso.web.dto.response.UserResponse
import io.micronaut.http.HttpResponse
import io.micronaut.http.MediaType
import io.micronaut.http.annotation.Body
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import io.micronaut.http.annotation.Post
import io.micronaut.http.annotation.Produces
import io.micronaut.security.annotation.Secured
import io.micronaut.security.authentication.Authentication
import io.micronaut.security.authentication.AuthenticationException
import io.micronaut.security.authentication.AuthenticationFailed
import io.micronaut.security.rules.SecurityRule
import io.micronaut.validation.Validated
import org.slf4j.LoggerFactory
import javax.validation.Valid

@Validated
@Controller("/users")
@Secured(SecurityRule.IS_AUTHENTICATED)
class UserController(private val userService: UserService) {

    private val log = LoggerFactory.getLogger(this::class.java)

    @Post
    @Secured("ROLE_ADMIN")
    fun createUser(@Body @Valid userRequest: UserRequest): HttpResponse<UserResponse> {
        log.info("Criando usuário com email: ${userRequest.email}")
        val user = userService.findByEmail(userRequest.email)

        if(user!!.isPresent) {
            log.warn("Usuário com email: ${userRequest.email} já existente.")
            throw AuthenticationException(AuthenticationFailed("Usuário já existente com e-mail: ${userRequest.email}."))
        }

        val savedUser = userService.save(userRequest)
        log.info("Usuário email ${savedUser.email} criado com sucesso")
        return HttpResponse.created(toUserResponse(savedUser))
    }

    @Produces(MediaType.TEXT_PLAIN)
    @Get("/authenticated")
    fun authenticated(authentication: Authentication): String? {
        return authentication.name + " is authenticated"
    }
}