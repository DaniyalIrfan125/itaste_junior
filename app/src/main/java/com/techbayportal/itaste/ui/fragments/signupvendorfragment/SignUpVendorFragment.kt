package com.techbayportal.itaste.ui.fragments.signupvendorfragment

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.techbayportal.itaste.BR
import com.techbayportal.itaste.R
import com.techbayportal.itaste.baseclasses.BaseFragment
import com.techbayportal.itaste.databinding.LayoutSignupvendorfragmentBinding
import com.techbayportal.itaste.ui.fragments.homefragment.adapter.HomeRecyclerAdapter
import com.techbayportal.itaste.ui.fragments.signupvendorfragment.adapter.DaysRecyclerAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.layout_homefragment.*
import kotlinx.android.synthetic.main.layout_homefragment.recycler_home
import kotlinx.android.synthetic.main.layout_signupvendorfragment.*

@AndroidEntryPoint
class SignUpVendorFragment :
    BaseFragment<LayoutSignupvendorfragmentBinding, SignupVendorViewModel>() {

    override val layoutId: Int
        get() = R.layout.layout_signupvendorfragment
    override val viewModel: Class<SignupVendorViewModel>
        get() = SignupVendorViewModel::class.java
    override val bindingVariable: Int
        get() = BR.viewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recycler_days.adapter = DaysRecyclerAdapter(
            listOf<String>(
              "Sunday","Monday","Tuesday","Wednesday","Thursday","Friday","Saturday"),
            requireContext()
        )
        recycler_days.layoutManager =  GridLayoutManager(context, 3, LinearLayoutManager.VERTICAL, false)

    }
}