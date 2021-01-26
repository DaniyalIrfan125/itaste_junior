package com.techbayportal.itaste.utils

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.View
import android.view.Window
import android.widget.Button
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.recyclerview.widget.RecyclerView
import com.techbayportal.itaste.R
import com.techbayportal.itaste.data.local.datastore.DataStoreProvider
import kotlinx.android.synthetic.main.fragment_my_profile.*

class DialogClass {

    companion object {

        fun loadingDialog(context: Context):Dialog {
            val dialog = Dialog(context)

            dialog.setContentView(R.layout.dialog_layout_loading)
            dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dialog.setCancelable(false)
            return dialog
        }

        fun errorDialog(context: Context,errorMessage:String ,isDarkMode : Boolean) {
            val dialog = Dialog(context)
            dialog.setContentView(R.layout.layout_error_loading_page)
            val titleText = dialog.findViewById<TextView>(R.id.tv_title)
            val btnOk = dialog.findViewById<Button>(R.id.btn_ok)
            titleText.text = context.getString(R.string.error_loading_page)

            val iv_image = dialog.findViewById<ImageView>(R.id.iv_image)

            btnOk.setOnClickListener {v: View? ->
                dialog.dismiss()
            }

            if (isDarkMode) {
                iv_image.setImageResource(R.drawable.icon_error_img_white)
            } else {
                iv_image.setImageResource(R.drawable.icon_error_img)
            }

            val errorTextMessage = dialog.findViewById<TextView>(R.id.tv_description)
            errorTextMessage.text = errorMessage

          //  val dialogButton = dialog.findViewById<Button>(R.id.btn_dialog_okay)
//        // if button is clicked, close the custom dialog
          //  dialogButton.setOnClickListener { v: View? -> dialog.dismiss() }
            dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dialog.show()
        }

        fun successDialog(context: Context,successMessage:String ,isDarkMode : Boolean) {
            val dialog = Dialog(context)
            dialog.setContentView(R.layout.layout_error_loading_page)



            // set the custom dialog components - text, image and button
            val titleText = dialog.findViewById<TextView>(R.id.tv_title_success)
            val descText = dialog.findViewById<TextView>(R.id.tv_title_success)
            val btnOk = dialog.findViewById<Button>(R.id.btn_ok_success)
            titleText.text = context.getString(R.string.you_are_all_set)

            val iv_image = dialog.findViewById<ImageView>(R.id.iv_image_success)

            btnOk.setOnClickListener {v: View? ->
                dialog.dismiss()
            }

            if (isDarkMode) {
                iv_image.setImageResource(R.drawable.icon_success_dialog_darkmode)
            } else {
                iv_image.setImageResource(R.drawable.icon_success_dialog)
            }

            val successTextMessage = dialog.findViewById<TextView>(R.id.tv_description)
            successTextMessage.text = successMessage

            dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dialog.show()
        }
    }
}