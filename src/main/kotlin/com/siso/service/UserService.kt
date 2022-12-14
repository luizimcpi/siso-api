package com.siso.service

import com.siso.configuration.BCryptPasswordEncoderService
import com.siso.exception.BadRequestException
import com.siso.exception.NotFoundException
import com.siso.model.entity.CustomUser
import com.siso.model.entity.RolesConstants.ROLE_USER
import com.siso.repository.RoleRepository
import com.siso.repository.UserRepository
import com.siso.web.dto.request.UserPasswordResetRequest
import com.siso.web.dto.request.UserRequest
import jakarta.inject.Singleton
import org.slf4j.LoggerFactory
import java.util.Optional

@Singleton
class UserService(private val userRepository: UserRepository,
                  private val roleRepository: RoleRepository,
                  private val customerService: CustomerService,
                  private val passwordEncoder: BCryptPasswordEncoderService) {

    private val log = LoggerFactory.getLogger(this::class.java)

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
        val userToUpdate = user.copy(password = passwordEncoder.encode(userPasswordResetRequest.newPassword), passwordReset = false)
        userRepository.update(userToUpdate)
    }

    fun deleteById(id: Long) {
        log.info("Iniciando busca de usuário com id $id para remoção.")
        val user = userRepository.findById(id)

        if(user.isPresent){
            log.info("Usuário com id $id encontrado iniciando remoção de customers")
            customerService.deleteAllByUserId(id)
            log.info("Iniciando remoção do usuário.")
            userRepository.deleteById(id)
            log.info("Processo de deleção completado.")
            return
        }
        log.info("Usuário com id $id não encontrado.")
        throw NotFoundException("Não foi encontrado usuário com id $id.")
    }
}