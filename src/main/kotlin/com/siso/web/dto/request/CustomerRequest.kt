package com.siso.web.dto.request

import io.micronaut.core.annotation.Introspected
import java.time.LocalDate
import javax.validation.constraints.Email

@Introspected
data class CustomerRequest(
        @field:Email
        val email: String,
        val name: String,
        val document: String,
        val birthDate: LocalDate
)
