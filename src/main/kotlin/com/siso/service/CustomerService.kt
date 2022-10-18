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
import org.slf4j.LoggerFactory

@Singleton
class CustomerService(private val customerRepository: CustomerRepository) {

    private val log = LoggerFactory.getLogger(this::class.java)

    fun save(customerRequest: CustomerRequest, user: CustomUser): CustomerResponse {
        log.info("Mapping customer request $customerRequest to entity ")
        val customer = Customer (
            user = user,
            name = customerRequest.name,
            email = customerRequest.email,
            document = customerRequest.document,
            birthDate = customerRequest.birthDate
        )
        log.info("Mapping customer request to entity $customer finished.")
        val savedCustomer = customerRepository.save(customer)

        log.info("Customer has been saved $savedCustomer.")
        return toCustomerResponse(savedCustomer)
    }

    fun findAllByUser(userId: Long, pageable: Pageable): Page<CustomerResponse> {
        return customerRepository.findByUserId(userId, pageable).map {
            customer -> toCustomerResponse(customer)
        }
    }

    fun findAllByUserAndName(userId: Long, name: String, pageable: Pageable): Page<CustomerResponse> {
        log.info("Finding all customers of userId $userId with name $name.")
        return customerRepository.findByUserIdAndNameContains(userId, name, pageable).map {
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

    fun deleteAllByUserId(userId: Long) {
        customerRepository.deleteAllByUserId(userId)
    }
}