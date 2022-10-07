package com.siso.web.controller.dto

import com.fasterxml.jackson.annotation.JsonAlias

data class LoginOutputDto(
    val username: String,
    val roles: Collection<String>,
    @JsonAlias("access_token")
    val token: String,
    @JsonAlias("token_type")
    val tokenType: String,
    @JsonAlias("expires_in")
    val expiration: Long
)
