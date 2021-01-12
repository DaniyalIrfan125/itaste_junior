package com.techbayportal.itaste.baseclasses

import android.app.Dialog
import android.content.Context
import android.graphics.Point
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewParent
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.widget.NestedScrollView
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.lifecycle.asLiveData
import com.techbayportal.itaste.SharedViewModel
import com.techbayportal.itaste.data.local.datastore.DataStoreProvider
import com.techbayportal.itaste.ui.activities.mainactivity.MainActivity
import com.techbayportal.itaste.utils.DialogClass


abstract class BaseFragment<T : ViewDataBinding, V : BaseViewModel> : Fragment() {

    lateinit var sharedViewModel: SharedViewModel

    private var mActivity: BaseActivity<*, *>? = null
    lateinit var mViewDataBinding: T
    protected lateinit var mViewModel: V
    protected lateinit var loadingDialog: Dialog

    abstract val layoutId: Int
    abstract val viewModel: Class<V>
    abstract val bindingVariable: Int

    lateinit var dataStoreProviderBase: DataStoreProvider

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mViewDataBinding = DataBindingUtil.inflate(inflater, layoutId, container, false)
        return mViewDataBinding.root

    }

    /*fun Intent.setFlagTaskOnHome() {
        flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_TASK_ON_HOME
    }*/

    /*fun Intent.clearStack() {
        flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
    }*/

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mViewDataBinding.setVariable(bindingVariable, mViewModel)
        mViewDataBinding.lifecycleOwner = this
        mViewDataBinding.executePendingBindings()
        dataStoreProviderBase = DataStoreProvider(requireContext())
        loadingDialog = DialogClass.loadingDialog(requireContext())


        subscribeToShareLiveData()
        subscribeToNavigationLiveData()
        subscribeToViewLiveData()

        subscribeToObserveDataStore()


    }

    private fun subscribeToObserveDataStore() {

        //observing data from data store and showing
        dataStoreProviderBase.darkModeFlow.asLiveData().observe(this, Observer {
            if (it) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                sharedViewModel.isDarkMode

            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                !sharedViewModel.isDarkMode

            }
        })

    }

    open fun subscribeToViewLiveData() {

        //observe view data

    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is BaseActivity<*, *>)
            this.mActivity = context
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mViewModel = ViewModelProviders.of(this).get(viewModel)
        sharedViewModel = ViewModelProviders.of(requireActivity()).get(SharedViewModel::class.java)
        loadingDialog = DialogClass.loadingDialog(requireContext())
    }


    open fun subscribeToShareLiveData() {

    }
    open fun subscribeToNetworkLiveData() {
        //All Network Tasks
    }

    open fun subscribeToNavigationLiveData() {

    }

    open fun showProgressBar() {
        (activity as MainActivity).showProgressBar()
    }

    open fun hideProgressBar() {
        (activity as MainActivity).hideProgressBar()
    }

    open fun openSoftKeyboard(context: Context, view: View) {
        view.requestFocus()
        // open the soft keyboard
        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT)
    }


    /**
     * Used to scroll to the given view.
     *
     * @param scrollViewParent Parent ScrollView
     * @param view             View to which we need to scroll.
     */
     open fun scrollToView(scrollViewParent: NestedScrollView, view: View) {
        // Get deepChild Offset
        val childOffset = Point()
        getDeepChildOffset(scrollViewParent, view.parent, view, childOffset)
        // Scroll to child.
        scrollViewParent.smoothScrollTo(0, childOffset.y)
    }

    /**
     * Used to get deep child offset.
     *
     *
     * 1. We need to scroll to child in scrollview, but the child may not the direct child to scrollview.
     * 2. So to get correct child position to scroll, we need to iterate through all of its parent views till the main parent.
     *
     * @param mainParent        Main Top parent.
     * @param parent            Parent.
     * @param child             Child.
     * @param accumulatedOffset Accumulated Offset.
     */
     open fun getDeepChildOffset(
        mainParent: ViewGroup,
        parent: ViewParent,
        child: View,
        accumulatedOffset: Point
    ) {
        val parentGroup = parent as ViewGroup
        accumulatedOffset.x += child.left
        accumulatedOffset.y += child.top
        if (parentGroup == mainParent) {
            return
        }
        getDeepChildOffset(mainParent, parentGroup.parent, parentGroup, accumulatedOffset)
    }


}