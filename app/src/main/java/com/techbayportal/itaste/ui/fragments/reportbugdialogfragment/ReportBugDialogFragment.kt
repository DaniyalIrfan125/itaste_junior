package com.techbayportal.itaste.ui.fragments.reportbugdialogfragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.techbayportal.itaste.R
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_delete_account.*
import kotlinx.android.synthetic.main.fragment_report_bug.*

@AndroidEntryPoint
class ReportBugDialogFragment : DialogFragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
         var rootView = inflater.inflate(R.layout.fragment_report_bug, container, false)

        btn_Cancel.setOnClickListener(View.OnClickListener {
            dismiss()
        })

        btn_submit.setOnClickListener(View.OnClickListener {
            val bugReport: String = tv_bug_report.text.toString()
            Toast.makeText(requireContext(), "Message:: $bugReport", Toast.LENGTH_SHORT).show()
        })
        return rootView
    }

}