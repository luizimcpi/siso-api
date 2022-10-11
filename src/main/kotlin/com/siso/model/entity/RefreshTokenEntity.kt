package com.siso.model.entity

import io.micronaut.core.annotation.Introspected
import io.micronaut.data.annotation.DateCreated
import java.time.Instant
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.validation.constraints.NotBlank

@Entity(name = "refresh_token")
@Introspected
data class RefreshTokenEntity(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,

    @NotBlank
    @Column
    var username: String,

    @NotBlank
    @Column(name = "refresh_token")
    var refreshToken: String,

    @Column
    var revoked: Boolean,

    @Column(name = "created_at")
    @DateCreated
    var dateCreated: Instant? = null

)
