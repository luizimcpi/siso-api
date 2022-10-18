package com.siso.repository

import com.siso.model.entity.Customer
import io.micronaut.data.annotation.Repository
import io.micronaut.data.jpa.repository.JpaRepository
import io.micronaut.data.model.Page
import io.micronaut.data.model.Pageable
import java.util.Optional


@Repository
interface CustomerRepository: JpaRepository<Customer, Long> {

    fun findByUserId(userId: Long, pageable: Pageable): Page<Customer>

    fun findByUserIdAndNameContains(userId: Long, name: String, pageable: Pageable): Page<Customer>

    fun findByIdAndUserId(id: Long, userId: Long): Optional<Customer>

    fun deleteByIdAndUserId(id: Long, userId: Long)

    fun deleteAllByUserId(userId: Long)

}