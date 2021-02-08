package com.techbayportal.itaste.ui.fragments.blockedaccountsfragment.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import com.techbayportal.itaste.R
import com.techbayportal.itaste.constants.AppConstants
import com.techbayportal.itaste.data.models.GetAllBlockedUserData
import com.techbayportal.itaste.databinding.ItemBlockedAccountsBinding
import com.techbayportal.itaste.ui.fragments.blockedaccountsfragment.itemclicklistener.BlockedAccountsRvClickListener
import com.techbayportal.itaste.ui.fragments.homefragment.itemclicklistener.HomeRvClickListener
import kotlinx.android.synthetic.main.layout_profilefragment.*
import java.lang.Exception


class BlockedAccountsAdapter(
    private val list: ArrayList<GetAllBlockedUserData>,
    private val listener: ClickItemListener

) : RecyclerView.Adapter<BlockedAccountsAdapter.ViewHolder>() {

//    private var onClickListener: BlockedAccountsRvClickListener? = null

    /*fun setOnEntryClickListener(onEntryClickListener: BlockedAccountsRvClickListener?) {
        blockedAccountsRvClickListener = onEntryClickListener
    }*/

    interface ClickItemListener {
        fun onClicked(getAllBlockedUserData: GetAllBlockedUserData)
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
       // val inflater = LayoutInflater.from(parent.context)
      //  return ViewHolder(inflater, parent)
        return ViewHolder(ItemBlockedAccountsBinding.inflate(LayoutInflater.from(parent.context)))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val model = list[position]
       // holder.bind(position)
        holder.bind.tvBlockedAccountUserName.text = model.username
        holder.bind.tvBlockedAccountName.text = model.first_name +" "+ model.last_name
        if(model.profilePic.isNotEmpty()){
            Picasso.get().load(model.profilePic).fit().centerCrop()
                .into(holder.bind.sivBlockedAccountPic, object : Callback{
                    override fun onSuccess() {
                        holder.bind.spinKit.visibility = View.GONE
                    }

                    override fun onError(e: Exception?) {
                        Picasso.get().load(R.drawable.placeholder_image).into(holder.bind.sivBlockedAccountPic)
                        holder.bind.spinKit.visibility = View.GONE
                    }

                })
        }
        else{
            holder.bind.sivBlockedAccountPic.setImageDrawable(ContextCompat.getDrawable(holder.bind.sivBlockedAccountPic.context,R.drawable.placeholder_image))
        }
        /*holder.bind.btnUnblock.setOnClickListener {
            onClickListener?.onItemClickListener(AppConstants.RecyclerViewKeys.BLOCKED_ACCOUNT_RV_UNBLOCK_BUTTON)
        }*/

        holder.bind.btnUnblock.setOnClickListener {
            listener.onClicked(model)
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    inner class ViewHolder(private var binding: ItemBlockedAccountsBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val bind = binding
    }

    /*inner class ViewHolder(inflater: LayoutInflater, parent: ViewGroup) :
        RecyclerView.ViewHolder(
            inflater.inflate(
                R.layout.item_blocked_accounts,
                parent,
                false
            )
        ) {

        private var btnUnblock: Button? = null

        init {
            btnUnblock = itemView.findViewById(R.id.btn_unblock)
        }

        fun bind(int: Int) {
            btnUnblock?.setOnClickListener {
                onClickListener?.onItemClickListener(AppConstants.RecyclerViewKeys.BLOCKED_ACCOUNT_RV_UNBLOCK_BUTTON)
            }
        }
    }*/
}