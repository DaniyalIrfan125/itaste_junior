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
           // userModel.days_of_week,
           // userModel.is_deliverable,
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

    suspend fun verifyOtpForUpdatePhone(auth: String, code: Int, phone: String, type: String) =
        apiService.verifyOptForUpdatePhone("application/json", auth, code, phone, type)

    suspend fun resentOtp(phone: String, type: String) =
        apiService.resentOtp("application/json", phone, type)


    suspend fun resentOtpUpdatePhone(auth: String, phone: String, type: String) =
        apiService.resentOtpUpdatePhone("application/json", auth, phone, type)

    // suspend fun getAllCountries() = apiService.getAllCountries()

    suspend fun getAllCountriesForHome(auth: String) =
        apiService.getAllCountriesForHome("application/json", auth)

    suspend fun getAllCities(auth: String, countryId: Int): Response<GetAllCitiesResponse> {
        return apiService.getAllCities("application/json", auth, countryId)
    }

    suspend fun updateUserLocation(
        auth: String,
        countryId: Int
    ): Response<GetAllCountriesResponse> {
        return apiService.updateUserLocation("application/json", auth, countryId)
    }

    suspend fun getUserPersonalProfile(auth: String): Response<UserPersonalProfileResponse> {
        return apiService.getUserPersonalProfile("application/json", auth)
    }


    suspend fun getVendorPersonalProfile(auth: String): Response<VendorPersonalProfileResponse> {
        return apiService.getVendorPersonalProfile("application/json", auth)
    }

    suspend fun updateUserPersonalProfile(
        auth: String,
        first: String,
        last: String,
        email: String,
        phone: String,
        profilePic: File?
    ): Response<UserPersonalProfileResponse> {

        val profilePicRequestBody: RequestBody? =
            profilePic?.asRequestBody("image/*".toMediaTypeOrNull())
        val profilePicMultiPartBody = profilePicRequestBody?.let {
            MultipartBody.Part.createFormData(
                "profilePic",
                System.currentTimeMillis().toString() + ".png",
                it
            )
        }
        if (profilePic == null) {
            return apiService.updateUserPersonalProfileWithoutPic(
                "application/json",
                auth,
                first,
                last,
                email,
                phone
            )
        } else {
            return apiService.updateUserPersonalProfile(
                "application/json",
                auth,
                first,
                last,
                email,
                phone,
                profilePicMultiPartBody!!
            )
        }


    }

    suspend fun updateVendorPersonalProfile(
        auth: String,
        first_name: String,
        last_name: String,
        description: String,
        phone: String,
        email: String,
        profilePic: File?,
        country_id: Int,
        city_id: Int

    ): Response<VendorUpdateProfileResponse> {
        val profilePicRequestBody: RequestBody? =
            profilePic?.asRequestBody("image/*".toMediaTypeOrNull())
        val profilePicMultiPartBody = profilePicRequestBody?.let {
            MultipartBody.Part.createFormData(
                "profilePic",
                System.currentTimeMillis().toString() + ".png",
                it
            )
        }
        if (profilePic == null) {
            return apiService.updateVendorPersonalProfileWithoutPic(
                "application/json",
                auth,
                first_name,
                last_name,
                description,
                phone,
                email,
                country_id,
                city_id
            )
        } else {
            return apiService.updateVendorPersonalProfile(
                "application/json",
                auth,
                first_name,
                last_name,
                description,
                phone,
                email,
                profilePicMultiPartBody!!,
                country_id,
                city_id
            )
        }
    }

    suspend fun switchToPremium(
        auth: String,
        country_id: Int,
        city_id: Int,
       // days_of_week: ArrayList<String>,
      //  is_deliverable: Int,
        description: String

    ): Response<SuccessResponse> {

        return apiService.switchToPremium(
            "application/json",
            auth,
            country_id,
            city_id,
          //  days_of_week,
         //   is_deliverable,
            description
        )
    }

    suspend fun changePassword(
        auth: String,
        old_password: String,
        new_password: String,
        password_confirmation: String
    ) =
        apiService.changePassword(
            "application/json",
            auth,
            old_password,
            new_password,
            password_confirmation
        )

    suspend fun logout(auth: String) = apiService.logout("application/json", auth)

    suspend fun deleteAccount(auth: String) = apiService.deleteAccount("application/json", auth)

    suspend fun contactUs(auth: String, name: String, email: String, message: String) =
        apiService.contactUs("application/json", auth, name, email, message)

    suspend fun getVendorProfileDetails(
        auth: String,
        vendorId: Int
    ): Response<VendorProfileDetailsResponse> {
        return apiService.getVendorProfileDetails("application/json", auth, vendorId)
    }

    suspend fun setFollow(auth: String, vendorId: Int): Response<FollowResponse> {
        return apiService.setFollow("application/json", auth, vendorId)
    }

    suspend fun reportBug(auth: String, message: String): Response<SuccessResponse> {
        return apiService.reportBug("application/json", auth, message)
    }

    suspend fun getAllBlockedUsers(auth: String): Response<GetAllBlockedUserResponse> {
        return apiService.getAllBlockedUsers("application/json", auth)
    }

    suspend fun blockVendor(auth: String, vendorId: Int): Response<BlockVendorResponse> {
        return apiService.blockVendor("application/json", auth, vendorId)
    }

    suspend fun getAllCategories(auth: String): Response<GetAllCategoriesResponse> {
        return apiService.getAllCategories("application/json", auth)
    }

    suspend fun getHomeScreenInfo(auth: String): Response<GetHomeScreenResponse> {
        return apiService.getHomeScreenInfo("application/json", auth)
    }

    suspend fun getNotifications(auth: String): Response<NotificationResponse> {
        return apiService.getNotifications("application/json", auth)
    }

    suspend fun getPackages(): Response<PackagesResponse> {
        return apiService.getPackages()
    }
}