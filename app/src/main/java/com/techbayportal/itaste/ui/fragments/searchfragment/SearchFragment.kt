package com.techbayportal.itaste.ui.fragments.searchfragment

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.RadioButton
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.techbayportal.itaste.BR
import com.techbayportal.itaste.R
import com.techbayportal.itaste.baseclasses.BaseFragment
import com.techbayportal.itaste.data.models.GetAllCategoriesData
import com.techbayportal.itaste.data.models.GetAllCitiesData
import com.techbayportal.itaste.data.models.GetAllCountriesData
import com.techbayportal.itaste.data.models.SearchAndFilterResponseData
import com.techbayportal.itaste.data.remote.Resource
import com.techbayportal.itaste.databinding.LayoutSearchfragmentBinding
import com.techbayportal.itaste.ui.fragments.searchfragment.adapter.SearchRecyclerAdapter
import com.techbayportal.itaste.utils.DialogClass
import com.techbayportal.itaste.utils.SearchFilterSpinnerAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.layout_searchfragment.*
import kotlinx.android.synthetic.main.layout_searchfragment.spinner_city
import kotlinx.android.synthetic.main.layout_searchfragment.spinner_country
import timber.log.Timber


@AndroidEntryPoint
class SearchFragment : BaseFragment<LayoutSearchfragmentBinding, SearchViewModel>() {
    override val layoutId: Int
        get() = R.layout.layout_searchfragment
    override val viewModel: Class<SearchViewModel>
        get() = SearchViewModel::class.java
    override val bindingVariable: Int
        get() = BR.viewModel

    var isLayoutOpened = false
    private val categoryList = ArrayList<GetAllCategoriesData>()
    private val allPostsList = ArrayList<SearchAndFilterResponseData>()
    var twoArrayList: ArrayList<SearchAndFilterResponseData> = ArrayList()
    var threeArrayList: ArrayList<SearchAndFilterResponseData> = ArrayList()
    var mainArrayList: ArrayList<ArrayList<SearchAndFilterResponseData>> = ArrayList()


    private val countriesList = ArrayList<GetAllCountriesData>()
    private val citiesList = ArrayList<GetAllCitiesData>()
    private val searchResultList = ArrayList<SearchAndFilterResponseData>()

    lateinit var searchRecyclerAdapter: SearchRecyclerAdapter

    var countryid: Int = 0
    var cityid: Int = 0
    var categoryId: String = ""

    var searchApiStatus = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        subscribeToNetworkLiveData()

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mViewModel.hitGetAllSearchPostsDataApi()
        initializing()
        swipeRevealLayout.setLockDrag(true)
        img_filter.setOnClickListener {


            if (!isLayoutOpened) {
                swipeRevealLayout.open(true)
                if(categoryList.isNullOrEmpty()){
                    mViewModel.hitGetAllCategoriesApi()
                }

                /*Check for not rastrain filter values when filter is applied and drawer is opened again*/
                if(countriesList.isNullOrEmpty()){
                    if(citiesList.isNullOrEmpty()){
                        mViewModel.getAllCountries()
                        citiesList.add(0, GetAllCitiesData(0, "Select City", true))
                        mViewDataBinding.spinnerCity.adapter = SearchFilterSpinnerAdapter(citiesList)
                    }

                }


                isLayoutOpened = true
            } else {
                swipeRevealLayout.close(true)
                isLayoutOpened = false
            }
        }

        searchRecyclerAdapter = SearchRecyclerAdapter(
            mainArrayList,

            requireContext()
        )

        recycler_searchposts.adapter = searchRecyclerAdapter
        recycler_searchposts.layoutManager = LinearLayoutManager(context)


    }

    private fun splitArray(splitArrayList: ArrayList<SearchAndFilterResponseData>) {
        var variableTwo = 0
        var variableThree = 0
        var isTwoRow = false

        mainArrayList.clear()

        for (i in 0 until splitArrayList.size) {
            if (!isTwoRow) {
                variableTwo += 1
                if (variableTwo == 1) {
                    twoArrayList = ArrayList()
                    twoArrayList.add(splitArrayList[i])
                } else if (variableTwo == 2) {
                    variableTwo = 0
                    isTwoRow = true
                    twoArrayList.add(splitArrayList[i])
                    mainArrayList.add(twoArrayList)
                }
            } else {
                variableThree += 1
                if (variableThree == 1) {
                    threeArrayList = ArrayList()
                    threeArrayList.add(splitArrayList[i])
                } else if (variableThree == 2) {
                    threeArrayList.add(splitArrayList[i])
                } else if (variableThree == 3) {
                    variableThree = 0
                    isTwoRow = false
                    threeArrayList.add(splitArrayList[i])
                    mainArrayList.add(threeArrayList)
                }
            }
        }
        Timber.d("val: $mainArrayList")

    }

    private fun initializing() {
        ed_search.doOnTextChanged { text, start, before, count ->
            if (text!!.length > 2 && searchApiStatus) {
                searchApiStatus = false
                mViewModel.hitSearchApi(text.toString())

            } else if (text.length == 0) {
                loading(false)
                searchResultList.clear()
                mViewModel.hitSearchApi("")
            }
        }
    }

    override fun subscribeToNetworkLiveData() {
        super.subscribeToNetworkLiveData()

        mViewModel.getAllSearchPostsResponse.observe(this, Observer {
            when (it.status) {
                Resource.Status.LOADING -> {
                    loadingDialog.show()
                }
                Resource.Status.SUCCESS -> {
                    loadingDialog.dismiss()

                    splitArray(it.data!!.data)
                    searchRecyclerAdapter.notifyDataSetChanged()
                    Timber.d("Search: All Posts")
                }
                Resource.Status.ERROR -> {
                    loadingDialog.dismiss()
                    DialogClass.errorDialog(requireContext(), it.message!!, baseDarkMode)
                }

            }
        })

        mViewModel.getCountriesResponse.observe(this, Observer {
            when (it.status) {
                Resource.Status.LOADING -> {
                    loadingDialog.show()
                }
                Resource.Status.SUCCESS -> {
                    loadingDialog.dismiss()
                    countriesList.clear()
                    countriesList.add(0, GetAllCountriesData(0, "Select Country", "", true))
                    countriesList.addAll(it.data!!.data)
                    mViewDataBinding.spinnerCountry.adapter = SearchFilterSpinnerAdapter(countriesList)
                }
                Resource.Status.ERROR -> {
                    loadingDialog.dismiss()
                    DialogClass.errorDialog(requireContext(), it.message!!, baseDarkMode)
                }

            }
        })

        mViewModel._getCitiesResponse.observe(this, Observer {

            when (it.status) {
                Resource.Status.LOADING -> {
                    loadingDialog.show()
                }
                Resource.Status.SUCCESS -> {
                    loadingDialog.dismiss()
                    citiesList.clear()
                    citiesList.add(0, GetAllCitiesData(0, "Select City", true))
                    citiesList.addAll(it.data!!.data)
                    mViewDataBinding.spinnerCity.adapter = SearchFilterSpinnerAdapter(citiesList)
                }
                Resource.Status.ERROR -> {
                    loadingDialog.dismiss()
                    DialogClass.errorDialog(requireContext(), it.message!!, baseDarkMode)
                }

            }

        })

        mViewModel.getAllCategoriesResponse.observe(this, Observer {
            when (it.status) {
                Resource.Status.LOADING -> {
                    loadingDialog.show()
                }
                Resource.Status.SUCCESS -> {
                    loadingDialog.dismiss()
                    categoryList.clear()
                    rg_categories.removeAllViews()
                    categoryList.addAll(it.data!!.data)
                    createRadioButton(categoryList)
                }
                Resource.Status.ERROR -> {
                    loadingDialog.dismiss()
                    DialogClass.errorDialog(requireContext(), it.message!!, baseDarkMode)
                }

            }
        })

        mViewModel.getSearchAndFilterResponse.observe(this, Observer {
            when (it.status) {
                Resource.Status.LOADING -> {
                    loadingDialog.show()
                }
                Resource.Status.SUCCESS -> {
                    loadingDialog.dismiss()
                    allPostsList.clear()
                    //Close the side filter screen

                    allPostsList.addAll(it.data!!.data)
                    splitArray(it.data.data)
                    searchRecyclerAdapter.notifyDataSetChanged()
                    Timber.d("Search: All data after filter")
                }
                Resource.Status.ERROR -> {
                    loadingDialog.dismiss()
                    DialogClass.errorDialog(requireContext(), it.message!!, baseDarkMode)
                }

            }
        })


        //Search only with keyword
        mViewModel.getSearchResponse.observe(this, Observer {
            when (it.status) {
                Resource.Status.LOADING -> {
                    loading(true)
                }
                Resource.Status.SUCCESS -> {
                    searchApiStatus = true
                    loading(false)
                    allPostsList.addAll(it.data!!.data)
                    splitArray(it.data.data)
                    searchRecyclerAdapter.notifyDataSetChanged()
                    Timber.d("Search: search applied")

                }
                Resource.Status.ERROR -> {
                    searchApiStatus = true
                    loading(false)
                    DialogClass.errorDialog(requireContext(), it.message!!, baseDarkMode)
                }

            }
        })

    }

    override fun subscribeToNavigationLiveData() {
        super.subscribeToNavigationLiveData()

        spinner_city.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                citiesList[position].id
                cityid = citiesList[position].id
            }
        }

        spinner_country.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                if (position > 0) {
                    countriesList[position].id
                    mViewModel.getAllCities(countriesList[position].id)
                    countryid = countriesList[position].id
                }
            }
        }
        groupradio.setOnCheckedChangeListener { group, checkedId ->
            categoryId = checkedId.toString()
        }

        mViewModel.onResetButtonClicked.observe(this, Observer {
            mViewModel.hitGetAllSearchPostsDataApi()
            rg_categories.removeAllViews()
            createRadioButton(categoryList)
            spinner_city.setSelection(0)
            spinner_country.setSelection(0)

            //if side Filter is open then close it
            if (!isLayoutOpened) {
                swipeRevealLayout.open(true)
                isLayoutOpened = true
            } else {
                swipeRevealLayout.close(true)
                isLayoutOpened = false
            }

        })

        mViewModel.onApplyFilterButtonClicked.observe(this, Observer {
            if (spinner_city.selectedItemPosition == 0 || spinner_country.selectedItemPosition == 0) {
                mViewModel.hitApplyFiltersApi(ed_search.text.toString(), "", "", categoryId)
            } else {
                mViewModel.hitApplyFiltersApi(
                    ed_search.text.toString(),
                    countryid.toString(),
                    cityid.toString(),
                    categoryId
                )
            }
            //if side Filter is open then close it
            if (!isLayoutOpened) {
                swipeRevealLayout.open(true)
                isLayoutOpened = true
            } else {
                swipeRevealLayout.close(true)
                isLayoutOpened = false
            }
        })
    }

    private fun createRadioButton(data: List<GetAllCategoriesData>) {
        val rb = arrayOfNulls<RadioButton>(data.size)
        for (i in data.indices) {
            rb[i] = RadioButton(requireContext())
            rb[i]!!.text = data[i].name
            rb[i]!!.setTextColor(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.titleTextColorBlack
                )
            )
            rb[i]!!.textSize = 14f
            val font = ResourcesCompat.getFont(requireContext(), R.font.roboto_regular)
            rb[i]!!.typeface = font
            rb[i]!!.id = data[i].id
            rg_categories.addView(rb[i])
        }

    }

    private fun loading(isLoading: Boolean) {
        if (isLoading) {
            searchIcon.visibility = View.GONE
            spinKit.visibility = View.VISIBLE
        } else {
            searchIcon.visibility = View.VISIBLE
            spinKit.visibility = View.GONE
        }
    }


}