package com.dacs.vku.domain.model

data class SignInResult(
    val data: UserData?,
    val errorMessage: String?
)

data class UserData(
    var userId: String,
    var username: String,
    val profilePictureUrl: String?
)
