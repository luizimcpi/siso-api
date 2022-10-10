package com.siso.web.dto.response

import io.micronaut.core.annotation.Introspected
import java.time.LocalDateTime
import java.util.UUID

@Introspected
data class UserResponse(
        val id: UUID,
        val email: String,
        val createdAt: LocalDateTime,
        val updatedAt: LocalDateTime
)
