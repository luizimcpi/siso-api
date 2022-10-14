package com.siso.web.controller

import com.siso.web.controller.dto.LoginOutputDto
import com.siso.web.dto.response.CustomerResponse
import com.siso.web.dto.response.UserResponse
import io.micronaut.core.type.Argument
import io.micronaut.data.model.Page
import io.micronaut.http.HttpRequest
import io.micronaut.http.HttpStatus
import io.micronaut.http.MutableHttpRequest
import io.micronaut.http.client.HttpClient
import io.micronaut.http.client.annotation.Client
import io.micronaut.http.client.exceptions.HttpClientResponseException
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import jakarta.inject.Inject
import org.flywaydb.core.Flyway
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation
import org.junit.jupiter.api.Order
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.TestMethodOrder
import org.junit.jupiter.api.assertThrows

@MicronautTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(OrderAnnotation::class)
class CustomerControllerTest {

    @Inject
    @field:Client("/")
    lateinit var httpClient: HttpClient

    @Inject
    lateinit var flyway: Flyway

    private lateinit var adminAccessToken: String
    private lateinit var userAccessToken: String
    private lateinit var anotherUserAccessToken: String
    private var customerId: Long = 0

    @BeforeAll
    fun setUp(){
        flyway.clean()
        flyway.migrate()

        val requestLoginBody = "{\"username\": \"luizimcpi@gmail.com\", \"password\": \"Mudar@123\"}"
        val requestLogin: HttpRequest<Any> = HttpRequest.POST("/login", requestLoginBody)
        val responseLogin =  httpClient.toBlocking().exchange(requestLogin, Argument.listOf(LoginOutputDto::class.java))

        val loginAdminOutputDto = responseLogin.body.get().first() as LoginOutputDto
        adminAccessToken = loginAdminOutputDto.accessToken

        val requestUserBody = "{\"email\": \"teste@email.com\", \"password\": \"12345678\"}"
        val request: MutableHttpRequest<String>? = HttpRequest.POST("/users", requestUserBody).bearerAuth(adminAccessToken)
        httpClient.toBlocking().exchange(request, Argument.listOf(UserResponse::class.java))

        val requestUserLoginBody = "{\"username\": \"teste@email.com\", \"password\": \"12345678\"}"
        val requestUserLogin: HttpRequest<Any> = HttpRequest.POST("/login", requestUserLoginBody)
        val responseUserLogin =  httpClient.toBlocking().exchange(requestUserLogin, Argument.listOf(LoginOutputDto::class.java))

        val loginUserOutputDto = responseUserLogin.body.get().first() as LoginOutputDto
        userAccessToken = loginUserOutputDto.accessToken

        val requestAnotherUserBody = "{\"email\": \"teste1@email.com\", \"password\": \"12345678\"}"
        val requestAnotherUser: MutableHttpRequest<String>? = HttpRequest.POST("/users", requestAnotherUserBody).bearerAuth(adminAccessToken)
        httpClient.toBlocking().exchange(requestAnotherUser, Argument.listOf(UserResponse::class.java))

        val requestAnotherUserLoginBody = "{\"username\": \"teste1@email.com\", \"password\": \"12345678\"}"
        val requestAnotherUserLogin: HttpRequest<Any> = HttpRequest.POST("/login", requestAnotherUserLoginBody)
        val responseAnotherUserLogin =  httpClient.toBlocking().exchange(requestAnotherUserLogin, Argument.listOf(LoginOutputDto::class.java))

        val loginAnotherUserOutputDto = responseAnotherUserLogin.body.get().first() as LoginOutputDto
        anotherUserAccessToken = loginAnotherUserOutputDto.accessToken
    }

    @AfterAll
    fun clean(){
        flyway.clean()
    }

    @Test
    @Order(1)
    fun `should create customer success when receive valid body and valid user token`(){
        val requestBody = "{\"email\": \"customer_test@email.com\", \"document\": \"12345678\", \"name\": \"customer_test\", \"birthDate\": \"2022-10-14\"}"
        val request: MutableHttpRequest<String>? = HttpRequest.POST("/customers", requestBody).bearerAuth(userAccessToken)
        val response = httpClient.toBlocking().exchange(request, Argument.listOf(CustomerResponse::class.java))

        assertNotNull(response)
        val customerResponse = response.body.get().first() as CustomerResponse
        assertNotNull(customerResponse)
        assertNotNull(customerResponse.id)
        customerId = customerResponse.id
        assertNotNull(customerResponse.createdAt)
        assertNotNull(customerResponse.updatedAt)
        assertEquals("customer_test@email.com", customerResponse.email)
        assertEquals("customer_test", customerResponse.name)
        assertEquals("12345678", customerResponse.document)
        assertEquals("2022-10-14", customerResponse.birthDate.toString())
        assertEquals(HttpStatus.CREATED, response.status)
    }

    @Test
    @Order(2)
    fun `should return unauthorized when try create customer without admin or user token`(){
        val requestBody = "{\"email\": \"customer_test@email.com\", \"document\": \"12345678\", \"name\": \"customer_test\", \"birthDate\": \"2022-10-14\"}"
        val request: MutableHttpRequest<String>? =
            HttpRequest.POST("/customers", requestBody)

        val thrown = assertThrows<HttpClientResponseException> {
            httpClient.toBlocking().exchange(request, Argument.listOf(CustomerResponse::class.java))
        }

        assertEquals(HttpStatus.UNAUTHORIZED, thrown.status)
    }

    @Test
    @Order(3)
    fun `should return forbidden when try create customer with admin token`(){
        val requestBody = "{\"email\": \"customer_test@email.com\", \"document\": \"12345678\", \"name\": \"customer_test\", \"birthDate\": \"2022-10-14\"}"
        val request: MutableHttpRequest<String>? = HttpRequest.POST("/customers", requestBody).bearerAuth(adminAccessToken)

        val thrown = assertThrows<HttpClientResponseException> {
            httpClient.toBlocking().exchange(request, Argument.listOf(CustomerResponse::class.java))
        }

        assertEquals(HttpStatus.FORBIDDEN, thrown.status)
    }

    @Test
    @Order(4)
    fun `should return bad request when create customer with invalid email`(){
        val requestBody = "{\"email\": \"customer_test\", \"document\": \"12345678\", \"name\": \"customer_test\", \"birthDate\": \"2022-10-14\"}"
        val request: MutableHttpRequest<String>? =
            HttpRequest.POST("/customers", requestBody).bearerAuth(userAccessToken)

        val thrown = assertThrows<HttpClientResponseException> {
            httpClient.toBlocking().exchange(request, Argument.listOf(CustomerResponse::class.java))
        }

        assertEquals(HttpStatus.BAD_REQUEST, thrown.status)
    }

    @Test
    @Order(5)
    fun `should find customer by id success when receive valid customerId and valid user token`(){
        val request: HttpRequest<*> = HttpRequest.GET<Any>("/customers/$customerId").bearerAuth(userAccessToken)
        val response = httpClient.toBlocking().exchange(request, Argument.listOf(CustomerResponse::class.java))

        assertNotNull(response)
        val customerResponse = response.body.get().first() as CustomerResponse
        assertNotNull(customerResponse)
        assertNotNull(customerResponse.id)
        assertNotNull(customerResponse.createdAt)
        assertNotNull(customerResponse.updatedAt)
        assertEquals("customer_test@email.com", customerResponse.email)
        assertEquals("customer_test", customerResponse.name)
        assertEquals("12345678", customerResponse.document)
        assertEquals("2022-10-14", customerResponse.birthDate.toString())
        assertEquals(HttpStatus.OK, response.status)
    }

    @Test
    @Order(6)
    fun `should find customer by id must return no content when receive valid customerId and another user token owner`(){
        val request: HttpRequest<*> = HttpRequest.GET<Any>("/customers/$customerId").bearerAuth(anotherUserAccessToken)

        val thrown = assertThrows<HttpClientResponseException> {
            httpClient.toBlocking().exchange(request, Argument.listOf(CustomerResponse::class.java))
        }

        assertEquals("Customer not found with id 1", thrown.message)
        assertEquals(HttpStatus.NOT_FOUND, thrown.status)
    }

    @Test
    @Order(7)
    fun `should find customers success when receive valid customerId and user token owner`(){
        val request: HttpRequest<*> = HttpRequest.GET<Any>("/customers").bearerAuth(userAccessToken)
        val response = httpClient.toBlocking().exchange(request, Argument.listOf(Page::class.java))

        assertNotNull(response)
        val customers = response.body.get().first() as Page<CustomerResponse>
        assertNotNull(customers)
        assertFalse(customers.isEmpty)
        assertFalse(customers.content.isEmpty())
        assertTrue(customers.content.size == 1)
        assertEquals(HttpStatus.OK, response.status)
    }

    @Test
    @Order(8)
    fun `should find customers must return no content when receive valid customerId and usertoken owner without customers`(){
        val request: HttpRequest<*> = HttpRequest.GET<Any>("/customers").bearerAuth(anotherUserAccessToken)
        val response = httpClient.toBlocking().exchange(request, Argument.listOf(Page::class.java))
        assertEquals(HttpStatus.NO_CONTENT, response.status)
    }

    @Test
    @Order(9)
    fun `should update customer by id success when receive valid customerId and valid user token`(){
        val requestBody = "{\"email\": \"customer_updated@email.com\", \"document\": \"updated\", \"name\": \"customer_updated\", \"birthDate\": \"1990-03-03\"}"
        val request: MutableHttpRequest<String>? = HttpRequest.PUT("/customers/$customerId", requestBody).bearerAuth(userAccessToken)
        val response = httpClient.toBlocking().exchange(request, Argument.listOf(CustomerResponse::class.java))

        assertNotNull(response)
        val customerResponse = response.body.get().first() as CustomerResponse
        assertNotNull(customerResponse)
        assertNotNull(customerResponse.id)
        assertNotNull(customerResponse.createdAt)
        assertNotNull(customerResponse.updatedAt)
        assertEquals("customer_updated@email.com", customerResponse.email)
        assertEquals("customer_updated", customerResponse.name)
        assertEquals("updated", customerResponse.document)
        assertEquals("1990-03-03", customerResponse.birthDate.toString())
        assertEquals(HttpStatus.OK, response.status)
    }

    @Test
    @Order(10)
    fun `should delete customer by id success when receive valid customerId and user token owner`(){
        val request: HttpRequest<*> = HttpRequest.DELETE<Any>("/customers/$customerId").bearerAuth(userAccessToken)
        val response = httpClient.toBlocking().exchange(request, Argument.listOf(Page::class.java))
        assertEquals(HttpStatus.NO_CONTENT, response.status)
    }
}