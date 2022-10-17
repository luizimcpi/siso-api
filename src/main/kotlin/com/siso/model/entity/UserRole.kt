package com.siso.model.entity

import io.micronaut.core.annotation.Introspected
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id

@Entity(name = "user_role")
@Introspected
data class UserRole(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @Column(name = "user_id")
    val  userId: Long,

    @Column(name = "role_id")
    val  roleId: Long

)
