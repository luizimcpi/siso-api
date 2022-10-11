package com.siso.web.controller

import com.nimbusds.jwt.JWTParser
import com.nimbusds.jwt.SignedJWT
import com.siso.web.controller.dto.LoginOutputDto
import io.micronaut.core.type.Argument
import io.micronaut.http.HttpRequest
import io.micronaut.http.client.HttpClient
import io.micronaut.http.client.annotation.Client
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import jakarta.inject.Inject
import org.flywaydb.core.Flyway
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance

@MicronautTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class LoginControllerTest {


    @Inject
    @field:Client("/")
    lateinit var httpClient: HttpClient

    @Inject
    lateinit var flyway: Flyway

    @BeforeAll
    fun setUp(){
        flyway.clean()
        flyway.migrate()
    }

    @AfterAll
    fun clean(){
        flyway.clean()
    }

    @Test
    fun uponSuccessfulAuthenticationUserGetsAccessTokenAndRefreshToken() {
        val requestLoginBody = "{\"username\": \"luizimcpi@gmail.com\", \"password\": \"Mudar@123\"}"
        val requestLogin: HttpRequest<Any> = HttpRequest.POST("/login", requestLoginBody)
        val responseLogin =  httpClient.toBlocking().exchange(requestLogin, Argument.listOf(LoginOutputDto::class.java))

        val loginOutputDto = responseLogin.body.get().first() as LoginOutputDto

        assertEquals("luizimcpi@gmail.com", loginOutputDto.username)
        assertNotNull(loginOutputDto.accessToken)
        assertNotNull(loginOutputDto.refreshToken)

        assertTrue(JWTParser.parse(loginOutputDto.accessToken) is SignedJWT)
    }
}