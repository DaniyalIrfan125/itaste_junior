package com.techbayportal.itaste.data.remote


import com.techbayportal.itaste.data.models.LoginResponse
import com.techbayportal.itaste.data.models.PostsResponse
import com.techbayportal.itaste.data.models.SignUpResponse
import retrofit2.Response
import retrofit2.http.*
import java.io.File

interface ApiService {

    @GET("posts")
    suspend fun getPosts(): Response<PostsResponse>


    @FormUrlEncoded
    @POST("signup")
    suspend fun signUp(
        @Header("Accept") acceptJson: String,
        @Field("first") first: String?,
        @Field("last") last: String?,
        @Field("username") username: String?,
        @Field("phone") phone: String?,
        @Field("profilePic") profilePic: File?,
        @Field("email") email: String?,
        @Field("password") password: String?,
        @Field("role") role: String?

    ): Response<SignUpResponse>
}