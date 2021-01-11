package com.techbayportal.itaste.baseclasses

import android.content.res.Configuration
import android.os.Bundle
import androidx.annotation.LayoutRes
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModelProviders
import androidx.lifecycle.asLiveData
import androidx.lifecycle.observe
import com.techbayportal.itaste.R
import com.techbayportal.itaste.data.local.datastore.DataStoreProvider
import kotlinx.android.synthetic.main.fragment_home_configuration_bottom_sheet.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.util.*


abstract class BaseActivity<T : ViewDataBinding, V : BaseViewModel> : AppCompatActivity() {

    private lateinit var mViewDataBinding: T
    protected lateinit var mViewModel: V

    lateinit var dataStoreProvider: DataStoreProvider

    /**
     * viewModel variable that will get value from activity which it will implement this
     * we will use this variable viewModel to bind with view through databinding
     */
    abstract val viewModel: Class<V>

    /**
     * layoutId variable to get layout value from activity which will implement this layoutId
     * we will use this layoutId for databinding
     */
    @get:LayoutRes
    abstract val layoutId: Int

    /**
     * bindingVariable which will bind with view
     */

    abstract val bindingVariable: Int

    override fun onCreate(savedInstanceState: Bundle?) {
        loadLocale()
        super.onCreate(savedInstanceState)
        databindingWithViewModel()


    }


    /**
     * Function to perform databinding and attaching viewmodel with view
     */
    private fun databindingWithViewModel() {
        mViewDataBinding = DataBindingUtil.setContentView(this, layoutId)
        mViewModel = ViewModelProviders.of(this).get(viewModel)
        mViewDataBinding.setVariable(bindingVariable, mViewModel)
        mViewDataBinding.executePendingBindings()

    }

    fun setLocaleLanguage(language: String) {
        val locale = Locale(language)
        Locale.setDefault(locale)
        val configuration: Configuration = resources.configuration
        configuration.setLocale(locale)
        configuration.setLayoutDirection(locale)
        resources.updateConfiguration(configuration, resources.displayMetrics)

        GlobalScope.launch {
            dataStoreProvider.storeLanguage(language)
        }
    }

    open fun loadLocale() {
        dataStoreProvider = DataStoreProvider(this)
        dataStoreProvider.languageFlow.asLiveData().observe(this, androidx.lifecycle.Observer {
            it?.let { setLocaleLanguage(it) }

        })

    }

    /*private fun subscribeToObserveLanguageActivation() {
        //observing data from data store and showing
        dataStoreProvider.languageFlow.asLiveData().observe(this, androidx.lifecycle.Observer {

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

    }*/

}