package com.techbayportal.itaste.utils

import android.text.TextUtils
import android.util.Patterns
import com.google.gson.Gson
import com.techbayportal.itaste.data.models.ResponseError
import com.techbayportal.itaste.data.remote.Resource
import kotlinx.coroutines.flow.FlowCollector
import retrofit2.HttpException

fun String.isValidEmail() =
    !TextUtils.isEmpty(this) && Patterns.EMAIL_ADDRESS.matcher(this).matches()

private suspend fun <T> validateError(
    flowCollector: FlowCollector<Resource<T>>,
    e: Exception
) {
    if (e is HttpException) {
        when {
            e.code() == 500 -> {
                flowCollector.emit(Resource.error(e.message()))
            }
            e.code() == 404 -> {
                flowCollector.emit(Resource.error(e.message()))
            }
            e.code() == 400 -> {
                flowCollector.emit(Resource.error(e.message()))
            }
            else -> {
                val errorMessagesJson =
                    e.response()?.errorBody()?.source()?.buffer?.readUtf8()!!
                val gsonObj = Gson()
                gsonObj.serializeNulls()
                val errorObj =
                    gsonObj.fromJson(errorMessagesJson, ResponseError::class.java)
//                flowCollector.emit(Resource.error("",errorObj))
            }
        }
    } else flowCollector.emit(Resource.error("" + e.message))
}
val gsonObj = Gson()

fun extractErrorMessage(errorMessagesJson : String):String{
    gsonObj.serializeNulls()
    val errorObj = gsonObj.fromJson(errorMessagesJson, ResponseError::class.java)
    val msg = errorObj.errors.values
    var errorMessages = ""
    msg.forEach {
        it.forEach{
            errorMessages += "\n${it}"
        }
    }
    return errorMessages
}