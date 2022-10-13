package com.siso.web.controller

import com.siso.model.entity.RolesConstants
import com.siso.service.CustomerService
import com.siso.service.UserService
import com.siso.web.dto.request.CustomerRequest
import com.siso.web.dto.response.CustomerResponse
import io.micronaut.core.annotation.Nullable
import io.micronaut.http.HttpResponse
import io.micronaut.http.annotation.Body
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Delete
import io.micronaut.http.annotation.Get
import io.micronaut.http.annotation.Post
import io.micronaut.http.annotation.Put
import io.micronaut.security.annotation.Secured
import io.micronaut.security.rules.SecurityRule
import io.micronaut.validation.Validated
import org.slf4j.LoggerFactory
import java.security.Principal
import javax.validation.Valid

@Validated
@Controller("/customers")
@Secured(SecurityRule.IS_AUTHENTICATED)
class CustomerController (private val userService: UserService,
                          private val customerService: CustomerService) {

    private val log = LoggerFactory.getLogger(this::class.java)

    @Post
    @Secured(RolesConstants.ROLE_USER)
    fun create(@Body @Valid customerRequest: CustomerRequest, @Nullable principal: Principal): HttpResponse<CustomerResponse> {
        log.info("Buscando usuário com email: ${principal.name} para inclusão de cliente")
        val user = userService.findByEmail(principal.name)

        if(user.isPresent) {
            val customer = customerService.save(customerRequest, user.get())
            return HttpResponse.created(customer)
        }

        return HttpResponse.unauthorized()
    }

    @Get
    @Secured(RolesConstants.ROLE_USER)
    fun findAllbyUser(@Nullable principal: Principal): HttpResponse<List<CustomerResponse>> {
        log.info("Buscando usuário com email: ${principal.name} para recuperar lista de clientes")
        val user = userService.findByEmail(principal.name)

        if(user.isPresent) {
            val customers = customerService.findAllByUser(user.get())
            return if(customers.isEmpty()){
                HttpResponse.noContent()
            } else {
                HttpResponse.ok(customers)
            }
        }
        return HttpResponse.unauthorized()
    }

    @Get("/{id}")
    @Secured(RolesConstants.ROLE_USER)
    fun findById(id: Long, @Nullable principal: Principal): HttpResponse<CustomerResponse> {
        log.info("Buscando usuário com email: ${principal.name} para recuperar cliente com id $id")
        val user = userService.findByEmail(principal.name)

        if(user.isPresent) {
            return HttpResponse.ok(user.get().id?.let { userId -> customerService.findByIdAndUserId(id, userId) })
        }
        return HttpResponse.unauthorized()
    }

    @Delete("/{id}")
    @Secured(RolesConstants.ROLE_USER)
    fun deleteById(id: Long, @Nullable principal: Principal): HttpResponse<Any> {
        log.info("Buscando usuário com email: ${principal.name} para deletar cliente com id $id")
        val user = userService.findByEmail(principal.name)
        if(user.isPresent) {
            user.get().id?.let { userId -> customerService.deleteByIdAndUserId(id, userId) }
            return HttpResponse.noContent()
        }
        return HttpResponse.unauthorized()
    }

    @Put("/{id}")
    @Secured(RolesConstants.ROLE_USER)
    fun updatebyId(id: Long,
                   @Body @Valid customerRequest: CustomerRequest,
                   @Nullable principal: Principal): HttpResponse<CustomerResponse> {
        log.info("Buscando usuário com email: ${principal.name} para alterar cliente com id $id")
        val user = userService.findByEmail(principal.name)
        if(user.isPresent) {
            return HttpResponse.ok(customerService.updateByIdAndUserId(id, user.get(), customerRequest))
        }
        return HttpResponse.unauthorized()
    }
}