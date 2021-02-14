package com.techbayportal.itaste.ui.fragments.cartfragment

import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.techbayportal.itaste.BR
import com.techbayportal.itaste.R
import com.techbayportal.itaste.baseclasses.BaseFragment
import com.techbayportal.itaste.data.models.Cart
import com.techbayportal.itaste.data.remote.Resource
import com.techbayportal.itaste.databinding.LayoutCartfragmentBinding
import com.techbayportal.itaste.ui.fragments.cartfragment.adapter.CartsAdapter
import com.techbayportal.itaste.utils.DialogClass
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.layout_cartfragment.*
import kotlinx.android.synthetic.main.layout_cartfragment.img_back
import kotlinx.android.synthetic.main.layout_savedposts.*


@AndroidEntryPoint
class CartFragment : BaseFragment<LayoutCartfragmentBinding, CartViewModel>(),
    CartsAdapter.ClickItemListener {
    override val layoutId: Int
        get() = R.layout.layout_cartfragment
    override val viewModel: Class<CartViewModel>
        get() = CartViewModel::class.java
    override val bindingVariable: Int
        get() = BR.viewModel


    lateinit var cartsAdapter: CartsAdapter
    var cartsDataArrayList: ArrayList<Cart> = ArrayList()
    var deletePosition = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mViewModel.getCart()
        subscribeToNetworkLiveData()
    }


    override fun subscribeToNetworkLiveData() {
        super.subscribeToNetworkLiveData()


        mViewModel.getCartResponse.observe(this, Observer {
            when (it.status) {
                Resource.Status.LOADING -> {
                    loadingDialog.show()
                }
                Resource.Status.SUCCESS -> {
                    it?.let { it ->
                        loadingDialog.dismiss()

                        cartsDataArrayList.clear()
                        cartsDataArrayList.addAll(it.data!!.data.cart)

                        cartsAdapter.notifyDataSetChanged()

                        if (cartsDataArrayList.size == 0) {
                            linear_emptyCart.visibility = View.VISIBLE
                        } else {
                            linear_emptyCart.visibility = View.GONE
                        }

                    }
                }
                Resource.Status.ERROR -> {
                    loadingDialog.dismiss()
                    DialogClass.errorDialog(requireContext(), it.message!!, baseDarkMode)
                }
            }
        })


        mViewModel.quantityResponse.observe(this, Observer {
            when (it.status) {
                Resource.Status.LOADING -> {
                    loadingDialog.show()
                }
                Resource.Status.SUCCESS -> {
                    it?.let { it ->
                        loadingDialog.dismiss()

                        cartsDataArrayList.clear()
                        cartsDataArrayList.addAll(it.data!!.data.cart)

                        cartsAdapter.notifyDataSetChanged()

                        if (cartsDataArrayList.size == 0) {
                            linear_emptyCart.visibility = View.VISIBLE
                        } else {
                            linear_emptyCart.visibility = View.GONE
                        }

                    }
                }
                Resource.Status.ERROR -> {
                    loadingDialog.dismiss()
                    DialogClass.errorDialog(requireContext(), it.message!!, baseDarkMode)
                }
            }
        })



        mViewModel.removeItemCartResponse.observe(this, Observer {
            when (it.status) {
                Resource.Status.LOADING -> {
                    loadingDialog.show()
                }
                Resource.Status.SUCCESS -> {
                    it?.let { it ->
                        loadingDialog.dismiss()

                        cartsDataArrayList.removeAt(deletePosition)
                        cartsAdapter.notifyItemRemoved(deletePosition)
                        cartsAdapter.notifyItemRangeChanged(
                            deletePosition,
                            cartsDataArrayList.size
                        )


                        if (cartsDataArrayList.size == 0) {
                            linear_emptyCart.visibility = View.VISIBLE
                        } else {
                            linear_emptyCart.visibility = View.GONE
                        }
                    }
                }
                Resource.Status.ERROR -> {
                    loadingDialog.dismiss()
                    DialogClass.errorDialog(requireContext(), it.message!!, baseDarkMode)
                }
            }
        })
    }


    override fun subscribeToNavigationLiveData() {
        super.subscribeToNavigationLiveData()

        mViewModel.onBackButtonClicked.observe(this, Observer {
            Navigation.findNavController(img_back).popBackStack()
        })
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        cartsAdapter = CartsAdapter(
            cartsDataArrayList,
            requireContext(),
            this
        )

        recycler_cart.adapter = cartsAdapter
        recycler_cart.layoutManager = LinearLayoutManager(context)
    }

    override fun addQuantityClicked(position: Int, postId: Int, quantitiy: Int) {

        mViewModel.addRemoveCartQuantity(postId, quantitiy)
    }

    override fun minusQuantityClicked(position: Int, postId: Int, quantitiy: Int) {
        mViewModel.addRemoveCartQuantity(postId, quantitiy)
    }

    override fun sendMessageClicked(postId: Int) {

    }

    override fun onDeleteItem(postId: Int, position: Int) {
        mViewModel.removeCartItem(postId)
        deletePosition = position
    }
}