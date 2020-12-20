package com.techbayportal.itaste.ui.fragments.postdetails4

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.techbayportal.itaste.BR
import com.techbayportal.itaste.R
import com.techbayportal.itaste.baseclasses.BaseFragment
import com.techbayportal.itaste.databinding.FragmentPostDetails4Binding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PostDetails4Fragment : BaseFragment<FragmentPostDetails4Binding, PostDetails4ViewModel>() {
    override val layoutId: Int
        get() = R.layout.fragment_post_details4
    override val viewModel: Class<PostDetails4ViewModel>
        get() = PostDetails4ViewModel::class.java
    override val bindingVariable: Int
        get() = BR.viewModel


}