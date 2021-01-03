package com.techbayportal.itaste.data.remote.reporitory

import com.techbayportal.itaste.data.local.db.AppDao
import com.techbayportal.itaste.data.remote.ApiService
import java.io.File
import javax.inject.Inject

class MainRepository @Inject constructor(
    private val apiService: ApiService,
    localDataSource: AppDao
) {

    suspend fun getPosts() = apiService.getPosts()

    suspend fun signUp(first:String, last:String, username:String, phone:String, profilePic:File, email:String, password:String, role:String)
            = apiService.signUp("application/json",first, last, username, phone, profilePic, email, password, role)

}