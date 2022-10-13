package com.siso.web.dto.response

import io.micronaut.core.annotation.Introspected
import java.time.LocalDate
import java.time.LocalDateTime

@Introspected
data class CustomerResponse(
        val id: Long,
        val email: String,
        val name: String,
        val document: String,
        val birthDate: LocalDate,
        val createdAt: LocalDateTime,
        val updatedAt: LocalDateTime
)
