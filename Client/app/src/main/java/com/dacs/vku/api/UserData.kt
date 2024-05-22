package com.dacs.vku.api

import java.io.Serializable

data class UserData(
    val name: String?,
    val email: String?,
    val userId: String,
    val profilePictureUrl: String?
) : Serializable
