package com.siso.model.entity

import com.siso.web.dto.response.UserResponse
import io.micronaut.core.annotation.Introspected
import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.UpdateTimestamp
import java.time.LocalDateTime
import javax.persistence.CascadeType
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.FetchType
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.JoinColumn
import javax.persistence.JoinTable
import javax.persistence.ManyToMany

@Entity(name = "users")
@Introspected
data class CustomUser(

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        var id: Long? = null,

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

        @ManyToMany(fetch = FetchType.EAGER, cascade = [CascadeType.ALL])
        @JoinTable(name = "user_role",
                joinColumns = [JoinColumn(name = "user_id", referencedColumnName = "id")],
                inverseJoinColumns = [JoinColumn(name = "role_id", referencedColumnName = "id")]
        )
        val roles: Set<Role> = emptySet()

)

fun toUserResponse(savedUser: CustomUser) : UserResponse =
        UserResponse(
                id = savedUser.id!!,
                email = savedUser.email,
                createdAt = savedUser.createdAt!!,
                updatedAt = savedUser.updatedAt!!
        )