package com.techbayportal.itaste.ui.fragments.chatfragment

import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.techbayportal.itaste.BR
import com.techbayportal.itaste.R
import com.techbayportal.itaste.baseclasses.BaseFragment
import com.techbayportal.itaste.databinding.FragmentChatBinding
import com.techbayportal.itaste.ui.fragments.chatfragment.adapter.ChatAdapter
import com.techbayportal.itaste.ui.fragments.chatfragment.itemClickListener.ChatRvItemClickListener
import com.techbayportal.offsidesportsapp.data.models.chat.ChatMessageDataClass
import com.techbayportal.itaste.data.models.chat.GeneralInboxDataClass
import com.techbayportal.offsidesportsapp.data.models.chat.UserInfoClass
import com.techbayportal.itaste.data.models.chat.UserStatusDataClass
import com.techbayportal.itaste.utils.LoginSession
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_chat.*
import kotlinx.android.synthetic.main.layout_write_message.*
import timber.log.Timber
import java.util.*
import kotlin.collections.ArrayList

@AndroidEntryPoint
class ChatFragment : BaseFragment<FragmentChatBinding, ChatFragmentViewModel>(),
    ChatRvItemClickListener {

    override val layoutId: Int
        get() = R.layout.fragment_chat
    override val viewModel: Class<ChatFragmentViewModel>
        get() = ChatFragmentViewModel::class.java
    override val bindingVariable: Int
        get() = BR.viewModel


    private var isLoadData: Boolean = false
    lateinit var firestoreRef: FirebaseFirestore
    private val chatMessageArray = arrayListOf<ChatMessageDataClass>()
    lateinit var layoutManager: LinearLayoutManager


    var itemPos = 0
    var mLastKey: String? = ""
    var mLastIndex = 0
    var mPrevKey: String? = ""

    val totalItemsToLoad = 10
    private var currentPage = 1
    private var selectedName = ""
    private var selectedId = ""
    private var selectedImage = ""
    private var chatKey: String? = null
    lateinit var seenListener: ListenerRegistration


    private var currentUserName = ""
    private var currentUserId = ""
    private lateinit var chatAdapter: ChatAdapter

    val loginSession = LoginSession.getInstance().getLoginResponse()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //mViewDataBinding.rvMessages.adapter = ChatAdapter(requireContext())

        firestoreRef = Firebase.firestore

/*Data of 2nd person to chat with*/
        // selectedName = intent.getStringExtra("name")!!
        sharedViewModel.vendorDetailsForCart?.let {
            selectedName = it.first + " " + it.last
            selectedId = it.id.toString()
        }

//        selectedImage =

        val userList = ArrayList<Int>()
        userList.add(loginSession!!.data.id)
        userList.add(selectedId.toInt())
        val sortedArray = userList.sorted()
        chatKey = "${sortedArray[0]}_${sortedArray[1]}"


        tv_reciverName.text = selectedName

        tvPostMessage.setOnClickListener {
            if (tv_message.text.isNotEmpty()) {
                val msg = tv_message.text
                sendMessage(msg.toString())
                msg.clear()
            } else {

            }

        }

//        if (chatKey != null)

        getUserStatus(selectedId)

        seenMessage()
        readMessages(chatKey!!)

        rvMessages.layoutManager = layoutManager
        layoutManager.reverseLayout = true
        //rvMessages.adapter = ChatAdapter(chatMessageArray, currentUserId, requireContext())
        currentUserId = loginSession.data.id.toString()
        chatAdapter = ChatAdapter(chatMessageArray, currentUserId, requireContext())
        rvMessages.adapter = chatAdapter


        rvMessages.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(v: RecyclerView, dx: Int, scrollY: Int) {
                super.onScrolled(v, dx, scrollY)

                if (v.getChildAt(v.childCount - 1) != null) {
                    if (
                        (scrollY >= (v.getChildAt(v.childCount - 1)
                            .measuredHeight - v.measuredHeight)) &&
                        scrollY < 0
                    ) {
                        val visibleItemCount = rvMessages.childCount
                        val totalItemCount = layoutManager.itemCount
                        val firstVisibleItem =
                            layoutManager.findFirstCompletelyVisibleItemPosition()

                        if (isLoadData) {
                            if ((visibleItemCount + firstVisibleItem) >= totalItemCount) {
                                currentPage += 1
                                // getPaginatedMessages()
                                isLoadData = false
                            }
                        }
                    }
                }
            }
        })

        currentUserName = loginSession.data.first + " " + loginSession.data.last
        currentUserId = loginSession.data.id.toString()
        layoutManager = LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)

    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        //dataStoreProvider = DataStoreProvider(context)
        layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
    }


    private fun sendMessage(msg: String) {

        val chatRef = firestoreRef.collection("Chats").document(chatKey!!)
        val msgRef = chatRef.collection("Threads").document()
        val secondKey = msgRef.id
        val currentDate = Date()
        val messageObj = ChatMessageDataClass()
        messageObj.message = msg
        messageObj.senderId = currentUserId
        messageObj.senderName = currentUserName
        messageObj.id = secondKey
        messageObj.createdAt = currentDate

        chatRef.collection("Threads").document(secondKey)
            .set(messageObj).addOnSuccessListener {
                // Toast.makeText(requireContext(), "Messages Sent", Toast.LENGTH_LONG).show()
                Timber.d("Chat: Messages Sent")
                tv_message.text.clear()
                updateInbox(chatKey!!, chatRef, msg, currentDate)
            }.addOnFailureListener {
                Toast.makeText(requireContext(), "Thread Error:$it", Toast.LENGTH_SHORT).show()
            }
    }

    private fun sendOrderDetailsMessage(orderId: String, orderImage: String, orderMessage: String) {

        val chatRef = firestoreRef.collection("Chats").document(chatKey!!)
        val msgRef = chatRef.collection("Threads").document()
        val secondKey = msgRef.id
        val currentDate = Date()
        val messageObj = ChatMessageDataClass()
        messageObj.orderId = orderId
        messageObj.orderImage = orderImage
        messageObj.message = orderMessage
        messageObj.senderId = currentUserId
        messageObj.senderName = currentUserName
        messageObj.id = secondKey
        messageObj.createdAt = currentDate

        chatRef.collection("Threads").document(secondKey)
            .set(messageObj).addOnSuccessListener {
                //Toast.makeText(requireContext(), "Messages Sent", Toast.LENGTH_LONG).show()
                Timber.d("Chat: Messages Sent")
                updateInbox(chatKey!!, chatRef, orderMessage, currentDate)
            }.addOnFailureListener {
                Toast.makeText(requireContext(), "Thread Error:$it", Toast.LENGTH_SHORT).show()
            }
    }

    override fun onPause() {
        super.onPause()
        seenListener.remove()
    }


    private fun updateInbox(
        firstKey: String,
        chatRef: DocumentReference,
        msg: String,
        currentDate: Date
    ) {

        val inboxDataClass = GeneralInboxDataClass()
        inboxDataClass.users = arrayListOf(
            UserInfoClass(selectedName, selectedId, ""),
            UserInfoClass(currentUserName, currentUserId, "")
        )

        inboxDataClass.usersId = arrayListOf(currentUserId, selectedId)
        inboxDataClass.lastMsg = null
        inboxDataClass.id = firstKey
        inboxDataClass.lastMsgTime = Date()
        inboxDataClass.senderId = ""
        inboxDataClass.senderName = ""
        inboxDataClass.isGroupChat = false

//        try {
        chatRef.set(inboxDataClass).addOnSuccessListener {

        }.addOnFailureListener {
            Toast.makeText(requireContext(), it.toString(), Toast.LENGTH_LONG).show()
        }
//        }catch (e:Exception){
//            e
//        }
    }

    private fun getUserStatus(selectedId: String) {
        val userStatusRef = firestoreRef.collection("UserStatus").document(selectedId)
        userStatusRef.addSnapshotListener { value, error ->
            if (error != null) {
                Toast.makeText(requireContext(), error.localizedMessage, Toast.LENGTH_SHORT).show()
                return@addSnapshotListener
            }
            val userStatus = value!!.toObject(UserStatusDataClass::class.java)
            if (userStatus != null) {
                if (userStatus.state == "offline") {
                    //  binding.userStatus.text = "Last Seen:${userStatus.lastSeen}"
                    Toast.makeText(
                        requireContext(),
                        "Last Seen:${userStatus.lastSeen}",
                        Toast.LENGTH_SHORT
                    ).show()
                } else if (userStatus.state == "Online") {
                    Toast.makeText(
                        requireContext(),
                        "Last Seen:${userStatus.lastSeen}",
                        Toast.LENGTH_SHORT
                    ).show()
                    //  binding.userStatus.text = userStatus.state
                }

            }
        }
    }

    private fun seenMessage() {
        val currentUserList = listOf(currentUserId)
        val getReceiverSentMsgRef =
            firestoreRef.collection("Chats").document(chatKey!!)
                .collection("Threads")
                .whereEqualTo("senderId", selectedId)

        seenListener = getReceiverSentMsgRef.addSnapshotListener { value, error ->
            if (error != null) {
               // Toast.makeText(requireContext(), error.localizedMessage, Toast.LENGTH_SHORT).show()
                Timber.d(error.localizedMessage)
                return@addSnapshotListener
            }
            if (value?.documents != null && value.documents.isNotEmpty()) {
                value.documents.forEach {
                    val message = it.toObject(ChatMessageDataClass::class.java)!!
                    if (!message.seenBy.contains(currentUserId)
                        &&
                        !message.deletedFor.contains(currentUserId)
                    ) {
                        firestoreRef.collection("Chats").document(chatKey!!)
                            .collection("Threads").document(message.id)
                            .update("seenBy", currentUserList).addOnSuccessListener {
                                it
                            }.addOnFailureListener { exception ->
                                exception
                            }
                    }
                }
            }
        }
    }

    private fun readMessages(selectedId: String) {
        val readMessageRef = firestoreRef.collection("Chats")
            .document(selectedId).collection("Threads")
            .orderBy("createdAt", Query.Direction.DESCENDING)

        readMessageRef.addSnapshotListener { value, error ->
            if (error != null) {
                //Toast.makeText(requireContext(), error.localizedMessage, Toast.LENGTH_SHORT).show()
                Timber.d(error.localizedMessage)
                return@addSnapshotListener
            }
            if (value != null && value.documents.isNotEmpty()) {
                chatMessageArray.clear()
                value.documents.forEach {
                    val message = it.toObject(ChatMessageDataClass::class.java)!!
                    if (!message.deletedFor.contains(currentUserId)) {
                        itemPos++
                        if (itemPos == 1) {
                            val messageKey = message.id
                            mLastKey = messageKey
                            mPrevKey = messageKey
                        }
                        chatMessageArray.add(message)
                    }
                    isLoadData = true
                    chatAdapter.notifyDataSetChanged()
                }
            } else if (value != null && value.documents.isEmpty()) {
                if (sharedViewModel.cartPost != null) {
                    sendOrderDetailsMessage(
                        sharedViewModel.cartPost!!.id.toString(),
                        sharedViewModel.cartPost!!.image,
                        getString(R.string.hello_i_saw_this_post_of_yours_yesterday_and_i_couldn_t_stop_myself_from_ordering_it_i_need_to_know_if_you_are_still_deliveri_ng_this_or_not_also_how_much_are_you_charging_for_delivery_service_good_day_to_you)
                    )

                }
            }
        }

    }

    private fun getPaginatedMessages() {

        loadingDialog.show()
        val readMessageRef = firestoreRef.collection("Chats")
            .document(chatKey!!).collection("Threads")
            .orderBy("createdAt", Query.Direction.DESCENDING)
            .startAfter(chatMessageArray.last().createdAt)
            .limit((totalItemsToLoad).toLong())

        readMessageRef.addSnapshotListener { value, error ->
            if (error != null) {
                Toast.makeText(requireContext(), error.localizedMessage, Toast.LENGTH_SHORT).show()
                return@addSnapshotListener
            }

            value!!.documents.forEach {
                val message = it.toObject(ChatMessageDataClass::class.java)!!
                if (!message.deletedFor.contains(currentUserId)) {
                    val messageKey = message.id
                    if (mPrevKey != messageKey) {
                        chatMessageArray.add(message)
                    } else {
                        mPrevKey = mLastKey
                    }
                    if (itemPos == 1) {
                        mLastKey = messageKey
                    }
                    isLoadData = true
                    chatAdapter.notifyDataSetChanged()
                    loadingDialog.show()
                }
            }
        }

    }


    override fun onItemClickListener() {
        // Toast.makeText(context, "ItemClicked", Toast.LENGTH_SHORT).show()
    }

}