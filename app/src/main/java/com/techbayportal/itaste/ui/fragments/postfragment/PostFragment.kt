package com.techbayportal.itaste.ui.fragments.postfragment

import android.graphics.Bitmap
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.RadioButton
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.Observer
import androidx.navigation.Navigation
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar
import com.techbayportal.itaste.BR
import com.techbayportal.itaste.R
import com.techbayportal.itaste.baseclasses.BaseFragment
import com.techbayportal.itaste.data.models.EditPostResponse
import com.techbayportal.itaste.data.models.GetCategoriesResponse
import com.techbayportal.itaste.data.models.GetTimeSuggestionData
import com.techbayportal.itaste.data.remote.Resource
import com.techbayportal.itaste.databinding.LayoutPostfragmentBinding
import com.techbayportal.itaste.ui.fragments.postfragment.adapter.TimeDurationAdapter
import com.techbayportal.itaste.utils.DialogClass
import dagger.hilt.android.AndroidEntryPoint
import id.zelory.compressor.Compressor
import id.zelory.compressor.constraint.format
import id.zelory.compressor.constraint.quality
import id.zelory.compressor.constraint.size
import kotlinx.android.synthetic.main.fragment_post_detail.*
import kotlinx.android.synthetic.main.layout_postfragment.*
import kotlinx.android.synthetic.main.layout_postfragment.img_back
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import www.sanju.motiontoast.MotionToast
import java.io.File


@AndroidEntryPoint
class PostFragment : BaseFragment<LayoutPostfragmentBinding, PostViewModel>() {
    override val layoutId: Int
        get() = R.layout.layout_postfragment
    override val viewModel: Class<PostViewModel>
        get() = PostViewModel::class.java
    override val bindingVariable: Int
        get() = BR.viewModel

    lateinit var compressedImageFile: File
    var isAllowComments: Int = 0
    var listOfTime: ArrayList<GetTimeSuggestionData> = ArrayList()
    lateinit var timeDurationAdapter: TimeDurationAdapter
    var isEditPostClicked = false
    var postId = -1
    var categoryId = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //calling get categories api

        mViewModel.getTimeSuggestions()

        if (sharedViewModel.isEditPost) {
            isEditPostClicked = true
            mViewModel.editPostCall(sharedViewModel.postId)
            sharedViewModel.isEditPost = false
            sharedViewModel.isEditBottomSheetClicked.value = false

        }
        subscribeToNetworkLiveData()
    }


    private fun fieldTextWatcher() {
        ed_price.doOnTextChanged { text, start, before, count ->
            tv_errorPrice.visibility = View.GONE
        }
        ed_time.doOnTextChanged { text, start, before, count ->
            tv_errorTime.visibility = View.GONE

        }

        ed_caption.doOnTextChanged { text, start, before, count ->
            tv_errorCaption.visibility = View.GONE

        }

        ed_description.doOnTextChanged { text, start, before, count ->
            tv_errorDescription.visibility = View.GONE

        }
    }

    override fun subscribeToShareLiveData() {
        super.subscribeToShareLiveData()

        sharedViewModel.selectedPostImageFile.observe(this, Observer {
            it?.let {
                compressImageFile(it)
                Glide.with(requireContext()).load(it).into(imgView_post)
            }
        })

        sharedViewModel.categoriesResponse?.observe(this, Observer {
            createRadioButton(it)
        })
    }

    override fun subscribeToNetworkLiveData() {
        super.subscribeToNetworkLiveData()



        mViewModel.getTimeSuggestionResponse.observe(this, Observer {
            when (it.status) {
                Resource.Status.LOADING -> {
                    loadingDialog.show()
                }
                Resource.Status.SUCCESS -> {
                    it?.let { it ->
                        loadingDialog.dismiss()
                        it.data?.let {
                            listOfTime.addAll(it.data)
                            timeDurationAdapter.notifyDataSetChanged()
                        }

                    }
                }
                Resource.Status.ERROR -> {
                    loadingDialog.dismiss()
                    DialogClass.errorDialog(requireContext(), it.message!!, baseDarkMode)
                }
            }
        })

        mViewModel.addPostsResponse.observe(this, Observer {
            when (it.status) {
                Resource.Status.LOADING -> {
                    loadingDialog.show()
                }
                Resource.Status.SUCCESS -> {
                    it?.let { it ->
                        loadingDialog.dismiss()
                        it.data?.let {

                            DialogClass.successDialog(requireContext(), getString(R.string.tv_post_added), baseDarkMode)

                            Navigation.findNavController(radioGroup)
                                .popBackStack(R.id.homeFragment, false)
                        }

                    }
                }
                Resource.Status.ERROR -> {
                    loadingDialog.dismiss()
                    DialogClass.errorDialog(requireContext(), it.message!!, baseDarkMode)
                }
            }
        })

        mViewModel.updatePostsResponse.observe(this, Observer {
            when (it.status) {
                Resource.Status.LOADING -> {
                    loadingDialog.show()
                }
                Resource.Status.SUCCESS -> {
                    it?.let { it ->
                        loadingDialog.dismiss()
                        it.data?.let {
                            DialogClass.successDialog(requireContext(), getString(R.string.tv_post_updated), baseDarkMode)
                           /* MotionToast.createToast(
                                requireActivity(),
                                getString(R.string.tv_success),
                                getString(R.string.tv_post_added),
                                MotionToast.TOAST_SUCCESS,
                                MotionToast.GRAVITY_BOTTOM,
                                MotionToast.SHORT_DURATION,
                                ResourcesCompat.getFont(requireActivity(), R.font.roboto_regular)
                            )*/

                            sharedViewModel.isPostUpdated.value = true
                            Navigation.findNavController(radioGroup).popBackStack()
                        }

                    }
                }
                Resource.Status.ERROR -> {
                    loadingDialog.dismiss()
                    DialogClass.errorDialog(requireContext(), it.message!!, baseDarkMode)
                }
            }
        })

        mViewModel.editPostResponse.observe(this, Observer { it ->
            when (it.status) {
                Resource.Status.LOADING -> {
                    loadingDialog.show()
                }
                Resource.Status.SUCCESS -> {
                    it?.let { it ->
                        loadingDialog.dismiss()
                        it.data?.let {

                            ed_caption.setText(it.data.caption)
                            ed_price.setText(it.data.price)
                            ed_time.setText(it.data.cooking_time)
                            ed_description.setText(it.data.description)
                            postId = it.data.id
                            categoryId = it.data.category_id.toInt()
                            radioGroup.check(it.data.category_id.toInt())
                            Glide.with(requireContext()).load(it.data.image).into(imgView_post)

                        }

                    }
                }
                Resource.Status.ERROR -> {
                    loadingDialog.dismiss()
                    DialogClass.errorDialog(requireContext(), it.message!!, baseDarkMode)
                }
            }
        })

    }

    override fun subscribeToNavigationLiveData() {
        super.subscribeToNavigationLiveData()

        mViewModel.onPostBtnClicked.observe(this, Observer {
            fieldValidations()
        })

        mViewModel.onBackButtonClicked.observe(this, Observer {
            Navigation.findNavController(img_back).popBackStack()
        })

    }

    private fun compressImageFile(imageFile: File?) {
        imageFile?.let {
            GlobalScope.launch {
                compressedImageFile =
                    Compressor.compress(requireContext(), imageFile) {
                        quality(70)
                        format(Bitmap.CompressFormat.WEBP)
                        size(2_097_152) //2MB

                    }
            }
        }
    }

    private fun fieldValidations() {
        if (!TextUtils.isEmpty(ed_caption.text)) {
            if (!TextUtils.isEmpty(ed_description.text)) {
                if (!TextUtils.isEmpty(ed_price.text)) {
                    if (!TextUtils.isEmpty(ed_time.text)) {
                        if (radioGroup.checkedRadioButtonId == -1) {
                            Snackbar.make(
                                radioGroup,
                                getString(R.string.tv_please_select_category),
                                Snackbar.LENGTH_SHORT
                            ).show()
                        } else {
                            if (!isEditPostClicked) {
                                mViewModel.addPostCall(
                                    radioGroup.checkedRadioButtonId,
                                    compressedImageFile,
                                    ed_caption.text.toString(),
                                    ed_price.text.toString().toDouble(),
                                    ed_time.text.toString(),
                                    ed_description.text.toString(),
                                    isAllowComments
                                )
                            } else {
                                if (postId != -1)


                                    mViewModel.updatePostCall(
                                        postId,
                                        radioGroup.checkedRadioButtonId,
                                        ed_caption.text.toString(),
                                        ed_price.text.toString().toDouble(),
                                        ed_time.text.toString(),
                                        ed_description.text.toString(),
                                        isAllowComments
                                    )
                            }
                        }


                    } else {
                        tv_errorTime.visibility = View.VISIBLE
                        tv_errorTime.text = getString(R.string.tv_write_timing)
                    }

                } else {

                    tv_errorPrice.visibility = View.VISIBLE
                    tv_errorPrice.text = getString(R.string.tv_write_price)
                }

            } else {
                tv_errorDescription.visibility = View.VISIBLE
                tv_errorDescription.text = getString(R.string.tv_error_description)
            }
        } else {
            tv_errorCaption.visibility = View.VISIBLE
            tv_errorCaption.text = getString(R.string.tv_write_Caption)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        fieldTextWatcher()

        if (isEditPostClicked) {
            tv_cornerText.setText(R.string.tv_update)
        }

        timeDurationAdapter = TimeDurationAdapter(
            listOfTime,
            requireContext()
        )
        recycler_time.adapter = timeDurationAdapter


        switch_allowComments.setOnCheckedChangeListener { buttonView, isChecked ->
            isAllowComments = if (isChecked)
                1
            else
                0
        }

        timeDurationAdapter.setOnEntryClickListener(object :
            TimeDurationAdapter.TimeDurationItemClickListener {
            override fun onItemClick(position: Int) {
                ed_time.setText("")
                ed_time.setText(listOfTime[position].time)
            }

        })


    }

    private fun createRadioButton(data: List<GetCategoriesResponse.Data>) {
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
            radioGroup.addView(rb[i])
        }


    }
}