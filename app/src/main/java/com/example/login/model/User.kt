package com.example.login.model

import kotlinx.serialization.Serializable

@Serializable
data class UserProfile(
    val id: String,
    val firstName: String,
    val lastName: String,
    val matricula: String,
    val career: String? = null,
    val group: String? = null,
    val cuatrimestre: String? = null,
    val club: String? = null,
    val isJefeDeGrupo: Boolean = false,
    val qrCode: String? = null
)
