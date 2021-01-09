package com.techbayportal.itaste.constants

import androidx.annotation.IntDef
import androidx.annotation.StringDef


object AppConstants {

    const val PROFILE_PIC_CODE  = 9272

    @StringDef(ApiConfiguration.BASE_URL)
    annotation class ApiConfiguration {
        companion object {

           // const val BASE_URL = "https://itaste.netfans.online/api/v1/"
            const val BASE_URL = "https://itaste.live/api/v1/"
        }
    }

    @StringDef(DbConfiguration.DB_NAME)
    annotation class DbConfiguration {
        companion object {
            const val DB_NAME = "BaseProject"
        }
    }
    @StringDef(UserTypeKeys.USER,UserTypeKeys.VENDOR)
    annotation class UserTypeKeys {
        companion object {
            const val USER = "user"
            const val VENDOR = "vendor"
        }
    }

    @StringDef(VerifyOTPTypeKeys.SIGN_UP,VerifyOTPTypeKeys.FORGOT_PASSWORD)
    annotation class VerifyOTPTypeKeys {
        companion object {
            const val SIGN_UP = "sign-up"
            const val FORGOT_PASSWORD = "forget-password"
        }
    }


    @StringDef(RecyclerViewKeys.HOME_RV)
    annotation class RecyclerViewKeys {
        companion object {
            const val HOME_RV = "home_rv"
            const val HOME_RV_IMG_DOTS = "home_rv_img_dots"
            const val HOME_RV_CHILD = "home_rv_child"
            const val BLOCKED_ACCOUNT_RV_UNBLOCK_BUTTON = "blocked_account_rv_unblock_button"
            const val NOTIFICATION_ITEM = "notification_item"
        }
    }


    @StringDef(DataStore.DATA_STORE_NAME,DataStore.LOCALIZATION_KEY_NAME,DataStore.USER_NAME_KEY)
    annotation class DataStore {
        companion object {
            const val DATA_STORE_NAME = "BaseProject"
            const val LOCALIZATION_KEY_NAME = "lang"
            const val USER_NAME_KEY = "user_name_key"
            const val DARK_MODE_KEY = "dark_mode_key"
            const val ARABIC_LANGUAGE_KEY = "Arabic"
            const val USER_TYPE_KEY = "user"
            //const val USER_DATA = "user_data"

            //
            const val IS_REMEMBER = "is_remember"
            const val USER_OBJ = "is_remember"
        }
    }

}