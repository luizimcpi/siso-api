package com.siso.web.dto.response

import com.fasterxml.jackson.annotation.JsonFormat
import io.micronaut.core.annotation.Introspected
import java.time.LocalDateTime
import java.util.UUID

@Introspected
data class UserResponse(
        val id: UUID,

        val email: String,

        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
        val createdAt: LocalDateTime,

        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
        val updatedAt: LocalDateTime
)
