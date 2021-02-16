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
import com.techbayportal.itaste.databinding.FragmentDirectMessagesBinding
import com.techbayportal.itaste.ui.fragments.directmessagesfragment.adapter.DirectMessagesAdapter
import com.techbayportal.itaste.ui.fragments.directmessagesfragment.itemClickListener.DirectMessagesRvItemClickListener
import com.techbayportal.offsidesportsapp.data.models.chat.ChatMessageDataClass
import com.techbayportal.offsidesportsapp.data.models.chat.GeneralInboxDataClass
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_direct_messages.*

@AndroidEntryPoint
class DirectMessagesFragment : BaseFragment<FragmentDirectMessagesBinding, DirectMessagesFragmentViewModel>() {

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
        getInboxList()
        mViewDataBinding.rvUsersForChat.adapter = DirectMessagesAdapter(inboxArray,object :DirectMessagesAdapter.ClickListener{
            override fun onClick(data: GeneralInboxDataClass) {
                Toast.makeText(context, " Inbox ItemClicked : ${data.id}", Toast.LENGTH_SHORT).show()
            }

        })

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
                        }
                }
            }.addOnFailureListener { inboxException ->
               // showPlaceholder()
                inboxException
            }
    }

        /*
         val fireStoreObj = FirebaseFirestore.getInstance()
        fireStoreObj.collection("Chats")
            .whereArrayContains("usersId", currentUserId)
            .orderBy("lastMsgTime", Query.Direction.ASCENDING)
            .whereEqualTo("groupChat", false)
            .get()
            .addOnSuccessListener {
                if (it!!.documents.isNotEmpty()) {
                    inboxArray.clear()
                    it.documents.forEach { documentSnapShot ->
                        val data = documentSnapShot.toObject(GeneralInboxDataClass::class.java)!!
                        inboxArray.add(0, data)
                        rvUsersForChat.adapter!!.notifyDataSetChanged()
                    }
                }
            }.addOnFailureListener {
                Toast.makeText(requireContext(), it.localizedMessage, Toast.LENGTH_SHORT).show()
            }*/


        /*inboxArray.clear()
        fireStoreObj.collection("Chats")
            .whereArrayContains("usersId", currentUserId)
            .orderBy("lastMsgTime", Query.Direction.ASCENDING)
            .whereEqualTo("groupChat", false).get()
            .addOnSuccessListener {
                it.documents.forEach { doc ->
                    val inboxData = doc.toObject(GeneralInboxDataClass::class.java)!!
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

                                inboxData.senderId = lastMessageData.senderId
                                inboxData.senderName = lastMessageData.senderName
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
                                rvUsersForChat.adapter!!.notifyDataSetChanged()
                            }
                            else{
                                Toast.makeText(requireContext(), "Inbox Is Empty", Toast.LENGTH_SHORT).show()
                            }
                        }
                        .addOnFailureListener { messageArrayExcpetion ->
                            messageArrayExcpetion
                        }
                }
            }.addOnFailureListener { inboxException ->
                inboxException
            }*/



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