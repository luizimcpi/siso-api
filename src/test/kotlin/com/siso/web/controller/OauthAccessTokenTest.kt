package com.siso.web.controller

import com.siso.repository.RefreshTokenRepository
import io.micronaut.http.HttpRequest
import io.micronaut.http.client.HttpClient
import io.micronaut.http.client.annotation.Client
import io.micronaut.security.authentication.UsernamePasswordCredentials
import io.micronaut.security.token.jwt.endpoints.TokenRefreshRequest
import io.micronaut.security.token.jwt.render.AccessRefreshToken
import io.micronaut.security.token.jwt.render.BearerAccessRefreshToken
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import jakarta.inject.Inject
import org.flywaydb.core.Flyway
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance

@MicronautTest(rollback = false)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class OauthAccessTokenTest {

    @Inject
    @field:Client("/")
    lateinit var client: HttpClient

    @Inject
    lateinit var refreshTokenRepository: RefreshTokenRepository

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
    fun verifyJWTAccessTokenRefreshWorks() {
        val username = "luizimcpi@gmail.com"

        val creds = UsernamePasswordCredentials(username, "Mudar@123")
        val request: HttpRequest<*> = HttpRequest.POST("/oauth/login", creds)

        val oldTokenCount = refreshTokenRepository.count()
        val rsp: BearerAccessRefreshToken = client.toBlocking().retrieve(request, BearerAccessRefreshToken::class.java)
        Thread.sleep(3000)
        assertEquals(oldTokenCount + 1, refreshTokenRepository.count())

        assertNotNull(rsp.accessToken)
        assertNotNull(rsp.refreshToken)

        Thread.sleep(1000) // sleep for one second to give time for the issued at `iat` Claim to change
        val refreshResponse = client.toBlocking().retrieve(HttpRequest.POST("/oauth/access_token",
            TokenRefreshRequest(rsp.refreshToken)
        ), AccessRefreshToken::class.java)

        assertNotNull(refreshResponse.accessToken)
        assertNotEquals(rsp.accessToken, refreshResponse.accessToken)

        refreshTokenRepository.deleteAll()
    }
}