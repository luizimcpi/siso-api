package com.siso.repository

import com.siso.model.entity.CustomUser
import com.siso.model.entity.Customer
import io.micronaut.data.annotation.Repository
import io.micronaut.data.jpa.repository.JpaRepository
import java.util.UUID


@Repository
interface CustomerRepository: JpaRepository<Customer, UUID> {

    fun findAllByUser(user: CustomUser): List<Customer>

}