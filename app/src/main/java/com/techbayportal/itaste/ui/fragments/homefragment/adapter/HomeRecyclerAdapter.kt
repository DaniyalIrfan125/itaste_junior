package com.techbayportal.itaste.ui.fragments.homefragment.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.github.ybq.android.spinkit.SpinKitView
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import com.techbayportal.itaste.R
import com.techbayportal.itaste.SharedViewModel
import com.techbayportal.itaste.constants.AppConstants
import com.techbayportal.itaste.data.models.GetHomeScreenData
import com.techbayportal.itaste.data.models.GetHomeScreenPostsData
import com.techbayportal.itaste.data.models.PostDetailData
import com.techbayportal.itaste.databinding.ItemHomeRecyclerImageBinding
import com.techbayportal.itaste.ui.fragments.homefragment.itemclicklistener.HomeChildRvClickListener
import com.techbayportal.itaste.ui.fragments.homefragment.itemclicklistener.HomeRvClickListener
import com.techbayportal.itaste.ui.fragments.profilefragment.adapter.PostsRecyclerAdapter
import java.lang.Exception

class HomeRecyclerAdapter(
    private val list: ArrayList<GetHomeScreenData>,
    val context: Context
) :
    RecyclerView.Adapter<HomeRecyclerAdapter.ViewHolder>() {
    private var onClickListener: HomeRvClickListener? = null
    lateinit var homeChildRecyclerAdapter: HomeChildRecyclerAdapter

    fun setOnEntryClickListener(onEntryClickListener: HomeRvClickListener?) {
        onClickListener = onEntryClickListener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)

        return ViewHolder(inflater, parent)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(position)
    }

    override fun getItemCount(): Int = list.size


    inner class ViewHolder(inflater: LayoutInflater, parent: ViewGroup) :
        RecyclerView.ViewHolder(
            inflater.inflate(
                R.layout.item_home_recyclerview,
                parent,
                false
            )
        ) {

        private var img_profile: ImageView? = null
        private var tv_profileName: TextView? = null
        private var tv_address: TextView? = null
        private var recyclerView: RecyclerView? = null
        private var img_dots: ImageView? = null
        private var spinKit: SpinKitView? = null

        init {
            img_profile = itemView.findViewById(R.id.img_profile)
            tv_profileName = itemView.findViewById(R.id.tv_profileName)
            tv_address = itemView.findViewById(R.id.tv_address)
            recyclerView = itemView.findViewById(R.id.recycler_images)
            img_dots = itemView.findViewById(R.id.img_dots)
            spinKit = itemView.findViewById(R.id.spinKit)
        }

        fun bind(int: Int) {
            val model = list[int]


            Picasso.get()
                .load(model.profilePic).fit().centerCrop()
                .into(img_profile , object : Callback {
                    override fun onSuccess() {
                        spinKit!!.visibility = View.GONE
                    }

                    override fun onError(e: Exception?) {
                        Picasso.get().load(R.drawable.placeholder_image).into(img_profile)
                        spinKit!!.visibility = View.GONE

                    }

                })


            tv_profileName!!.text = model.first_name + model.last_name
            tv_address!!.text = model.location

            img_profile?.setOnClickListener {
                onClickListener?.onItemClickListener(AppConstants.RecyclerViewKeys.HOME_RV,model.id)


            }

            img_dots?.setOnClickListener {
                onClickListener?.onItemClickListener(AppConstants.RecyclerViewKeys.HOME_RV_IMG_DOTS,-1)
            }


           homeChildRecyclerAdapter = HomeChildRecyclerAdapter(model.post)
            recyclerView!!.adapter = homeChildRecyclerAdapter

            homeChildRecyclerAdapter.setOnChildClickListener(object : HomeChildRvClickListener {
                override fun onChildItemClick(position: Int) {
                    onClickListener?.onChildItemClick(position)
                }
            })

            recyclerView!!.layoutManager =
                LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)

            itemView

        }

    }

}