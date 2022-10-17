package com.siso.web.dto.request

import io.micronaut.core.annotation.Introspected
import javax.validation.constraints.Size

@Introspected
data class UserPasswordResetRequest(

    @field:Size(min = 8)
    val oldPassword: String,

    @field:Size(min = 8)
    val newPassword: String,

    @field:Size(min = 8)
    val newPasswordConfirmation: String

)
