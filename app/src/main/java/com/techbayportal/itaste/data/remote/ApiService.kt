package com.techbayportal.itaste.data.remote


import com.techbayportal.itaste.data.models.*
import okhttp3.MultipartBody
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
        @Query("first") first: String?,
        @Query("last") last: String?,
        @Query("username") username: String?,
        @Query("phone") phone: String?,
        @Part profileImage: MultipartBody.Part,
        @Query("email") email: String?,
        @Query("password") password: String?,
        @Query("role") role: String?
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
       // @Query("days_of_week") days_of_week: MutableList<DaysOfWeek>?,
        @Query("days_of_week[]") days_of_week: List<String>,
        @Query("is_deliverable") is_deliverable: Int?,
        @Query("password_confirmation") password_confirmation: String?,
        @Query("description") description: String?


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
    ): Response<LoginResponse>

    @FormUrlEncoded
    @POST("verify-otp")
    suspend fun verifyOptForUpdatePhone(
        @Header("Accept") acceptJson: String,
        @Header("Authorization") authHeader: String,
        @Field("code") code: Int?,
        @Field("phone") phone: String?,
        @Field("type") type: String?
    ): Response<LoginResponse>

    @FormUrlEncoded
    @POST("ressend/otp")
    suspend fun resentOtp(
        @Header("Accept") acceptJson: String,
        @Field("phone") phone: String?,
        @Field("type") type: String?
    ): Response<ResendOtpResponse>

    @FormUrlEncoded
    @POST("ressend/otp")
    suspend fun resentOtpUpdatePhone(
        @Header("Accept") acceptJson: String,
        @Header("Authorization") authHeader: String,
        @Field("phone") phone: String?,
        @Field("type") type: String?
    ): Response<ResendOtpResponse>


   /* @GET("countries")
    suspend fun getAllCountries(
    ): Response<GetAllCountriesResponse>*/

    @GET("countries")
    suspend fun getAllCountriesForHome(
        @Header("Accept") acceptJson: String,
        @Header("Authorization") authHeader: String
    ): Response<GetAllCountriesResponse>


    @GET("cities")
    suspend fun getAllCities(
        @Header("Accept") acceptJson: String,
        @Header("Authorization") authHeader: String,
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


    @FormUrlEncoded
    @POST("location-update")
    suspend fun updateUserLocation(
        @Header("Accept") acceptJson: String,
        @Header("Authorization") authHeader: String,
        @Field("country_id") country_id: Int?
    ): Response<GetAllCountriesResponse>

   
    @GET("user/personal-profile")
    suspend fun getUserPersonalProfile(
        @Header("Accept") acceptJson: String,
        @Header("Authorization") authHeader: String
    ): Response<UserPersonalProfileResponse>

    @Multipart
    @POST("user/update-profile")
    suspend fun updateUserPersonalProfile(
        @Header("Accept") acceptJson: String,
        @Header("Authorization") authHeader: String,
        @Query("first") first: String,
        @Query("last") last: String,
        @Query("email") email : String,
        @Query("phone") phone : String,
        @Part profilePic : MultipartBody.Part
    ): Response<SuccessResponse>


    @POST("user/update-profile")
    suspend fun updateUserPersonalProfileWithoutPic(
        @Header("Accept") acceptJson: String,
        @Header("Authorization") authHeader: String,
        @Query("first") first: String,
        @Query("last") last: String,
        @Query("email") email : String,
        @Query("phone") phone : String
    ): Response<SuccessResponse>

    @GET("vendor/personal-profile")
    suspend fun getVendorPersonalProfile(
        @Header("Accept") acceptJson: String,
        @Header("Authorization") authHeader: String
    ): Response<VendorPersonalProfileResponse>

    @Multipart
    @POST("vendor/update-profile")
    suspend fun updateVendorPersonalProfile(
        @Header("Accept") acceptJson: String,
        @Header("Authorization") authHeader: String,
        @Query("first_name") first_name: String,
        @Query("last_name") last_name : String,
        @Query("bio") bio : String,
        @Query("phone") phone : String,
        @Query("email") email : String,
        @Part profilePic : MultipartBody.Part,
        @Query("country_id") country_id : String,
        @Query("city_id") city_id : String

    ): Response<SuccessResponse>


    @POST("vendor/update-profile")
    suspend fun updateVendorPersonalProfileWithoutPic(
        @Header("Accept") acceptJson: String,
        @Header("Authorization") authHeader: String,
        @Query("first_name") first_name: String,
        @Query("last_name") last_name : String,
        @Query("bio") bio : String,
        @Query("phone") phone : String,
        @Query("email") email : String,
        @Query("country_id") country_id : String,
        @Query("city_id") city_id : String
    ): Response<SuccessResponse>

    @FormUrlEncoded
    @POST("password-change")
    suspend fun changePassword(
        @Header("Accept") acceptJson: String,
        @Header("Authorization") authHeader: String,
        @Field("old_password") old_password : String?,
        @Field("new_password") new_password: String?,
        @Field("password_confirmation") password_confirmation: String?
    ): Response<SuccessResponse>



    @POST("logout")
    suspend fun logout(
        @Header("Accept") acceptJson: String,
        @Header("Authorization") authHeader: String
    ): Response<SuccessResponse>

    @POST("delete-account")
    suspend fun deleteAccount(
        @Header("Accept") acceptJson: String,
        @Header("Authorization") authHeader: String
    ): Response<SuccessResponse>

    @FormUrlEncoded
    @POST("contact-us")
    suspend fun contactUs(
        @Header("Accept") acceptJson: String,
        @Header("Authorization") authHeader: String,
        @Field("name") name : String,
        @Field("email") email: String,
        @Field("message") message: String
    ): Response<SuccessResponse>

    @GET("vendor/details")
    suspend fun getVendorProfileDetails(
        @Header("Accept") acceptJson: String,
        @Header("Authorization") authHeader: String,
        @Query("vendor_id") name: Int
    ): Response<VendorProfileDetailsResponse>

    @FormUrlEncoded
    @POST("vendor/follow")
    suspend fun setFollow(
        @Header("Accept") acceptJson: String,
        @Header("Authorization") authHeader: String,
        @Field("vendor_id") vendor_id: Int
    ): Response<FollowResponse>

    @FormUrlEncoded
    @POST("report/bug")
    suspend fun reportBug(
        @Header("Accept") acceptJson: String,
        @Header("Authorization") authHeader: String,
        @Field("message") message: String
    ): Response<SuccessResponse>


    @GET("get-block-users")
    suspend fun getAllBlockedUsers(
        @Header("Accept") acceptJson: String,
        @Header("Authorization") authHeader: String
    ): Response<GetAllBlockedUserResponse>


    @FormUrlEncoded
    @POST("vendor/block")
    suspend fun blockVendor(
        @Header("Accept") acceptJson: String,
        @Header("Authorization") authHeader: String,
        @Field("vendor_id") vendor_id: Int
    ): Response<BlockVendorResponse>




}