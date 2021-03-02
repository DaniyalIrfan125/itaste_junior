package com.techbayportal.itaste.ui.fragments.notificationfragment

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.asLiveData
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.techbayportal.itaste.BR
import com.techbayportal.itaste.R
import com.techbayportal.itaste.baseclasses.BaseFragment
import com.techbayportal.itaste.constants.AppConstants
import com.techbayportal.itaste.data.local.datastore.DataStoreProvider
import com.techbayportal.itaste.data.models.GetHomeScreenData
import com.techbayportal.itaste.data.models.GetHomeScreenPostsData
import com.techbayportal.itaste.data.models.NotificationResponse
import com.techbayportal.itaste.data.models.NotificationResponseData
import com.techbayportal.itaste.data.remote.Resource
import com.techbayportal.itaste.databinding.FragmentNotificationBinding
import com.techbayportal.itaste.ui.activities.signupactivity.SignupActivity
import com.techbayportal.itaste.ui.fragments.notificationfragment.adapter.NotificationFragmentAdapter
import com.techbayportal.itaste.ui.fragments.notificationfragment.itemclicklistener.NotificationRvItemClickListener
import com.techbayportal.itaste.utils.DialogClass
import com.techbayportal.itaste.utils.LoginSession
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_bloked_accounts.*
import kotlinx.android.synthetic.main.fragment_notification.*
import kotlinx.android.synthetic.main.item_notification.*
import kotlinx.android.synthetic.main.layout_homefragment.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.security.auth.callback.Callback

@AndroidEntryPoint
class NotificationFragment :
    BaseFragment<FragmentNotificationBinding, NotificationFragmentViewModel>() {

    override val layoutId: Int
        get() = R.layout.fragment_notification
    override val viewModel: Class<NotificationFragmentViewModel>
        get() = NotificationFragmentViewModel::class.java
    override val bindingVariable: Int
        get() = BR.viewModel

    // val loginSession = LoginSession.getInstance().getLoginResponse()

    // lateinit var notificationResponse: NotificationResponse
    var notificationDataList = ArrayList<NotificationResponseData>()
    lateinit var mView: View
    lateinit var notificationFragmentAdapter: NotificationFragmentAdapter

   // lateinit var dataStoreProvider: DataStoreProvider
    private var guestModeNotificationUi: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        subscribeToNetworkLiveData()
       // dataStoreProvider = DataStoreProvider(requireContext())


            GlobalScope.launch {
                val guestMode = mViewModel.dataStoreProvider.guestModeFlow.first()
                if (guestMode) {
                    guestModeNotificationUi = true
                    Timber.d("Guest Mode On")
                } else if (!guestMode) {
                    guestModeNotificationUi = false
                    mViewModel.hitGetNotificationApi()
                    Timber.d("Guest Mode Off")
                }
        }


    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mView = view
        initializing()
        if (guestModeNotificationUi) {
            ll_notificationsGuest.visibility = View.VISIBLE
            ll_no_notifications.visibility = View.GONE
        } else {
            ll_notificationsGuest.visibility = View.GONE
            if (notificationDataList.isEmpty()) {
                ll_no_notifications.visibility = View.VISIBLE
            }
        }

        /*if(this::notificationResponse.isInitialized){
            setData(getHomeScreenResponse)
        }*/

        /*GlobalScope.launch {
            val guestMode = dataStoreProvider.guestModeFlow.first()
            if (guestMode) {
                guestModeNotificationUi = true
                ll_notificationsGuest.visibility = View.VISIBLE
                Timber.d("Guest Mode On")
            }
        }*/


    }


    private fun initializing() {
        notificationFragmentAdapter =
            NotificationFragmentAdapter(requireContext(), notificationDataList,
                object : NotificationFragmentAdapter.ClickItemListener {
                    override fun onClicked(notificationResponseData: NotificationResponseData) {
                        when (notificationResponseData.type) {
                            "post" -> {
                                Timber.d("Move to the Post")
                                sharedViewModel.postId = notificationResponseData.path_id.toInt()
                                Navigation.findNavController(mView)
                                    .navigate(R.id.action_notificationFragment_to_postDetailFragment)
                            }
                            "vendor" -> {
                                Timber.d("Move to the Vendor Profile")
                                // sharedViewModel.postId = notificationResponseData.path_id.toInt()
                                sharedViewModel.vendorHomeScreenData = GetHomeScreenData(
                                    notificationResponseData.path_id.toInt(), "", "", "", "",
                                    arrayListOf<GetHomeScreenPostsData>()
                                )
                                Navigation.findNavController(mView)
                                    .navigate(R.id.action_notificationFragment_to_profileFragment)
                            }
                            else -> {
                                Timber.d("Any Other Notification")
                            }
                        }

                    }

                })
        rvNotifications.adapter = notificationFragmentAdapter
    }

    private fun navigateToLoginScreen() {
        val intent = Intent(activity, SignupActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
        startActivity(intent)


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
                    if (!it.data!!.data.isNullOrEmpty()) {
                        ll_no_notifications.visibility = View.GONE
                        ll_notificationsGuest.visibility = View.GONE
                        notificationDataList.addAll(it.data.data)
                        notificationFragmentAdapter.notifyDataSetChanged()
                    } else {
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

        mViewModel.onSignInButtonClicked.observe(this, Observer {

            navigateToLoginScreen()

        })
    }


}