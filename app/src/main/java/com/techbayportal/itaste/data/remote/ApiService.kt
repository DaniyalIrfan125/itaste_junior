package com.techbayportal.itaste.data.remote


import com.techbayportal.itaste.data.models.*
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.*

interface ApiService {

    @GET("posts")
    suspend fun getPosts(): Response<PostsResponse>

    @FormUrlEncoded
    @POST("login")
    suspend fun login(
        @Header("Accept") acceptJson: String,
        @Field("username") email: String?,
        @Field("password") password: String?
    ): Response<LoginResponse>


    @Multipart
    @POST("user-signup")
    suspend fun signUp(
        @Header("Accept") acceptJson: String,
        @Part("first") first: RequestBody?,
        @Part("last") last: RequestBody?,
        @Part("username") username: RequestBody?,
        @Part("phone") phone: RequestBody?,
        @Part profileImage: MultipartBody.Part,
        @Part("email") email: RequestBody?,
        @Part("password") password: RequestBody?,
        @Part("role") role: RequestBody?
    ): Response<SignUpResponse>

    @Multipart
    @POST("vendor-signup")
    suspend fun signUpVendor(
        @Header("Accept") acceptJson: String,
        @Query("first") first: String?,
        @Query("last") last: String?,
        @Query("username") username: String?,
        @Part profilePic: MultipartBody.Part,
        @Query("phone") phone: String?,
        @Query("email") email: String?,
        @Query("password") password: String?,
        @Query("country_id") country_id: String?,
        @Query("city_id") city_id: String?,
        @Query("days_of_week") days_of_week: ArrayList<String>?,
        @Query("is_deliverable") is_deliverable: Boolean?,
        @Query("password_confirmation") password_confirmation: String?

    ): Response<SignUpResponse>

    @FormUrlEncoded
    @POST("forget-password")
    suspend fun forgotPassword(
        @Header("Accept") acceptJson: String,
        @Field("phone") phone: String?
    ): Response<VerifyOtpResponse>

    /*@FormUrlEncoded
    @POST("reset-password/verify-code")
    suspend fun forgotVerifyOtp(
        @Header("Accept") acceptJson: String,
        @Field("code") name: String?,
        @Field("username") email: String?
    ): Response<VerifyOtpResponse>*/

    @FormUrlEncoded
    @POST("verify-otp")
    suspend fun verifyOpt(
        @Header("Accept") acceptJson: String,
        @Field("code") code: Int?,
        @Field("phone") phone: String?,
        @Field("type") type: String?
    ): Response<VerifyOtpResponse>

    @FormUrlEncoded
    @POST("ressend/otp")
    suspend fun resentOtp(
        @Header("Accept") acceptJson: String,
        @Field("phone") phone: String?,
        @Field("type") type: String?
    ): Response<VerifyOtpResponse>


    @GET("countries")
    suspend fun getAllCountries(
    ): Response<GetAllCountriesResponse>

    @GET("cities")
    suspend fun getAllCities(
        @Header("Accept") acceptJson: String,
        @Query("country_id") name: Int
    ): Response<GetAllCitiesResponse>

    @FormUrlEncoded
    @POST("reset-password/reset")
    suspend fun updatePassword(
        @Header("Accept") acceptJson: String,
        @Field("phone") phone: String?,
        @Field("password") password: String?,
        @Field("password_confirmation") confirmationPassword: String?
    ): Response<VerifyOtpResponse>


}