package com.techbayportal.itaste.utils

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.View
import android.widget.Button
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.techbayportal.itaste.R

class DialogClass {

    interface ClickListener {
        fun onSingalItemBtnClick(dialog: Dialog)
        fun onMultipleItemBtnClick(dialog: Dialog)
        fun onAuctionItemBtnClick(dialog: Dialog)
        fun onCloseBtn()
    }

    companion object {

        fun loadingDialog(context: Context):Dialog {
            val dialog = Dialog(context)
            dialog.setContentView(R.layout.dialog_layout_loading)
            dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dialog.setCancelable(false)
            return dialog
        }

        fun errorDialog(context: Context,errorMessage:String) {
            val dialog = Dialog(context)
            dialog.setContentView(R.layout.layout_error_loading_page)



            // set the custom dialog components - text, image and button
            val titleText = dialog.findViewById<TextView>(R.id.tv_title)
            titleText.text = context.getString(R.string.error_loading_page)

            val errorTextMessage = dialog.findViewById<TextView>(R.id.tv_description)
            errorTextMessage.text = errorMessage

          //  val dialogButton = dialog.findViewById<Button>(R.id.btn_dialog_okay)
//        // if button is clicked, close the custom dialog
          //  dialogButton.setOnClickListener { v: View? -> dialog.dismiss() }
            dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dialog.show()
        }


       /* fun placeBidDialog(context: Context) {
            val dialog = Dialog(context)
            dialog.setContentView(R.layout.dialog_layout_place_bid)

            val recyclerView = dialog.findViewById<RecyclerView>(R.id.recyclerPostBidItems)
            val adapter = PostBidAdapter()
            recyclerView.adapter = adapter

            val dialogButton = dialog.findViewById<RelativeLayout>(R.id.closeBtn)
//        // if button is clicked, close the custom dialog
            dialogButton.setOnClickListener { v: View? -> dialog.dismiss() }
            dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dialog.show()
        }


        fun addPostDialog(context: Context, clickListener: ClickListener):Dialog {
            val dialog = Dialog(context)
            dialog.setContentView(R.layout.dialog_layout_post)

            val closeButton = dialog.findViewById<RelativeLayout>(R.id.closeBtn)
            val sellItemButton = dialog.findViewById<Button>(R.id.sellItemBtn)
            val auctionItemButton = dialog.findViewById<Button>(R.id.auctionItemBtn)
//        // if button is clicked, close the custom dialog
            closeButton.setOnClickListener { v: View? ->
                dialog.dismiss()
                clickListener.onCloseBtn()
            }

            sellItemButton.setOnClickListener { v: View? ->
                sellItemDialog(dialog, context, clickListener)
            }

            auctionItemButton.setOnClickListener { v: View? ->
                clickListener.onAuctionItemBtnClick(dialog)
            }

            dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            return dialog
        }

        fun sellItemDialog(addPostDialog: Dialog, context: Context, clickListener: ClickListener) {

            val dialog = Dialog(context)
            dialog.setContentView(R.layout.dialog_layout_sell_item)

            val closeButton = dialog.findViewById<RelativeLayout>(R.id.closeBtn)
            val singalSellItemBtn = dialog.findViewById<Button>(R.id.singalSellItemBtn)
            val multiplySellItemBtn = dialog.findViewById<Button>(R.id.multiplySellItemBtn)
//        // if button is clicked, close the custom dialog
            closeButton.setOnClickListener { v: View? -> dialog.dismiss() }

            singalSellItemBtn.setOnClickListener { v: View? ->
                clickListener.onSingalItemBtnClick(dialog)
                addPostDialog.dismiss()
            }

            multiplySellItemBtn.setOnClickListener { v: View? ->
                clickListener.onMultipleItemBtnClick(dialog)
                addPostDialog.dismiss()
            }

            dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dialog.show()
        }*/
    }
}