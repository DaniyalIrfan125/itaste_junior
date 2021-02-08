package com.techbayportal.itaste.ui.fragments.postdetailbottomsheetfragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.techbayportal.itaste.R
import com.techbayportal.itaste.SharedViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_post_detail_bottom_sheet.*

@AndroidEntryPoint
class PostDetailBottomSheetFragment : BottomSheetDialogFragment() {

    private lateinit var sharedViewModel: SharedViewModel
    private lateinit var mView: View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.CustomBottomSheetDialogTheme)

        sharedViewModel = ViewModelProviders.of(requireActivity()).get(SharedViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mView = inflater.inflate(R.layout.fragment_post_detail_bottom_sheet, container, false)

        return mView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)



        btn_cancel.setOnClickListener {
            dismiss()
        }

        linear_edit.setOnClickListener {


            sharedViewModel.isEditBottomSheetClicked.value = true
            dismiss()

        }


        linear_delete.setOnClickListener {
            sharedViewModel.isPostDetailDeleteClicked .value= true
            dismiss()
        }

    }


}