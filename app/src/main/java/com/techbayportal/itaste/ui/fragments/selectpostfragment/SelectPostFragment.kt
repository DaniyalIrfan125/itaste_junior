package com.techbayportal.itaste.ui.fragments.selectpostfragment

import android.content.ContentUris
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.techbayportal.itaste.BR
import com.techbayportal.itaste.R
import com.techbayportal.itaste.baseclasses.BaseFragment
import com.techbayportal.itaste.databinding.LayoutSelectpostBinding
import com.techbayportal.itaste.ui.fragments.selectpostfragment.adapter.SelectPostsAdapter
import kotlinx.android.synthetic.main.layout_savedposts.recycler_posts
import kotlinx.android.synthetic.main.layout_selectpost.*


class SelectPostFragment : BaseFragment<LayoutSelectpostBinding, SelectPostViewModel>() {
    override val layoutId: Int
        get() = R.layout.layout_selectpost
    override val viewModel: Class<SelectPostViewModel>
        get() = SelectPostViewModel::class.java
    override val bindingVariable: Int
        get() = BR.viewModel


    var imagesList = ArrayList<Uri>()
    lateinit var adapter: SelectPostsAdapter


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        imagesList = getLocalImagePaths()

        adapter = SelectPostsAdapter(
            requireContext(),
            imagesList
        )

        tv_cornerText.text = getString(R.string.next)
        tv_topText.text =getText(R.string.new_post)

        recycler_posts.adapter = adapter
        recycler_posts.layoutManager =
            GridLayoutManager(context, 4, LinearLayoutManager.VERTICAL, false)
        recycler_posts.autoFitColumns(90)

        adapter.setOnClickListener(object : SelectPostsAdapter.OnClickListener {
            override fun onEntryClick(position: Int) {

                Glide.with(requireContext()).load(imagesList[position]).into(imageView)
            }

        })

    }

    private fun RecyclerView.autoFitColumns(columnWidth: Int) {
        val displayMetrics = this.context.resources.displayMetrics
        val noOfColumns =
            ((displayMetrics.widthPixels / displayMetrics.density) / columnWidth).toInt()
        this.layoutManager = GridLayoutManager(this.context, noOfColumns)
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