package com.siso.service

import com.siso.configuration.BCryptPasswordEncoderService
import com.siso.exception.BadRequestException
import com.siso.model.entity.CustomUser
import com.siso.model.entity.RolesConstants.ROLE_USER
import com.siso.repository.RoleRepository
import com.siso.repository.UserRepository
import com.siso.web.dto.request.UserPasswordResetRequest
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
        val roleUser = roleRepository.findByName(ROLE_USER)

        val user = CustomUser(
            email = userRequest.email,
            password = passwordEncoder.encode(userRequest.password),
            active = true,
            passwordReset = true,
            roles = setOf(roleUser)
        )

       return userRepository.save(user)
    }

    fun update(user: CustomUser, userPasswordResetRequest: UserPasswordResetRequest) {
        if(!passwordEncoder.matches(userPasswordResetRequest.oldPassword, user.password)){
            throw BadRequestException("Senha atual não confere.")
        }
        if(userPasswordResetRequest.newPassword != userPasswordResetRequest.newPasswordConfirmation) {
            throw BadRequestException("Novas senhas não são iguais.")
        }
        val userToUpdate = user.copy(password = passwordEncoder.encode(userPasswordResetRequest.newPassword))
        userRepository.update(userToUpdate)
    }
}