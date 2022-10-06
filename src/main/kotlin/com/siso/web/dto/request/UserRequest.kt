package com.siso.web.dto.request

import io.micronaut.core.annotation.Introspected
import javax.validation.constraints.Email
import javax.validation.constraints.Size

@Introspected
data class UserRequest(
        @field:Email
        val email: String,

        @field:Size(min = 8)
        val password: String
)
