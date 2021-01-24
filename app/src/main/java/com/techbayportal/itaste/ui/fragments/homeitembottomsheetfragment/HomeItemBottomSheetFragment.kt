package com.techbayportal.itaste.ui.fragments.homeitembottomsheetfragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProviders
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.techbayportal.itaste.R
import com.techbayportal.itaste.SharedViewModel
import com.techbayportal.itaste.constants.AppConstants
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_home_item_bottom_sheet.*

@AndroidEntryPoint
class HomeItemBottomSheetFragment : BottomSheetDialogFragment() {

    lateinit var sharedViewModel :SharedViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(BottomSheetDialogFragment.STYLE_NORMAL, R.style.CustomBottomSheetDialogTheme)
        sharedViewModel = ViewModelProviders.of(requireActivity()).get(SharedViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        var view = inflater.inflate(R.layout.fragment_home_item_bottom_sheet, container, false)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        ll_blockVendor.setOnClickListener(View.OnClickListener {
            sharedViewModel.homeItemBottomSheetClickId.postValue(AppConstants.HomeItemBottomSheet.BLOCK_VENDOR)
            dismiss()
        })


        btn_cancel.setOnClickListener(View.OnClickListener {
            dismiss()
        })
    }


}