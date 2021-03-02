package com.techbayportal.itaste.ui.fragments.directmessagesfragment

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.asLiveData
import androidx.navigation.Navigation
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.techbayportal.itaste.BR
import com.techbayportal.itaste.R
import com.techbayportal.itaste.baseclasses.BaseFragment
import com.techbayportal.itaste.data.local.datastore.DataStoreProvider
import com.techbayportal.itaste.data.models.CartVendor
import com.techbayportal.itaste.databinding.FragmentDirectMessagesBinding
import com.techbayportal.itaste.ui.fragments.directmessagesfragment.adapter.DirectMessagesAdapter
import com.techbayportal.itaste.utils.LoginSession
import com.techbayportal.itaste.data.models.chat.ChatMessageDataClass
import com.techbayportal.itaste.data.models.chat.GeneralInboxDataClass
import com.techbayportal.itaste.ui.activities.signupactivity.SignupActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_direct_messages.*
import kotlinx.android.synthetic.main.fragment_direct_messages.img_back
import kotlinx.android.synthetic.main.fragment_direct_messages.linear
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import timber.log.Timber

@AndroidEntryPoint
class DirectMessagesFragment : BaseFragment<FragmentDirectMessagesBinding, DirectMessagesFragmentViewModel>() {

    override val layoutId: Int
        get() = R.layout.fragment_direct_messages
    override val viewModel: Class<DirectMessagesFragmentViewModel>
        get() = DirectMessagesFragmentViewModel::class.java
    override val bindingVariable: Int
        get() = BR.viewModel


    val inboxArray = arrayListOf<GeneralInboxDataClass>()
    private var currentUserId = 0
    private var currentUserName = ""
    private var currentUserImage = ""
    private lateinit var adapter: DirectMessagesAdapter

    val loginSession = LoginSession.getInstance().getLoginResponse()
    //lateinit var dataStoreProvider: DataStoreProvider
    private var guestModeNotificationUi: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

      //  dataStoreProvider = DataStoreProvider(requireContext())
        //guest mode stuff

        GlobalScope.launch {
            val guestMode = mViewModel.dataStoreProvider.guestModeFlow.first()
            if (guestMode) {
                guestModeNotificationUi = true
                Timber.d("Guest Mode On")
            }else{
                guestModeNotificationUi = false
                Timber.d("Guest Mode Off")
            }
        }


    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (guestModeNotificationUi) {
            ll_guestModeDirectMessage.visibility = View.VISIBLE
            ll_new_message.visibility = View.GONE
            linear.visibility = View.GONE
            tv_message.visibility = View.GONE
            llUserList.visibility = View.GONE
        }else if(!guestModeNotificationUi){
            ll_guestModeDirectMessage.visibility = View.GONE
            ll_new_message.visibility = View.VISIBLE
            linear.visibility = View.VISIBLE
            tv_message.visibility = View.VISIBLE
            llUserList.visibility = View.VISIBLE

            if (loginSession != null) {

                currentUserId = loginSession.data.id
                currentUserName = loginSession.data.first + " " + loginSession.data.last
                currentUserImage = loginSession.data.profile_pic
            }


            adapter = DirectMessagesAdapter(inboxArray, object : DirectMessagesAdapter.ClickListener {
                override fun onClick(data: GeneralInboxDataClass) {
                    // Toast.makeText(context, " Inbox ItemClicked : ${data.senderId}", Toast.LENGTH_SHORT).show()
                    Timber.d("Inbox ItemClicked : ${data.senderId}")
                    sharedViewModel.vendorDetailsForCart = CartVendor("", data.senderName, data.senderId.toInt(), "", data.imgStr)
                    sharedViewModel.cartPost = null
                    Navigation.findNavController(ll_new_message)
                        .navigate(R.id.action_directMessagesFragment_to_chatFragment2)
                }

            })
            rvUsersForChat.adapter = adapter
            getInboxList()
            Timber.d("Guest Mode Off")
        }
        /*dataStoreProvider.guestModeFlow.asLiveData().observe(viewLifecycleOwner, Observer {
            if (it) {
                Timber.d("Guest Mode On")
                //Toast.makeText(context, "Guest Mode On", Toast.LENGTH_SHORT).show()
                ll_guestModeDirectMessage.visibility = View.VISIBLE
                ll_new_message.visibility = View.GONE
                linear.visibility = View.GONE
                tv_message.visibility = View.GONE
                llUserList.visibility = View.GONE
            } else {

            }

        })*/


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

        mViewModel.onSignInButtonClicked.observe(this, Observer {
            mViewModel.setGuestMode(false)
            navigateToLoginScreen()

        })
    }

    private fun navigateToLoginScreen() {
        val intent = Intent(activity, SignupActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
        startActivity(intent)

    }

    private fun getInboxList() {


        val fireStoreObj = FirebaseFirestore.getInstance()
        inboxArray.clear()
        fireStoreObj.collection("Chats")
            .whereArrayContains("usersId", currentUserId.toString())
            .orderBy("lastMsgTime", Query.Direction.ASCENDING)
            .whereEqualTo("groupChat", false).get()
            .addOnSuccessListener {
                it.documents.forEach { doc ->
                    val inboxData = doc.toObject(GeneralInboxDataClass::class.java)!!
                    val chatUser = inboxData.users.find { user -> user.id != currentUserId.toString() }

                    //added for online status
                    /* fireStoreObj.collection("UserStatus")
                         .document(chatUser!!.id)
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
                         }*/
                    fireStoreObj.collection("Chats")
                        .document(inboxData.id)
                        .collection("Threads")
                        .orderBy("createdAt", Query.Direction.DESCENDING)
                        .get()
                        .addOnSuccessListener { messageArray ->
                            val filteredMessagesArray = arrayListOf<ChatMessageDataClass>()
                            if (messageArray.documents.isNotEmpty()) {
                                //llNoMessages.visibility = View.GONE
                                messageArray.documents.forEach { messageDoc ->
                                    val messageData = messageDoc.toObject(ChatMessageDataClass::class.java)
                                    if (!messageData!!.deletedFor.contains(currentUserId.toString()))
                                        filteredMessagesArray.add(messageData)
                                }

                                val lastMessageData = filteredMessagesArray.first()

                                inboxData.senderId = chatUser?.id ?: "0"
                                inboxData.senderName = chatUser?.name ?: ""
                                inboxData.imgStr = chatUser!!.imgStr
                               // inboxData.imgStr = currentUserImage
                                inboxData.lastMsg = lastMessageData.message
                                inboxData.lastMsgTime = lastMessageData.createdAt

                                var unreadCount = 0
                                filteredMessagesArray.forEach { messgeDoc ->
                                    if (messgeDoc.seenBy.isEmpty() && messgeDoc.senderId != currentUserId.toString()) {
                                        unreadCount++
                                    }
                                }
                                inboxData.unreadMessages = unreadCount
                                inboxArray.add(0, inboxData)
                                adapter.notifyDataSetChanged()
                            } else {
                                //llNoMessages.visibility  =View.VISIBLE
                                showPlaceholder()
                            }
                            //   showPlaceholder()

                        }
                        .addOnFailureListener { messageArrayExcpetion ->
                            //  showPlaceholder()
                            //llNoMessages.visibility  =View.VISIBLE

                            messageArrayExcpetion
                            Timber.d("$messageArrayExcpetion")
                        }
                }
            }.addOnFailureListener { inboxException ->
                // showPlaceholder()
                //llNoMessages.visibility  =View.VISIBLE
                inboxException
            }
    }

    private fun getOnlineStatus() {

    }

    private fun showPlaceholder() {
        llNoMessages.visibility = View.VISIBLE
        rvUsersForChat.visibility = View.GONE
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