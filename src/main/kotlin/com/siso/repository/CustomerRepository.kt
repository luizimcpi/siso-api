package com.siso.repository

import com.siso.model.entity.Customer
import io.micronaut.data.annotation.Query
import io.micronaut.data.annotation.Repository
import io.micronaut.data.jpa.repository.JpaRepository
import io.micronaut.data.model.Page
import io.micronaut.data.model.Pageable
import java.util.Optional


@Repository
interface CustomerRepository: JpaRepository<Customer, Long> {

    fun findByUserId(userId: Long, pageable: Pageable): Page<Customer>

    fun findByIdAndUserId(id: Long, userId: Long): Optional<Customer>

    fun deleteByIdAndUserId(id: Long, userId: Long)

    @Query(value = "select c from customers c join c.user u WHERE u.id = :userId and c.name like :name")
    fun findByUserIdAndName(
        userId: Long,
        name: String
    ): List<Customer>

}