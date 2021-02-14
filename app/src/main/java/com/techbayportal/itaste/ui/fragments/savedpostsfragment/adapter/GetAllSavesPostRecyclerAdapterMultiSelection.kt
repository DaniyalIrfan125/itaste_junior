package com.techbayportal.itaste.ui.fragments.savedpostsfragment.adapter

import android.content.Context
import android.util.SparseBooleanArray
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnLongClickListener
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import com.techbayportal.itaste.R
import com.techbayportal.itaste.data.models.GetAllSavedData
import com.techbayportal.itaste.databinding.ItemLayoutPostsBinding
import java.util.*
import kotlin.collections.ArrayList

class GetAllSavesPostRecyclerAdapterMultiSelection(
    private val mContext: Context,
    list: ArrayList<GetAllSavedData>
) :
    RecyclerView.Adapter<GetAllSavesPostRecyclerAdapterMultiSelection.ViewHolder>() {
    private val list: ArrayList<GetAllSavedData>
    private val selectedItems: SparseBooleanArray
    private var selectedIndex = -1
    private var itemClick: OnItemClick? = null
    fun setItemClick(itemClick: OnItemClick?) {
        this.itemClick = itemClick
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val bi: ItemLayoutPostsBinding = DataBindingUtil.inflate(
            LayoutInflater.from(mContext),
            R.layout.item_layout_posts,
            parent,
            false
        )
        return ViewHolder(bi)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        if (list[position].image.isNotEmpty()) {
            Picasso.get().load(list[position].image).fit().centerCrop()
                .into(holder.bi.imgProduct, object : Callback {
                    override fun onSuccess() {
                        holder.bi.skPostImage.visibility = View.GONE
                    }

                    override fun onError(e: Exception?) {
                        Picasso.get().load(R.drawable.placeholder_image)
                            .into(holder.bi.imgProduct)
                        holder.bi.skPostImage.visibility = View.GONE
                    }

                })

        }


        //Changes the activated state of this view.
        holder.bi.relativePost.isActivated = selectedItems[position, false]
        holder.bi.relativePost.setOnClickListener(View.OnClickListener { view ->
            if (itemClick == null) return@OnClickListener
            itemClick!!.onItemClick(view, list[position], position)
        })
        holder.bi.relativePost.setOnLongClickListener(OnLongClickListener { view ->
            if (itemClick == null) {
                false
            } else {
                itemClick!!.onLongPress(view, list[position], position)
                true
            }
        })
        toggleIcon(holder.bi, position)

    }

    /*
       This method will trigger when we we long press the item and it will change the icon of the item to check icon.
     */
    private fun toggleIcon(bi: ItemLayoutPostsBinding, position: Int) {
        if (selectedItems[position, false]) {
            bi.imgTick.visibility = View.VISIBLE
            if (selectedIndex == position) selectedIndex = -1
        } else {
            bi.imgTick.visibility = View.GONE
            if (selectedIndex == position) selectedIndex = -1
        }
    }

    /*
       This method helps you to get all selected items from the list
     */
    fun getSelectedItems(): List<Int> {
        val items: MutableList<Int> = ArrayList(selectedItems.size())
        for (i in 0 until selectedItems.size()) {
            items.add(selectedItems.keyAt(i))
        }
        return items
    }

    /*
       this will be used when we want to delete items from our list
     */
    fun removeItems(position: Int) {
        list.removeAt(position)
        selectedIndex = -1
    }

    /*
       for clearing our selection
     */
    fun clearSelection() {
        selectedItems.clear()
        notifyDataSetChanged()
    }

    /*
             this function will toggle the selection of items
     */
    fun toggleSelection(position: Int) {
        selectedIndex = position
        if (selectedItems[position, false]) {
            selectedItems.delete(position)
        } else {
            selectedItems.put(position, true)
        }
        notifyItemChanged(position)
    }

    /*
      How many items have been selected? this method exactly the same . this will return a total number of selected items.
     */
    fun selectedItemCount(): Int {
        return selectedItems.size()
    }

    override fun getItemCount(): Int {
        return list.size
    }

    class ViewHolder(itemView: ItemLayoutPostsBinding) :
        RecyclerView.ViewHolder(itemView.root) {
        var bi: ItemLayoutPostsBinding = itemView

    }

    interface OnItemClick {
        fun onItemClick(view: View?, inbox: GetAllSavedData?, position: Int)
        fun onLongPress(view: View?, inbox: GetAllSavedData?, position: Int)
    }

    init {
        this.list = list
        selectedItems = SparseBooleanArray()
    }
}
