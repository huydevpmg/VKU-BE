package com.dacs.vku.api

import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface AuthenticationAPI {
    @FormUrlEncoded
    @POST("/auth/verify-id-token")
    fun verifyIdToken(@Field("idToken") idToken: String): Call<AuthResponse>
}