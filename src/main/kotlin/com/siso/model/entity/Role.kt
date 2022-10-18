package com.siso.model.entity

import io.micronaut.core.annotation.Introspected
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.FetchType
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.ManyToMany

@Entity(name = "roles")
@Introspected
data class Role (

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,

    @Column
    val name: String,

    @ManyToMany(mappedBy = "roles", fetch = FetchType.EAGER)
    val user: Collection<CustomUser>
)