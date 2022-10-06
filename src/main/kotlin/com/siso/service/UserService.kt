package com.siso.service

import com.siso.configuration.BCryptPasswordEncoderService
import com.siso.model.entity.CustomUser
import com.siso.repository.RoleRepository
import com.siso.repository.UserRepository
import com.siso.web.dto.request.UserRequest
import jakarta.inject.Singleton
import java.util.Optional

@Singleton
class UserService(private val userRepository: UserRepository,
                  private val roleRepository: RoleRepository,
                  private val passwordEncoder: BCryptPasswordEncoderService) {

    fun findByEmail(email: String): Optional<CustomUser> {
        return userRepository.findByEmail(email)
    }

    fun save(userRequest: UserRequest): CustomUser {
        val roleUser = roleRepository.findByName("ROLE_USER")

        val user = CustomUser(
            email = userRequest.email,
            password = passwordEncoder.encode(userRequest.password),
            active = true,
            passwordReset = true,
            role = roleUser
        )

       return userRepository.save(user)
    }
}