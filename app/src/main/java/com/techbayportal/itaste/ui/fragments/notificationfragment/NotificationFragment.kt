package com.techbayportal.itaste.ui.fragments.notificationfragment

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.techbayportal.itaste.BR
import com.techbayportal.itaste.R
import com.techbayportal.itaste.baseclasses.BaseFragment
import com.techbayportal.itaste.databinding.FragmentNotificationBinding
import com.techbayportal.itaste.ui.fragments.notificationfragment.adapter.NotificationFragmentAdapter
import com.techbayportal.itaste.ui.fragments.notificationfragment.itemclicklistener.NotificationRvItemClickListener
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.item_notification.*

@AndroidEntryPoint
class NotificationFragment : BaseFragment<FragmentNotificationBinding, NotificationFragmentViewModel>(), NotificationRvItemClickListener {

    override val layoutId: Int
        get() = R.layout.fragment_notification
    override val viewModel: Class<NotificationFragmentViewModel>
        get() = NotificationFragmentViewModel::class.java
    override val bindingVariable: Int
        get() = BR.viewModel


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mViewDataBinding.rvNotifications.adapter = NotificationFragmentAdapter(this)
    }

    override fun onItemClickListener() {
        Toast.makeText(context, "Notification Clicked", Toast.LENGTH_SHORT).show()
        rvNotificationItem.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.colorBlack))
    }


}