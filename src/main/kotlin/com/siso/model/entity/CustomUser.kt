package com.siso.model.entity

import com.siso.web.dto.response.UserResponse
import io.micronaut.core.annotation.Introspected
import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.GenericGenerator
import org.hibernate.annotations.UpdateTimestamp
import java.time.LocalDateTime
import java.util.UUID
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.persistence.JoinColumn
import javax.persistence.OneToOne

@Entity(name = "users")
@Introspected
data class CustomUser(

        @Id
        @GeneratedValue(generator = "UUID")
        @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
        val id: UUID? = null,

        @Column
        val email: String,

        @Column
        val password: String,

        @Column
        val active: Boolean,

        @Column(name = "password_reset")
        val passwordReset: Boolean,

        @Column(name = "created_at")
        @CreationTimestamp
        val createdAt: LocalDateTime? = null,

        @Column(name = "updated_at")
        @UpdateTimestamp
        val updatedAt: LocalDateTime? = null,

        @OneToOne
        @JoinColumn(name = "role", referencedColumnName = "id")
        val role: Role
)

fun toUserResponse(savedUser: CustomUser) : UserResponse =
        UserResponse(
                id = savedUser.id!!,
                email = savedUser.email,
                createdAt = savedUser.createdAt!!,
                updatedAt = savedUser.updatedAt!!
        )