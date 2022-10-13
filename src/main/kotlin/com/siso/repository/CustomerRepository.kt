package com.siso.repository

import com.siso.model.entity.Customer
import io.micronaut.data.annotation.Query
import io.micronaut.data.annotation.Repository
import io.micronaut.data.jpa.repository.JpaRepository
import java.util.Optional


@Repository
interface CustomerRepository: JpaRepository<Customer, Long> {

    @Query(value = "select c from customers c join c.user u WHERE u.id = :userId")
    fun findAllByUser(userId: Long): List<Customer>

    fun findByIdAndUserId(id: Long, userId: Long): Optional<Customer>

    fun deleteByIdAndUserId(id: Long, userId: Long)

}