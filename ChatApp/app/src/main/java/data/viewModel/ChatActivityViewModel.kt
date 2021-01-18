package com.mu.jan.sparkchat.data.viewModel

import android.content.Context
import androidx.lifecycle.ViewModel
import com.mu.jan.sparkchat.data.database.MyFirebase
import com.mu.jan.sparkchat.data.model.chat.Message
import com.mu.jan.sparkchat.data.repository.ChatActivityRepository
import com.mu.jan.sparkchat.databinding.ActivityChatBinding
import com.mu.jan.sparkchat.ui.adapters.ChatAdapter
import com.mu.jan.sparkchat.ui.listeners.RvScroll


class ChatActivityViewModel constructor() : ViewModel() {

    private lateinit var context: Context
    private lateinit var binding : ActivityChatBinding
    //adapter
    private lateinit var rvAdapter : ChatAdapter
    //firebase
    private lateinit var myFirebase : MyFirebase

    var isUserNeedScrollStateBottom = false
    var isUserNeedSmoothScrollStateBottom = false

    var isChatListEmpty : Boolean = true
    //message list
    var messageList = mutableListOf<Message>()

    fun loadChatList(context : Context,userFriendId : String,binding : ActivityChatBinding){
        this.context = context
        this.binding = binding
        //load
        ChatActivityRepository.getChats(userFriendId){
            loadAdapter(it)
        }
        //chat listener
        myFirebase = MyFirebase.get()
        myFirebase.registerChatListener(userFriendId){
            refreshAdapter(it)
        }

    }
    private fun loadAdapter(list : MutableList<Message>){
        isChatListEmpty = list.size<=0
        messageList = list
        //adapter
        initRvAdapter()
        //scroll to bottom
        binding.activityChatRv.scrollToPosition(list.size-1)
        //Rv scroll listener
        RvScroll.setListener(binding.activityChatRv,this)
    }
    fun refreshAdapter(list : MutableList<Message>){
        if(!this::rvAdapter.isInitialized){ initRvAdapter() }
        if(list.size<=0){
            isChatListEmpty = true
        }else {
            isChatListEmpty = false
            messageList = list
            rvAdapter.list = messageList
            //notifyDataSetChanged
            binding.activityChatRv.adapter?.notifyDataSetChanged()
            //scroll to bottom
            if(isUserNeedScrollStateBottom) binding.activityChatRv.scrollToPosition(list.size-1)
            if(isUserNeedSmoothScrollStateBottom) binding.activityChatRv.smoothScrollToPosition(list.size-1)
        }
    }
    private fun initRvAdapter(){
        rvAdapter = ChatAdapter(context,messageList)
        //set adapter
        binding.activityChatRv.adapter = rvAdapter
    }

}