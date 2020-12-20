package com.techbayportal.itaste.ui.fragments.reportabug

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatDialogFragment
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import com.google.android.material.dialog.MaterialDialogs
import com.techbayportal.itaste.R
import kotlinx.android.synthetic.main.fragment_delete_account.*
import kotlinx.android.synthetic.main.layout_report_bug.*


class ReportBugFragment : DialogFragment() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
         var rootView = inflater.inflate(R.layout.layout_report_bug, container, false)

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