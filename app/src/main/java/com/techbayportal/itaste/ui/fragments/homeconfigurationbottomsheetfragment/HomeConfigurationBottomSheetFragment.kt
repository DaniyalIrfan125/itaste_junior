package com.techbayportal.itaste.ui.fragments.homeconfigurationbottomsheetfragment


import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.asLiveData
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.techbayportal.itaste.R
import com.techbayportal.itaste.data.local.datastore.DataStoreProvider
import com.techbayportal.itaste.ui.activities.mainactivity.MainActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_home_configuration_bottom_sheet.*
import kotlinx.android.synthetic.main.fragment_my_profile.*


@AndroidEntryPoint
class HomeConfigurationBottomSheetFragment : BottomSheetDialogFragment() {

    lateinit var dataStoreProvider: DataStoreProvider

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        dataStoreProvider = DataStoreProvider(requireContext())
        setStyle(STYLE_NORMAL, R.style.CustomBottomSheetDialogTheme)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        var view = inflater.inflate(R.layout.fragment_home_configuration_bottom_sheet, container, false)

      //  subscribeToObserveDataStore()

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        subscribeToObserveDataStore()


        ll_settings.setOnClickListener(View.OnClickListener {
            Toast.makeText(requireContext(), "Settings", Toast.LENGTH_SHORT).show()
            Log.d("CLICK:", "Settings")
        })

        ll_change_language.setOnClickListener(View.OnClickListener {

            if (tv_language_name.text == getString(R.string.arabic)){
                activity?.finish()
                (activity as MainActivity?)!!.setLocaleLanguage("ar")
                startActivity(
                    Intent(
                        context,
                        MainActivity::class.java
                    )
                )

            } else{
                activity?.finish()
                (activity as MainActivity?)!!.setLocaleLanguage("en")
                startActivity(
                    Intent(
                        context,
                        MainActivity::class.java
                    )
                )
            }

            Toast.makeText(requireContext(), "change language", Toast.LENGTH_SHORT).show()
            Log.d("CLICK:", "change language")
        })
    }


    private fun subscribeToObserveDataStore() {
        //observing data from data store and showing
        dataStoreProvider.languageFlow.asLiveData().observe(this, Observer {

            if (it != null) {
                if (it == "ar") {
                    tv_language_name.text = getString(R.string.english)


                } else {
                    tv_language_name.text = getString(R.string.arabic)
                }
            } else {
                tv_language_name.text = getString(R.string.english)
            }
        })

    }





}