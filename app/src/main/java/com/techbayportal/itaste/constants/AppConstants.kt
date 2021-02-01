package com.techbayportal.itaste.constants

import androidx.annotation.IntDef
import androidx.annotation.StringDef
import java.lang.annotation.RetentionPolicy


object AppConstants {

    const val PROFILE_PIC_CODE = 9272
    const val USER_PROFILE_PIC_CODE = 92
    const val VENDOR_PROFILE_PIC_CODE = 93

    @StringDef(ApiConfiguration.BASE_URL)
    annotation class ApiConfiguration {
        companion object {

            // const val BASE_URL = "https://itaste.netfans.online/api/v1/"
            const val BASE_URL = "https://itaste.live/api/v1/"
        }
    }

    @IntDef(
        HomeConfigBottomSheet.SETTINGS,
        HomeConfigBottomSheet.TURN_OFF_NOTIFICATION,
        HomeConfigBottomSheet.CONTACT_US,
        HomeConfigBottomSheet.LANGUAGE,
        HomeConfigBottomSheet.LOGOUT,
        HomeConfigBottomSheet.UPDATE_LOCATION
    )
    annotation class HomeConfigBottomSheet {
        companion object {
            const val SETTINGS = 1
            const val TURN_OFF_NOTIFICATION = 2
            const val CONTACT_US = 3
            const val LANGUAGE = 4
            const val LOGOUT = 5
            const val UPDATE_LOCATION = 6

        }
    }

    @IntDef(
        HomeItemBottomSheet.BLOCK_VENDOR
    )
    annotation class HomeItemBottomSheet {
        companion object {
            const val BLOCK_VENDOR = 1

        }
    }

    @IntDef(
        ReportBugDialog.CANCEL,
        ReportBugDialog.SUBMIT
    )
    annotation class ReportBugDialog {
        companion object {
            const val CANCEL = 1
            const val SUBMIT = 2

        }
    }


   /* @StringDef(ReportBugDialogFragment.REPORT_BUG_MESSAGE)
    annotation class ReportBugDialogFragment {
        companion object {
            const val REPORT_BUG_MESSAGE = "Message"
        }
    }*/


    @StringDef(DbConfiguration.DB_NAME)
    annotation class DbConfiguration {
        companion object {
            const val DB_NAME = "BaseProject"
        }
    }

    @StringDef(UserTypeKeys.USER, UserTypeKeys.VENDOR)
    annotation class UserTypeKeys {
        companion object {
            const val USER = "user"
            const val VENDOR = "vendor"
        }
    }

    @StringDef(VerifyOTPTypeKeys.SIGN_UP, VerifyOTPTypeKeys.FORGOT_PASSWORD)
    annotation class VerifyOTPTypeKeys {
        companion object {
            const val SIGN_UP = "sign-up"
            const val FORGOT_PASSWORD = "forget-password"
            const val UPDATE_PHONE = "update-phone"
        }
    }


    @StringDef(RecyclerViewKeys.HOME_RV)
    annotation class RecyclerViewKeys {
        companion object {
            const val HOME_RV = "home_rv"
            const val HOME_RV_IMG_DOTS = "home_rv_img_dots"
            const val HOME_RV_CHILD = "home_rv_child"
            const val BLOCKED_ACCOUNT_RV_UNBLOCK_BUTTON = "unBlocked_account_rv_button"
            const val NOTIFICATION_ITEM = "notification_item"
        }
    }


    @StringDef(DataStore.DATA_STORE_NAME, DataStore.LOCALIZATION_KEY_NAME, DataStore.USER_NAME_KEY)
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
            const val USER_OBJ = "user_obj"
            const val IS_DARK_MODE = "is_dark_mode"
            const val LANGUAGE_PREF = "language_pref"
            const val SWITCH_TO_PREMIUM = "switch_to_premium"
        }
    }

}