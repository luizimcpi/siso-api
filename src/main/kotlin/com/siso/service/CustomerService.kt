package com.siso.service

import com.siso.model.entity.CustomUser
import com.siso.model.entity.Customer
import com.siso.model.entity.toCustomerResponse
import com.siso.repository.CustomerRepository
import com.siso.web.dto.request.CustomerRequest
import com.siso.web.dto.response.CustomerResponse
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

    fun findAllByUser(user: CustomUser): List<CustomerResponse> {
        return customerRepository.findAllByUser(user).map {
            customer -> toCustomerResponse(customer)
        }
    }
}