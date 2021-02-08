package com.techbayportal.itaste.ui.fragments.reportbugdialogfragment

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.google.android.material.snackbar.Snackbar
import com.techbayportal.itaste.R
import com.techbayportal.itaste.SharedViewModel
import com.techbayportal.itaste.constants.AppConstants
import com.techbayportal.itaste.utils.LoginSession
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_report_bug.*
import kotlinx.android.synthetic.main.layout_signupfragment.*

class ReportBugDialogFragment : DialogFragment() {

    lateinit var sharedViewModel : SharedViewModel
    val loginSession = LoginSession.getInstance().getLoginResponse()
    lateinit var mView: View

   // private var bugMessageReport = MutableLiveData<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        sharedViewModel = ViewModelProviders.of(requireActivity()).get(SharedViewModel::class.java)
    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
         var rootView = inflater.inflate(R.layout.fragment_report_bug, container, false)
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mView = view
        fieldTextWatcher()

        tv_bug_report.addTextChangedListener(object : TextWatcher {

            override fun afterTextChanged(s: Editable) {}

            override fun beforeTextChanged(s: CharSequence, start: Int,
                                           count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence, start: Int,
                                       before: Int, count: Int) {
                sharedViewModel.bugReportMessage = s.toString()
            }
        })

        ll_cancel.setOnClickListener(View.OnClickListener {
            dismiss()
        })

        btn_submit.setOnClickListener(View.OnClickListener {

            fieldValidations()
        })
    }

    private fun fieldTextWatcher() {

        tv_bug_report.doOnTextChanged { text, start, before, count ->
            tv_errorReportMessage.visibility = View.GONE
            tv_bug_report.background = ContextCompat.getDrawable(requireContext(), R.drawable.ed_states_bug)
        }
    }

    fun fieldValidations() {
        if (tv_bug_report.text.isNotEmpty()) {
            tv_bug_report.setText(sharedViewModel.bugReportMessage)
            sharedViewModel.reportBugButtonsClicked.postValue(AppConstants.ReportBugDialog.SUBMIT)
        }else{
            tv_errorReportMessage.visibility = View.VISIBLE
            tv_bug_report.background = ContextCompat.getDrawable(requireContext(), R.drawable.ed_errorboundary_bug)
            tv_errorReportMessage.text = getString(R.string.writereport)
        }
    }




}