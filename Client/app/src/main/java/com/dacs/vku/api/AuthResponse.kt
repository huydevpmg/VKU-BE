package com.dacs.vku.api

data class AuthResponse(
    val userId: String,
    val name: String,
    val email: String
)