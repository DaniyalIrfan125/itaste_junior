package com.techbayportal.itaste.ui.fragments.directmessagesfragment

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.navigation.Navigation
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.techbayportal.itaste.BR
import com.techbayportal.itaste.R
import com.techbayportal.itaste.baseclasses.BaseFragment
import com.techbayportal.itaste.data.models.CartVendor
import com.techbayportal.itaste.data.models.chat.UserOnlineStatusModel
import com.techbayportal.itaste.databinding.FragmentDirectMessagesBinding
import com.techbayportal.itaste.ui.fragments.directmessagesfragment.adapter.DirectMessagesAdapter
import com.techbayportal.itaste.ui.fragments.directmessagesfragment.itemClickListener.DirectMessagesRvItemClickListener
import com.techbayportal.offsidesportsapp.data.models.chat.ChatMessageDataClass
import com.techbayportal.offsidesportsapp.data.models.chat.GeneralInboxDataClass
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_chat.*
import kotlinx.android.synthetic.main.fragment_direct_messages.*
import kotlinx.android.synthetic.main.fragment_direct_messages.img_back
import timber.log.Timber

@AndroidEntryPoint
class DirectMessagesFragment :
    BaseFragment<FragmentDirectMessagesBinding, DirectMessagesFragmentViewModel>() {

    override val layoutId: Int
        get() = R.layout.fragment_direct_messages
    override val viewModel: Class<DirectMessagesFragmentViewModel>
        get() = DirectMessagesFragmentViewModel::class.java
    override val bindingVariable: Int
        get() = BR.viewModel


    val inboxArray = arrayListOf<GeneralInboxDataClass>()
    private var currentUserId = ""
    private lateinit var adapter: DirectMessagesAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        currentUserId = sharedViewModel.userModel.id.toString()

        adapter = DirectMessagesAdapter(inboxArray, object : DirectMessagesAdapter.ClickListener {
            override fun onClick(data: GeneralInboxDataClass) {
               // Toast.makeText(context, " Inbox ItemClicked : ${data.id}", Toast.LENGTH_SHORT).show()
                Timber.d("Inbox ItemClicked : ${data.senderId}")
                sharedViewModel.vendorDetailsForCart = CartVendor("", data.senderName,data.senderId.toInt(), "",data.imgStr)
                sharedViewModel.cartPost = null
                Navigation.findNavController(ll_new_message).navigate(R.id.action_directMessagesFragment_to_chatFragment2)
            }

        })
        rvUsersForChat.adapter = adapter
        getInboxList()

    }

    override fun subscribeToNavigationLiveData() {
        super.subscribeToNavigationLiveData()

        mViewModel.onNewMessageIconClicked.observe(this, Observer {
            Navigation.findNavController(ll_new_message)
                .navigate(R.id.action_directMessagesFragment_to_newMessageFragment)
        })

        mViewModel.onBackButtonClicked.observe(this, Observer {
            Navigation.findNavController(img_back).popBackStack()
        })
    }

    private fun getInboxList() {


        val fireStoreObj = FirebaseFirestore.getInstance()
        inboxArray.clear()
        fireStoreObj.collection("Chats")
            .whereArrayContains("usersId", currentUserId)
            .orderBy("lastMsgTime", Query.Direction.ASCENDING)
            .whereEqualTo("groupChat", false).get()
            .addOnSuccessListener {
                it.documents.forEach { doc ->
                    val inboxData = doc.toObject(GeneralInboxDataClass::class.java)!!
                    val chatUser = inboxData.users.find { user -> user.id != currentUserId }
                    fireStoreObj.collection("UserStatus")
                        .document("219")
                        .get()
                        .addOnSuccessListener {statusDoc ->
                            val onlineStatusData = statusDoc.toObject(UserOnlineStatusModel::class.java)!!
                            inboxData.imgStr = onlineStatusData.imgStr
                            if (onlineStatusData.state == "online") {
                                inboxData.userOnlineStatus = true
                            } else if (onlineStatusData.state == "offline") {
                                inboxData.userOnlineStatus = false
                            }

                        }
                        .addOnFailureListener{exception ->
                            exception
                        }
                    fireStoreObj.collection("Chats")
                        .document(inboxData.id)
                        .collection("Threads")
                        .orderBy("createdAt", Query.Direction.DESCENDING)
                        .get()
                        .addOnSuccessListener { messageArray ->
                            val filteredMessagesArray = arrayListOf<ChatMessageDataClass>()
                            if (messageArray.documents.isNotEmpty()) {
                                messageArray.documents.forEach { messageDoc ->
                                    val messageData =
                                        messageDoc.toObject(ChatMessageDataClass::class.java)
                                    if (!messageData!!.deletedFor.contains(currentUserId))
                                        filteredMessagesArray.add(messageData)
                                }

                                val lastMessageData = filteredMessagesArray.first()

                                inboxData.senderId = chatUser?.id ?: "0"
                                inboxData.senderName = chatUser?.name ?: ""
                                inboxData.imgStr = chatUser?.imgStr ?: ""
                                inboxData.lastMsg = lastMessageData.message
                                inboxData.lastMsgTime = lastMessageData.createdAt

                                var unreadCount = 0
                                filteredMessagesArray.forEach { messgeDoc ->
                                    if (messgeDoc.seenBy.isEmpty() && messgeDoc.senderId != currentUserId) {
                                        unreadCount++
                                    }
                                }
                                inboxData.unreadMessages = unreadCount
                                inboxArray.add(0, inboxData)
                                adapter.notifyDataSetChanged()
                            } else {
                            }
                            //showPlaceholder()
                        }
                        .addOnFailureListener { messageArrayExcpetion ->
                            // showPlaceholder()

                            messageArrayExcpetion
                            Timber.d("$messageArrayExcpetion")
                        }
                }
            }.addOnFailureListener { inboxException ->
                // showPlaceholder()
                inboxException
            }
    }

    private fun  getOnlineStatus(){

    }


    /* override fun onUserSelected(selectedUser: GeneralInboxDataClass) {
         *//*startActivity(
            Intent(this, ChatActivity::class.java)
                .putExtra("name", selectedUser.senderName)
                .putExtra("id", selectedUser.senderId)
                .putExtra("chatKey", selectedUser.id)
        )*//*
    }*/

    /* override fun onItemClickListener() {
         Toast.makeText(context, "ItemClicked", Toast.LENGTH_SHORT).show()
     }*/


}