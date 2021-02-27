package com.techbayportal.itaste.ui.fragments.changelanguagefragment

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.navigation.Navigation
import com.google.android.material.snackbar.Snackbar
import com.techbayportal.itaste.BR
import com.techbayportal.itaste.R
import com.techbayportal.itaste.baseclasses.BaseFragment
import com.techbayportal.itaste.databinding.FragmentChangeLanguageBinding
import com.techbayportal.itaste.ui.activities.mainactivity.MainActivity
import com.techbayportal.itaste.ui.activities.signupactivity.SignupActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_change_language.*

@AndroidEntryPoint
class ChangeLanguageFragment : BaseFragment<FragmentChangeLanguageBinding, ChangeLanguageViewModel>() {

    override val layoutId: Int
        get() = R.layout.fragment_change_language
    override val viewModel: Class<ChangeLanguageViewModel>
        get() = ChangeLanguageViewModel::class.java
    override val bindingVariable: Int
        get() = BR.viewModel

    private var languagePreference: String = ""

    lateinit var mView : View


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mView =view

        rg_language.setOnCheckedChangeListener { group, checkedId ->
            if (checkedId == R.id.rb_english) {
                languagePreference = "en"
            }
            if (checkedId == R.id.rb_arabic) {
                languagePreference = "ar"
            }
        }
    }

    private fun validations() {
        if (languagePreference.isNotEmpty()) {
            if (languagePreference == "en") {
                activity?.finish()
                (activity as MainActivity?)!!.setLocaleLanguage("en")
                startActivity(Intent(context, MainActivity::class.java))
            } else if (languagePreference == "ar") {
                activity?.finish()
                (activity as MainActivity?)!!.setLocaleLanguage("ar")
                startActivity(Intent(context, MainActivity::class.java))
            }

        } else {
            Snackbar.make(requireView(), getString(R.string.Select_preffered_language), Snackbar.LENGTH_SHORT).show()
        }

    }

    override fun subscribeToNavigationLiveData() {
        super.subscribeToNavigationLiveData()

        mViewModel.onBackButtonClicked.observe(this, Observer {
            Navigation.findNavController(mView).popBackStack()
        })

        mViewModel.onSaveButtonClicked.observe(this, Observer {
            validations()
        })
    }

}