package com.siso.model.entity

import io.micronaut.core.annotation.Introspected
import java.io.Serializable
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Id
//
//@Introspected
//data class UserRoleKey(
//
//    @Column(name = "user_id")
//    val userId: Long,
//    @Column(name = "role_id")
//    val roleId: Long
//)

@Introspected
@Entity(name = "user_role")
class UserRole(
    @Id @Column(name = "user_id", nullable = false) val userId: Long,
    @Id @Column(name = "role_id", nullable = false) val roleId: Long) : Serializable {
    companion object {
        private const val serialVersionUID: Long = 170010121L
    }
}
