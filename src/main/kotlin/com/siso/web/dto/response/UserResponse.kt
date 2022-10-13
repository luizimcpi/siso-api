package com.siso.web.dto.response

import io.micronaut.core.annotation.Introspected
import java.time.LocalDateTime

@Introspected
data class UserResponse(
        val id: Long,
        val email: String,
        val createdAt: LocalDateTime,
        val updatedAt: LocalDateTime
)
