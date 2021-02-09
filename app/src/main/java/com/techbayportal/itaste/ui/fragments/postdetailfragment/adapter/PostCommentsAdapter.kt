package com.techbayportal.itaste.ui.fragments.postdetailfragment.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.like.LikeButton
import com.like.OnLikeListener
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import com.techbayportal.itaste.R
import com.techbayportal.itaste.data.models.Comment
import com.techbayportal.itaste.databinding.ItemProfileCommentBinding
import com.techbayportal.itaste.generated.callback.OnClickListener
import com.techbayportal.itaste.ui.fragments.postdetailfragment.itemclicklistener.PostCommentsRvClickListener
import kotlinx.android.synthetic.main.fragment_post_detail.*
import kotlinx.android.synthetic.main.item_profile_comment.view.*
import java.lang.Exception

class PostCommentsAdapter(
    val onClickListener: PostCommentsRvClickListener,
    val commentsList: ArrayList<Comment>,
    var showAllItems: Boolean
) : RecyclerView.Adapter<PostCommentsAdapter.ViewHolderClass>() {

    fun updateShowAllItems(isShow: Boolean) {
        showAllItems = isShow
    }

    inner class ViewHolderClass(val itemBinding: ItemProfileCommentBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {

        fun bindData(position: Int) {

            itemBinding.tvUserName.text =
                commentsList[position].first + " " + commentsList[position].last
            itemBinding.tvComment.text = commentsList[position].comment
            itemBinding.mLikeButton.isLiked = commentsList[position].total_likes != 0

            Picasso.get().load(commentsList[position].profilePic).fit().centerCrop()
                .into(itemBinding.root.image_profile, object :
                    Callback {
                    override fun onSuccess() {
                        itemBinding.root.spinKit_profile.visibility = View.GONE
                    }

                    override fun onError(e: Exception?) {
                        itemBinding.root.spinKit_profile.visibility = View.GONE
                    }
                })


            itemBinding.mLikeButton.setOnLikeListener(object : OnLikeListener {
                override fun liked(likeButton: LikeButton?) {
                    onClickListener.favouriteUnFavorite(commentsList[position].id)
                }

                override fun unLiked(likeButton: LikeButton?) {
                    onClickListener.favouriteUnFavorite(commentsList[position].id)
                }
            })



        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderClass {
        return ViewHolderClass(
            DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.item_profile_comment,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolderClass, position: Int) {
        holder.bindData(position)
    }

    override fun getItemCount(): Int {
        return if (commentsList.size < 6) {
            commentsList.size
        } else {
            var count = 0
            if (showAllItems) {
                count = commentsList.size
            } else {
                for (i in 0 until commentsList.size) {
                    count = i
                }

                if(count > 6){
                    count = 6
                }
            }
            return count

        }
    }
}