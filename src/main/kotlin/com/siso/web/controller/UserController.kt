package com.siso.web.controller

import com.siso.exception.ConflictException
import com.siso.model.entity.RolesConstants.ROLE_ADMIN
import com.siso.model.entity.RolesConstants.ROLE_USER
import com.siso.model.entity.toUserResponse
import com.siso.service.UserService
import com.siso.web.dto.request.UserPasswordResetRequest
import com.siso.web.dto.request.UserRequest
import com.siso.web.dto.response.UserResponse
import io.micronaut.core.annotation.Nullable
import io.micronaut.http.HttpResponse
import io.micronaut.http.annotation.Body
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Delete
import io.micronaut.http.annotation.Patch
import io.micronaut.http.annotation.Post
import io.micronaut.security.annotation.Secured
import io.micronaut.security.rules.SecurityRule
import io.micronaut.validation.Validated
import org.slf4j.LoggerFactory
import java.security.Principal
import javax.validation.Valid

@Validated
@Controller("/users")
@Secured(SecurityRule.IS_AUTHENTICATED)
class UserController(private val userService: UserService) {

    private val log = LoggerFactory.getLogger(this::class.java)

    @Post
    @Secured(ROLE_ADMIN)
    fun createUser(@Body @Valid userRequest: UserRequest): HttpResponse<UserResponse> {
        log.info("Criando usuário com email: ${userRequest.email}")
        val user = userService.findByEmail(userRequest.email)

        if(user.isPresent) {
            log.warn("Usuário com email: ${userRequest.email} já existente.")
            throw ConflictException("Usuário já existente com e-mail: ${userRequest.email}.")
        }

        val savedUser = userService.save(userRequest)
        log.info("Usuário email ${savedUser.email} criado com sucesso")
        return HttpResponse.created(toUserResponse(savedUser))
    }

    @Patch("/password")
    @Secured(ROLE_ADMIN, ROLE_USER)
    fun updatePassword(@Body @Valid userPasswordResetRequest: UserPasswordResetRequest,
                       @Nullable principal: Principal): HttpResponse<Any> {
        log.info("Buscando usuário com email: ${principal.name} para alterar a senha")

        val user = userService.findByEmail(principal.name)
        if(user.isPresent) {
            userService.update(user.get(), userPasswordResetRequest)
            return HttpResponse.ok()
        }
        return HttpResponse.unauthorized()
    }

    @Delete("/{id}")
    @Secured(ROLE_ADMIN)
    fun deleteUser(id: Long): HttpResponse<Any>{
        userService.deleteById(id)
        return HttpResponse.noContent()
    }

}