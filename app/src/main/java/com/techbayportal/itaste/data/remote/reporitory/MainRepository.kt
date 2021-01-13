package com.techbayportal.itaste.data.remote.reporitory

import com.techbayportal.itaste.data.local.db.AppDao
import com.techbayportal.itaste.data.models.*
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
    suspend fun signUp(userModel: UserModel): Response<SignUpResponse> {

        val profilePicRequestBody: RequestBody =
            userModel.profileImage.asRequestBody("image/*".toMediaTypeOrNull())
        val profilePicMultiPartBody = MultipartBody.Part.createFormData(
            "profilePic",
            System.currentTimeMillis().toString() + ".png",
            profilePicRequestBody
        )

        return apiService.signUp(
            "application/json",
            userModel.first,
           // last.toRequestBody(MultipartBody.FORM),
            userModel.last,
            userModel.username,
            userModel.phone,
            profilePicMultiPartBody,
            userModel.email,
            userModel.password,
            userModel.role
        )
    }

    //Sign Up User
    suspend fun signUpVendor(userModel: UserModel): Response<SignUpResponse> {

        val profilePicRequestBody: RequestBody =
            userModel.profileImage.asRequestBody("image/*".toMediaTypeOrNull())
        val profilePicMultiPartBody = MultipartBody.Part.createFormData(
            "profilePic",
            System.currentTimeMillis().toString() + ".png",
            profilePicRequestBody
        )

        return apiService.signUpVendor(
            "application/json",
            userModel.first,
            userModel.last,
            userModel.username,
            profilePicMultiPartBody,
            userModel.phone,
            userModel.email,
            userModel.password,
            userModel.country_id,
            userModel.city_id,
            userModel.days_of_week,
            userModel.is_deliverable,
            userModel.password_confirmation,
            userModel.description
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

    suspend fun updateUserLocation(auth:String, countryId: Int): Response<GetAllCountriesResponse> {
        return apiService.updateUserLocation("application/json",auth, countryId)
    }
}