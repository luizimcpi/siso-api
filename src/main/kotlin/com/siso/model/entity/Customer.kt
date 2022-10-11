package com.siso.model.entity

import com.siso.web.dto.response.CustomerResponse
import io.micronaut.core.annotation.Introspected
import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.GenericGenerator
import org.hibernate.annotations.UpdateTimestamp
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.UUID
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.persistence.ManyToOne

@Entity(name = "customers")
@Introspected
data class Customer(

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    val id: UUID? = null,

    @ManyToOne
    val user: CustomUser,

    @Column
    val name: String,

    @Column
    val email: String,

    @Column
    val document: String,

    @Column(name = "birth_date")
    val birthDate: LocalDate,

    @Column(name = "created_at")
    @CreationTimestamp
    val createdAt: LocalDateTime? = null,

    @Column(name = "updated_at")
    @UpdateTimestamp
    val updatedAt: LocalDateTime? = null,
)

fun toCustomerResponse(savedCustomer: Customer) : CustomerResponse =
    CustomerResponse(
        id = savedCustomer.id!!,
        email = savedCustomer.email,
        name = savedCustomer.name,
        document = savedCustomer.document,
        birthDate = savedCustomer.birthDate,
        createdAt = savedCustomer.createdAt!!,
        updatedAt = savedCustomer.updatedAt!!
    )
