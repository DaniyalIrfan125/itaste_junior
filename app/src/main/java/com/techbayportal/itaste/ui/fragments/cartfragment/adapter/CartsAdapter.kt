package com.techbayportal.itaste.ui.fragments.cartfragment.adapter

import android.content.Context
import android.opengl.Visibility
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.github.ybq.android.spinkit.SpinKitView
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import com.techbayportal.itaste.R
import com.techbayportal.itaste.data.models.Cart
import com.techbayportal.itaste.data.models.CartPost
import com.techbayportal.itaste.data.models.CartVendor

class CartsAdapter(
    private val list: List<Cart>,
    val context: Context,
    val itemClickListener: ClickItemListener
) :
    RecyclerView.Adapter<CartsAdapter.ViewHolder>() {


    interface ClickItemListener {
        fun addQuantityClicked(position: Int, postId: Int, quantitiy: Int)
        fun minusQuantityClicked(position: Int, postId: Int, quantitiy: Int)
        fun sendMessageClicked(cartPost: CartPost, vendor : CartVendor)
        fun onDeleteItem(postId: Int, position: Int)
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return ViewHolder(inflater, parent)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val cartData: Cart = list[position]
        holder.bind(cartData, position)
    }

    override fun getItemCount(): Int = list.size


    inner class ViewHolder(inflater: LayoutInflater, parent: ViewGroup) :
        RecyclerView.ViewHolder(
            inflater.inflate(
                R.layout.item_layout_cart,
                parent,
                false
            )
        ) {

        private var img_product: ImageView? = null
        private var img_profile: ImageView? = null
        private var tv_profileName: TextView? = null
        private var tv_address: TextView? = null
        private var tv_description: TextView? = null
        private var btn_sendMessage: Button? = null
        private var relative_minusQuantity: RelativeLayout? = null
        private var tv_counterQuantity: TextView? = null
        private var relative_addQuantity: RelativeLayout? = null
        private var sk_postImage: SpinKitView? = null
        private var delete_Cart: RelativeLayout? = null

        init {
            img_product = itemView.findViewById(R.id.img_product)
            img_profile = itemView.findViewById(R.id.img_profile)
            tv_profileName = itemView.findViewById(R.id.tv_profileName)
            tv_address = itemView.findViewById(R.id.tv_address)
            tv_description = itemView.findViewById(R.id.tv_description)
            btn_sendMessage = itemView.findViewById(R.id.btn_sendMessage)
            relative_minusQuantity = itemView.findViewById(R.id.relative_minusQuantity)
            tv_counterQuantity = itemView.findViewById(R.id.tv_counterQuantity)
            relative_addQuantity = itemView.findViewById(R.id.relative_addQuantity)
            sk_postImage = itemView.findViewById(R.id.sk_postImage)
            delete_Cart = itemView.findViewById(R.id.delete_Cart)

        }

        fun bind(getCartData: Cart, position: Int) {

            tv_counterQuantity?.text = getCartData.post.quantity.toString()

            if (!TextUtils.isEmpty(getCartData.post.image)) {
                Picasso.get()
                    .load(getCartData.post.image)
                    .fit()
                    .centerCrop()
                    .into(img_product, object : Callback {
                        override fun onSuccess() {
                            sk_postImage?.visibility = View.GONE
                        }

                        override fun onError(e: Exception) {
                            sk_postImage?.visibility = View.GONE
                        }
                    })
            }


            if (!TextUtils.isEmpty(getCartData.vendor.profile_pic)) {
                Picasso.get()
                    .load(getCartData.vendor.profile_pic)
                    .fit()
                    .centerCrop()
                    .into(img_profile, object : Callback {
                        override fun onSuccess() {

                        }

                        override fun onError(e: Exception) {

                        }
                    })
            }

            tv_profileName?.text = getCartData.vendor.first + " " + getCartData.vendor.last
            tv_address?.text = getCartData.vendor.address
            tv_description?.text = getCartData.post.description


            relative_addQuantity?.setOnClickListener {
                if (tv_counterQuantity?.text.toString().toInt() != 0) {
                    var quantityCart = tv_counterQuantity?.text.toString().toInt() + 1
                    tv_counterQuantity?.text = quantityCart.toString()

                    itemClickListener.addQuantityClicked(
                        position,
                        getCartData.post.id,
                        quantityCart
                    )
                }
            }

            relative_minusQuantity?.setOnClickListener {

                if (tv_counterQuantity?.text.toString().toInt() != 0) {
                    var quantityCart = tv_counterQuantity?.text.toString().toInt() - 1
                    tv_counterQuantity?.text = quantityCart.toString()
                    itemClickListener.minusQuantityClicked(
                        position,
                        getCartData.post.id,
                        quantityCart
                    )
                }
            }

            delete_Cart?.setOnClickListener {
                itemClickListener.onDeleteItem(getCartData.post.id, position)
            }

            btn_sendMessage?.setOnClickListener{
                itemClickListener.sendMessageClicked(getCartData.post, getCartData.vendor)

            }
        }


    }
}

