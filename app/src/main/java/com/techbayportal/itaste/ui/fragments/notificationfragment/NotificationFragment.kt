package com.techbayportal.itaste.ui.fragments.notificationfragment

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.techbayportal.itaste.BR
import com.techbayportal.itaste.R
import com.techbayportal.itaste.baseclasses.BaseFragment
import com.techbayportal.itaste.constants.AppConstants
import com.techbayportal.itaste.data.models.NotificationResponse
import com.techbayportal.itaste.data.models.NotificationResponseData
import com.techbayportal.itaste.data.remote.Resource
import com.techbayportal.itaste.databinding.FragmentNotificationBinding
import com.techbayportal.itaste.ui.fragments.notificationfragment.adapter.NotificationFragmentAdapter
import com.techbayportal.itaste.ui.fragments.notificationfragment.itemclicklistener.NotificationRvItemClickListener
import com.techbayportal.itaste.utils.DialogClass
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_bloked_accounts.*
import kotlinx.android.synthetic.main.fragment_notification.*
import kotlinx.android.synthetic.main.item_notification.*
import javax.security.auth.callback.Callback

@AndroidEntryPoint
class NotificationFragment : BaseFragment<FragmentNotificationBinding, NotificationFragmentViewModel>() {

    override val layoutId: Int
        get() = R.layout.fragment_notification
    override val viewModel: Class<NotificationFragmentViewModel>
        get() = NotificationFragmentViewModel::class.java
    override val bindingVariable: Int
        get() = BR.viewModel

    var notificationDataList = ArrayList<NotificationResponseData>()
    lateinit var mView: View
    lateinit var notificationFragmentAdapter: NotificationFragmentAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mViewModel.hitGetNotificationApi()
        subscribeToNetworkLiveData()

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mView = view
        initializing()
    }

    private fun initializing() {
        notificationFragmentAdapter = NotificationFragmentAdapter(requireContext(), notificationDataList,
            object : NotificationFragmentAdapter.ClickItemListener{
                override fun onClicked(notificationResponseData: NotificationResponseData) {

                }

            })
        rvNotifications.adapter = notificationFragmentAdapter
    }

    override fun subscribeToNetworkLiveData() {
        super.subscribeToNetworkLiveData()
        mViewModel.getNotificationsResponse.observe(this, Observer {

            when (it.status) {
                Resource.Status.LOADING -> {
                    loadingDialog.show()
                }
                Resource.Status.SUCCESS -> {
                    loadingDialog.dismiss()
                    notificationDataList.clear()
                    if(!it.data!!.data.isNullOrEmpty()){
                        ll_no_notifications.visibility = View.GONE
                        notificationDataList.addAll(it.data.data)
                        notificationFragmentAdapter.notifyDataSetChanged()
                    }else{
                        ll_no_notifications.visibility = View.VISIBLE
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

            Navigation.findNavController(mView).popBackStack()
        })
    }


}