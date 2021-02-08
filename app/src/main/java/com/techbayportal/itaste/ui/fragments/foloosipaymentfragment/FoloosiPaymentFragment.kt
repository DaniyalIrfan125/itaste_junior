package com.techbayportal.itaste.ui.fragments.foloosipaymentfragment

import android.content.Intent
import android.content.Intent.getIntent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.Observable
import androidx.databinding.library.baseAdapters.BR
import com.foloosi.core.FPayListener
import com.foloosi.core.FoloosiLog
import com.foloosi.core.FoloosiPay
import com.foloosi.models.Customer
import com.foloosi.models.OrderData
import com.techbayportal.itaste.R
import com.techbayportal.itaste.baseclasses.BaseFragment
import com.techbayportal.itaste.constants.AppConstants
import com.techbayportal.itaste.data.remote.Resource
import com.techbayportal.itaste.data.remote.Resource.Companion.loading
import com.techbayportal.itaste.databinding.FragmentFoloosiPaymentBinding
import com.techbayportal.itaste.databinding.LayoutHomefragmentBinding
import com.techbayportal.itaste.ui.activities.mainactivity.MainActivity
import com.techbayportal.itaste.utils.DialogClass
import com.techbayportal.itaste.utils.LoginSession
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import java.util.*

@AndroidEntryPoint
class FoloosiPaymentFragment : BaseFragment<FragmentFoloosiPaymentBinding, FoloosiPaymentViewModel>() , FPayListener {

    override val layoutId: Int
        get() = R.layout.fragment_foloosi_payment
    override val viewModel: Class<FoloosiPaymentViewModel>
        get() = FoloosiPaymentViewModel::class.java
    override val bindingVariable: Int
        get() = BR.viewModel

    lateinit var mView: View
    private var total = 0.0
    var orderId = 0
    val loginSession = LoginSession.getInstance().getLoginResponse()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //total = getIntent().getDoubleExtra(AppConstants.DataStore.DURATION,0.0)
        initialisingPayment()
        subscribeToNetworkLiveData()

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mView = view
    }

    /**4
     * Initialising foloosi and making payment
     */
    private fun initialisingPayment() {
        try {
            FoloosiLog.setLogVisible(true)
            //test
            FoloosiPay.init(requireActivity(),getString(R.string.foloosi_key))
            FoloosiPay.setPaymentListener(this)
            val orderData = OrderData() // Foloosi Order Data Model Class Instance
            total = sharedViewModel.packagePrice.toDouble()
            orderData.orderAmount = total // in double format ##,###.##
            val rand = Random()
            orderId = rand.nextInt(100000)
            orderData.customColor = "#ff962d" // make payment page loading color as app color.
            orderData.orderId = orderId.toString() // unique order id.
            orderData.orderDescription = sharedViewModel.packageType // any description.
            orderData.currencyCode = "AED"
            val customer = Customer()
            customer.name = loginSession!!.data.first + " "+ loginSession.data.last
            customer.email = "khanAdnan496@gmail.com"
            customer.mobile = loginSession.data.phone
           // customer.city = loginSession.data.city_id
            orderData.customer = customer
            FoloosiPay.makePayment(orderData)
        } catch (e: Exception) {
            Toast.makeText(requireContext(), e.message, Toast.LENGTH_LONG).show()
        }
    }

    override fun onTransactionCancelled() {
        Timber.d("Transaction Cancelled")
        DialogClass.errorDialog(requireContext(),"Ooops!\n Transaction Cancelled", baseDarkMode)
    }

    override fun onTransactionSuccess(transactionId: String?) {
        Timber.d("Transaction Id $transactionId")
       // navigateAfterSuccessFulPayment()
       // Toast.makeText(requireContext(), "Payment Successful", Toast.LENGTH_SHORT).show()
        mViewModel.hitCheckOutApi(orderId,sharedViewModel.packageId,total.toInt())
    }

    override fun onTransactionFailure(error: String?) {
        Timber.d("Transaction Error $error")
        DialogClass.errorDialog(requireContext(),error!!, baseDarkMode)
    }

    private fun navigateAfterSuccessFulPayment(){
        val intent = Intent(requireContext(), MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
        startActivity(intent)
    }

    override fun subscribeToNetworkLiveData() {
        super.subscribeToNetworkLiveData()

        mViewModel.getCheckoutResponse.observe(this, androidx.lifecycle.Observer {
            when (it.status) {
                Resource.Status.LOADING -> {
                    loading(true)
                }
                Resource.Status.SUCCESS -> {
                    loading(false)

                    Toast.makeText(requireContext(), "CheckOut Api hit", Toast.LENGTH_SHORT)
                        .show()
                }
                Resource.Status.ERROR -> {
                    loading(false)
                    DialogClass.errorDialog(requireContext(), it.message!!, baseDarkMode)
                }

            }
        })
    }


}