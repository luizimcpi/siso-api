package com.siso.repository

import com.siso.model.entity.CustomUser
import io.micronaut.data.annotation.Repository
import io.micronaut.data.jpa.repository.JpaRepository
import java.util.Optional


@Repository
interface UserRepository: JpaRepository<CustomUser, Long> {
    fun findByEmail(email: String): Optional<CustomUser>
}