package com.techbayportal.itaste.data.remote.reporitory

import com.techbayportal.itaste.data.local.db.AppDao
import com.techbayportal.itaste.data.models.GetAllCitiesResponse
import com.techbayportal.itaste.data.models.SignUpResponse
import com.techbayportal.itaste.data.models.getCitiesInputModel
import com.techbayportal.itaste.data.remote.ApiService
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Response
import retrofit2.http.Multipart
import java.io.File
import javax.inject.Inject

class MainRepository @Inject constructor(
    private val apiService: ApiService,
    localDataSource: AppDao
) {

    suspend fun getPosts() = apiService.getPosts()

    suspend fun login(email: String, password: String) =
        apiService.login("application/json", email, password)

    //Sign Up User
    suspend fun signUp(
        first: String,
        last: String,
        username: String,
        phone: String,
        profilePic: File,
        email: String,
        password: String,
        role: String
    ): Response<SignUpResponse> {

        val profilePicRequestBody: RequestBody =
            profilePic.asRequestBody("image/*".toMediaTypeOrNull())
        val profilePicMultiPartBody = MultipartBody.Part.createFormData(
            "profilePic",
            System.currentTimeMillis().toString() + ".png",
            profilePicRequestBody
        )

        return apiService.signUp(
            "application/json",
            first.toRequestBody(MultipartBody.FORM),
            last.toRequestBody(MultipartBody.FORM),
            username.toRequestBody(MultipartBody.FORM),
            phone.toRequestBody(MultipartBody.FORM),
            profilePicMultiPartBody,
            email.toRequestBody(MultipartBody.FORM),
            password.toRequestBody(MultipartBody.FORM),
            role.toRequestBody(MultipartBody.FORM)
        )
    }

    //Sign Up User
    suspend fun signUpVendor(
        first: String,
        last: String,
        username: String,
        profilePic: File,
        phone: String,
        email: String,
        password: String,
        country_id: String,
        city_id: String,
        days_of_week: ArrayList<String>,
        is_deliverable: Boolean,
        password_confirmation: String
    ): Response<SignUpResponse> {

        val profilePicRequestBody: RequestBody =
            profilePic.asRequestBody("image/*".toMediaTypeOrNull())
        val profilePicMultiPartBody = MultipartBody.Part.createFormData(
            "profilePic",
            System.currentTimeMillis().toString() + ".png",
            profilePicRequestBody
        )

        return apiService.signUpVendor(
            "application/json",
            first,
            last,
            username,
            profilePicMultiPartBody,
            phone,
            email,
            password,
            country_id,
            city_id,
            days_of_week,
            is_deliverable,
            password_confirmation
        )
    }


    suspend fun forgotPassword(phone: String) = apiService.forgotPassword("application/json", phone)

    //commented because single api is used for both flows. signup side otp and forget password side otp
    //suspend fun forgotVerifyOtp(code:String,userName:String) = apiService.forgotVerifyOtp("application/json",code,userName)

    suspend fun updatePassword(phone: String, password: String, confirmPassword: String) =
        apiService.updatePassword("application/json", phone, password, confirmPassword)


    suspend fun verifyOtp(code: Int, phone: String, type: String) =
        apiService.verifyOpt("application/json", code, phone, type)

    suspend fun resentOtp(phone: String, type: String) =
        apiService.resentOtp("application/json", phone, type)

    suspend fun getAllCountries() = apiService.getAllCountries()

    suspend fun getAllCities(countryId: Int): Response<GetAllCitiesResponse> {
        return apiService.getAllCities("application/json", countryId)
    }
}