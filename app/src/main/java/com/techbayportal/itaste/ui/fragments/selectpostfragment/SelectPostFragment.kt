package com.techbayportal.itaste.ui.fragments.selectpostfragment

import android.content.ContentUris
import android.content.Intent
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import androidx.lifecycle.Observer
import androidx.navigation.Navigation
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.google.android.material.snackbar.Snackbar
import com.opensooq.supernova.gligar.GligarPicker
import com.squareup.picasso.Picasso
import com.techbayportal.itaste.BR
import com.techbayportal.itaste.R
import com.techbayportal.itaste.baseclasses.BaseFragment
import com.techbayportal.itaste.constants.AppConstants
import com.techbayportal.itaste.data.remote.Resource
import com.techbayportal.itaste.databinding.LayoutSelectpostBinding
import com.techbayportal.itaste.ui.fragments.selectpostfragment.adapter.SelectPostsAdapter
import com.techbayportal.itaste.utils.DialogClass
import com.techbayportal.itaste.utils.FileUtils
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.layout_savedposts.recycler_posts
import kotlinx.android.synthetic.main.layout_selectpost.*
import kotlinx.android.synthetic.main.layout_signupfragment.*
import java.io.File


@AndroidEntryPoint
class SelectPostFragment : BaseFragment<LayoutSelectpostBinding, SelectPostViewModel>() {
    override val layoutId: Int
        get() = R.layout.layout_selectpost
    override val viewModel: Class<SelectPostViewModel>
        get() = SelectPostViewModel::class.java
    override val bindingVariable: Int
        get() = BR.viewModel


    var imagesList = ArrayList<Uri>()
    lateinit var adapter: SelectPostsAdapter
    var selectedImageFile: File? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        subscribeToNetworkLiveData()

        mViewModel.getCategories()
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initlialising()
        clickListener()


    }

    private fun clickListener() {

        selectedImageFile?.let {
            Glide.with(requireContext()).load(it).into(imageView)
        }

        adapter.setOnClickListener(object : SelectPostsAdapter.OnClickListener {
            override fun onEntryClick(position: Int) {
                selectedImageFile = FileUtils.getFile(requireContext(), imagesList[position])
                Glide.with(requireContext()).load(imagesList[position]).into(imageView)
            }

        })
    }

    private fun initlialising() {
        //getting images from the gallery
        imagesList = getLocalImagePaths()
        adapter = SelectPostsAdapter(
            requireContext(),
            imagesList
        )

        tv_cornerText.text = getString(R.string.next)
        tv_topText.text = getText(R.string.new_post)


        recycler_posts.adapter = adapter
        recycler_posts.layoutManager =
            GridLayoutManager(context, 4, LinearLayoutManager.VERTICAL, false)
        recycler_posts.autoFitColumns(90)
    }

    override fun subscribeToNavigationLiveData() {
        super.subscribeToNavigationLiveData()

        mViewModel.onNextBtnClicked.observe(this, Observer {
            if (selectedImageFile != null) {
                sharedViewModel.selectedPostImageFile.value = selectedImageFile
                Navigation.findNavController(imageView)
                    .navigate(R.id.action_selectPostFragment_addposts_fragment)
            } else {
                Snackbar.make(
                    imageView,
                    getString(R.string.select_picture),
                    Snackbar.LENGTH_SHORT
                ).show()
            }

        })

        mViewModel.onCameraBtnClicked.observe(this, Observer {
            GligarPicker().requestCode(AppConstants.SELECT_POST_PICTURE)
                .withFragment(this)
                .limit(1)
                .show()
        })
    }

    private fun RecyclerView.autoFitColumns(columnWidth: Int) {
        val displayMetrics = this.context.resources.displayMetrics
        val noOfColumns =
            ((displayMetrics.widthPixels / displayMetrics.density) / columnWidth).toInt()
        this.layoutManager = GridLayoutManager(this.context, noOfColumns)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (data != null) {
            if (requestCode == AppConstants.SELECT_POST_PICTURE) {
                val imagePath = data.extras?.getStringArray(GligarPicker.IMAGES_RESULT)!![0]
                selectedImageFile = File(imagePath)
                if (selectedImageFile != null) {
                    //calling method to compress image
                    sharedViewModel.selectedPostImageFile.value = selectedImageFile
                    Navigation.findNavController(imageView)
                        .navigate(R.id.action_selectPostFragment_addposts_fragment)
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    override fun subscribeToNetworkLiveData() {
        super.subscribeToNetworkLiveData()

        mViewModel.getCategoriesResponse.observe(this, Observer {
            when (it.status) {
                Resource.Status.LOADING -> {
                    loadingDialog.show()
                }
                Resource.Status.SUCCESS -> {
                    it?.let { it ->
                        loadingDialog.dismiss()
                        it.data?.let {
                            sharedViewModel.categoriesResponse.value = it.data
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

    fun getLocalImagePaths(): ArrayList<Uri> {
        val result = ArrayList<Uri>()
        val uri: Uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        val orderBy = MediaStore.Images.Media.DATE_ADDED + " DESC";
        val projection = arrayOf(MediaStore.Images.Media._ID)

        activity?.contentResolver?.query(uri, projection, null, null, orderBy)?.use {
            while (it.moveToNext()) {
                result.add(
                    ContentUris.withAppendedId(
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                        it.getLong(0)
                    )

                )

            }

        }
        return result
    }

}