package com.techbayportal.itaste.ui.fragments.welcomefragment

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.asLiveData
import com.techbayportal.itaste.BR
import com.techbayportal.itaste.R
import com.techbayportal.itaste.baseclasses.BaseFragment
import com.techbayportal.itaste.data.local.datastore.DataStoreProvider
import com.techbayportal.itaste.databinding.LayoutWelcomefragmentBinding
import com.techbayportal.itaste.ui.activities.mainactivity.MainActivity
import com.techbayportal.itaste.ui.activities.signupactivity.SignupActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.layout_welcomefragment.*


@AndroidEntryPoint
class Welcomefragment : BaseFragment<LayoutWelcomefragmentBinding, WelcomeViewModel>() {

    override val layoutId: Int
        get() = R.layout.layout_welcomefragment
    override val viewModel: Class<WelcomeViewModel>
        get() = WelcomeViewModel::class.java
    override val bindingVariable: Int
        get() = BR.viewModel

    lateinit var dataStoreProvider: DataStoreProvider

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dataStoreProvider = DataStoreProvider(requireContext())
        subscribeToObserveDarkActivation()
    }
    override fun subscribeToNavigationLiveData() {
        super.subscribeToNavigationLiveData()

        mViewModel.onSignInClicked.observe(this, androidx.lifecycle.Observer {

            val intent = Intent(activity, SignupActivity::class.java)
            //intent.setFlagTaskOnHome()
            startActivity(intent)


        })
    }

    private fun subscribeToObserveDarkActivation() {

        //observing data from data store and showing
        dataStoreProvider.darkModeFlow.asLiveData().observe(this, Observer {
            //  switch_darkMode.isChecked = it
            if (it != null) {
                if(it == true){
                    iv_app_logo.setImageDrawable(resources.getDrawable(R.drawable.app_icon_dark))
                }
            }
        })

    }
}