package com.techbayportal.itaste.ui.fragments.choosepackagefragment

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.asLiveData
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.techbayportal.itaste.BR
import com.techbayportal.itaste.R
import com.techbayportal.itaste.baseclasses.BaseFragment
import com.techbayportal.itaste.data.local.datastore.DataStoreProvider
import com.techbayportal.itaste.data.models.NotificationResponseData
import com.techbayportal.itaste.data.models.PackagesResponse
import com.techbayportal.itaste.data.models.PackagesResponseData
import com.techbayportal.itaste.data.remote.Resource
import com.techbayportal.itaste.databinding.LayoutChoosepackagefragmentBinding
import com.techbayportal.itaste.ui.fragments.choosepackagefragment.adapter.ChoosePakageAdapter
import com.techbayportal.itaste.ui.fragments.notificationfragment.adapter.NotificationFragmentAdapter
import com.techbayportal.itaste.utils.DialogClass
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_notification.*
import kotlinx.android.synthetic.main.layout_changepasswordfragment.*
import kotlinx.android.synthetic.main.layout_choosepackagefragment.*
import kotlinx.android.synthetic.main.layout_loginfragment.*

@AndroidEntryPoint
class ChoosePakageFragment : BaseFragment<LayoutChoosepackagefragmentBinding,ChoosePackageViewModel>(){
    override val layoutId: Int
        get() = R.layout.layout_choosepackagefragment
    override val viewModel: Class<ChoosePackageViewModel>
        get() = ChoosePackageViewModel::class.java
    override val bindingVariable: Int
        get() = BR.viewModel

    lateinit var choosePakageAdapter: ChoosePakageAdapter
    var packagesList = ArrayList<PackagesResponseData>()
    lateinit var mView: View

    var darkMode :Boolean = false
    lateinit var dataStoreProvider: DataStoreProvider

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mViewModel.hitGetPackagesApi()
        subscribeToNetworkLiveData()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mView = view
        dataStoreProvider = DataStoreProvider(requireContext())
        subscribeToObserveDarkActivation()
        initializing()

    }

    private fun initializing() {
        choosePakageAdapter = ChoosePakageAdapter(packagesList,requireContext(),
            object : ChoosePakageAdapter.ClickItemListener{
                override fun onClicked(packagesResponseData: PackagesResponseData) {
                    Toast.makeText(requireContext(), "Packages get ${packagesResponseData.name}", Toast.LENGTH_SHORT).show()
                }


            })

        recycler_choosepakage.adapter = choosePakageAdapter
    }


    override fun subscribeToNetworkLiveData() {
        super.subscribeToNetworkLiveData()

        mViewModel.getPackagesResponse.observe(this, Observer {

            when (it.status) {
                Resource.Status.LOADING -> {
                    loadingDialog.show()
                }
                Resource.Status.SUCCESS -> {
                    loadingDialog.dismiss()
                    packagesList.clear()
                    if(!it.data!!.data.isNullOrEmpty()){
                        packagesList.addAll(it.data.data)
                        choosePakageAdapter.notifyDataSetChanged()
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

        mViewModel.onProceedButtonClicked.observe(this, Observer {

        })

        mViewModel.onCancelButtonClicked.observe(this, Observer {
            Navigation.findNavController(mView).popBackStack()
        })
    }

    private fun subscribeToObserveDarkActivation() {

        //observing data from data store and showing
        dataStoreProvider.darkModeFlow.asLiveData().observe(this, Observer {
            if (it != null) {
                if(it == true){
                    iv_iconPayment.setImageResource(R.drawable.icon_choose_package_dark)
                    darkMode
                }else{
                    iv_iconPayment.setImageResource(R.drawable.icon_choosepakage)
                    !darkMode
                }
            }
        })

    }
}