package com.siso.service

import com.siso.exception.NotFoundException
import com.siso.model.entity.CustomUser
import com.siso.model.entity.Customer
import com.siso.model.entity.toCustomerResponse
import com.siso.repository.CustomerRepository
import com.siso.web.dto.request.CustomerRequest
import com.siso.web.dto.response.CustomerResponse
import io.micronaut.data.model.Page
import io.micronaut.data.model.Pageable
import jakarta.inject.Singleton

@Singleton
class CustomerService(private val customerRepository: CustomerRepository) {

    fun save(customerRequest: CustomerRequest, user: CustomUser): CustomerResponse {
        val customer = Customer (
            user = user,
            name = customerRequest.name,
            email = customerRequest.email,
            document = customerRequest.document,
            birthDate = customerRequest.birthDate
        )
        val savedCustomer = customerRepository.save(customer)

        return toCustomerResponse(savedCustomer)
    }

    fun findAllByUser(user: CustomUser, pageable: Pageable): Page<CustomerResponse> {
        return customerRepository.findByUserId(user.id!!, pageable).map {
            customer -> toCustomerResponse(customer)
        }
    }

    fun findByIdAndUserId(id: Long, userId: Long): CustomerResponse {
        val customer = customerRepository.findByIdAndUserId(id, userId)
        if(customer.isPresent){
            return toCustomerResponse(customer.get())
        }
        throw NotFoundException("Customer not found with id $id")
    }

    fun deleteByIdAndUserId(id: Long, userId: Long) {
        findByIdAndUserId(id, userId)
        customerRepository.deleteByIdAndUserId(id, userId)
    }

    fun updateByIdAndUserId(id: Long, user: CustomUser, customerRequest: CustomerRequest): CustomerResponse {
        val customer = customerRepository.findByIdAndUserId(id, user.id!!)
        if(customer.isPresent){
            val customerToUpdate = Customer(
                id = id,
                user = user,
                name = customerRequest.name,
                email = customerRequest.email,
                document = customerRequest.document,
                birthDate = customerRequest.birthDate,
                createdAt = customer.get().createdAt
            )
            val updatedCustomer = customerRepository.update(customerToUpdate)
            return toCustomerResponse(updatedCustomer)
        }
        throw NotFoundException("Customer not found with id $id")
    }
}