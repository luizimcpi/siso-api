package com.siso.web.controller

import com.siso.web.controller.dto.LoginOutputDto
import com.siso.web.dto.response.UserResponse
import io.micronaut.core.type.Argument
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
import org.junit.jupiter.api.Assertions.assertNotNull
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
class UserControllerTest {

    @Inject
    @field:Client("/")
    lateinit var httpClient: HttpClient

    @Inject
    lateinit var flyway: Flyway

    private lateinit var accessToken: String

    @BeforeAll
    fun setUp(){
        flyway.clean()
        flyway.migrate()

        val requestLoginBody = "{\"username\": \"luizimcpi@gmail.com\", \"password\": \"Mudar@123\"}"
        val requestLogin: HttpRequest<Any> = HttpRequest.POST("/login", requestLoginBody)
        val responseLogin =  httpClient.toBlocking().exchange(requestLogin, Argument.listOf(LoginOutputDto::class.java))

        val loginOutputDto = responseLogin.body.get().first() as LoginOutputDto
        accessToken = loginOutputDto.accessToken
    }

    @AfterAll
    fun clean(){
        flyway.clean()
    }

    @Test
    @Order(1)
    fun `should create user success when receive valid body and valid admin user token`(){
        val requestBody = "{\"email\": \"teste@email.com\", \"password\": \"12345678\"}"
        val request: MutableHttpRequest<String>? = HttpRequest.POST("/users", requestBody).bearerAuth(accessToken)
        val response = httpClient.toBlocking().exchange(request, Argument.listOf(UserResponse::class.java))

        assertNotNull(response)
        val userResponse = response.body.get().first() as UserResponse
        assertNotNull(userResponse)
        assertNotNull(userResponse.id)
        assertNotNull(userResponse.createdAt)
        assertNotNull(userResponse.updatedAt)
        assertEquals("teste@email.com", userResponse.email)
        assertEquals(HttpStatus.CREATED, response.status)
    }

    @Test
    @Order(2)
    fun `should create user error when try create another user with same email`(){
        val requestBody = "{\"email\": \"teste@email.com\", \"password\": \"12345678\"}"
        val request: MutableHttpRequest<String>? = HttpRequest.POST("/users", requestBody).bearerAuth(accessToken)

        val thrown = assertThrows<HttpClientResponseException> {
            httpClient.toBlocking().exchange(request, Argument.listOf(UserResponse::class.java))
        }

        assertEquals("Client '/': Conflict", thrown.message)
        assertEquals(HttpStatus.CONFLICT, thrown.status)
    }

    @Test
    @Order(3)
    fun `should return unauthorized when try create user without admin user token`(){
        val requestBody = "{\"email\": \"teste@email.com\", \"password\": \"12345678\"}"
        val request: MutableHttpRequest<String>? =
            HttpRequest.POST("/users", requestBody)

        val thrown = assertThrows<HttpClientResponseException> {
            httpClient.toBlocking().exchange(request, Argument.listOf(UserResponse::class.java))
        }

        assertEquals(HttpStatus.UNAUTHORIZED, thrown.status)
    }

    @Test
    @Order(4)
    fun `should return bad request when create user with invalid size of password`(){
        val requestBody = "{\"email\": \"teste@email.com\", \"password\": \"1234567\"}"
        val request: MutableHttpRequest<String>? =
            HttpRequest.POST("/users", requestBody).header("Authorization", "Bearer $accessToken")

        val thrown = assertThrows<HttpClientResponseException> {
            httpClient.toBlocking().exchange(request, Argument.listOf(UserResponse::class.java))
        }

        assertEquals(HttpStatus.BAD_REQUEST, thrown.status)
    }

    @Test
    @Order(5)
    fun `should return bad request when create user with invalid email`(){
        val requestBody = "{\"email\": \"teste\", \"password\": \"12345678\"}"
        val request: MutableHttpRequest<String>? =
            HttpRequest.POST("/users", requestBody).header("Authorization", "Bearer $accessToken")

        val thrown = assertThrows<HttpClientResponseException> {
            httpClient.toBlocking().exchange(request, Argument.listOf(UserResponse::class.java))
        }

        assertEquals(HttpStatus.BAD_REQUEST, thrown.status)
    }


}