package com.siso.repository

import com.siso.model.entity.Role
import io.micronaut.data.annotation.Repository
import io.micronaut.data.jpa.repository.JpaRepository
import java.util.UUID

@Repository
interface RoleRepository: JpaRepository<Role, Long> {
    fun findByName(name: String): Role
}