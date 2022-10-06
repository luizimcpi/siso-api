package com.siso.configuration

import jakarta.inject.Singleton
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder

@Singleton
class BCryptPasswordEncoderService : PasswordEncoder {

    private val bCryptEncoder = BCryptPasswordEncoder()

    override fun encode(rawPassword: CharSequence?): String {
        return bCryptEncoder.encode(rawPassword)
    }

    override fun matches(rawPassword: CharSequence?, encodedPassword: String?): Boolean {
        return bCryptEncoder.matches(rawPassword, encodedPassword)
    }

}