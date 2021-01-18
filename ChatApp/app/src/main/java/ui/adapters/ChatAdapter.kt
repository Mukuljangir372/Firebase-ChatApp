package com.mu.jan.sparkchat.ui.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.library.baseAdapters.BR
import androidx.recyclerview.widget.RecyclerView
import com.mu.jan.sparkchat.R
import com.mu.jan.sparkchat.data.model.chat.ChatOwner
import com.mu.jan.sparkchat.data.model.chat.Message
import com.mu.jan.sparkchat.databinding.SingleChatFriendBinding
import com.mu.jan.sparkchat.databinding.SingleChatUserBinding


class ChatAdapter(val context: Context, var list : MutableList<Message>) : RecyclerView.Adapter<ChatAdapter.MyViewModel>(){

    private val CHAT_FRIEND = 0
    private var CHAT_USER = 1

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewModel {
        val inflater = LayoutInflater.from(context)
        return when(viewType){
            CHAT_FRIEND -> {
                MyViewModel(SingleChatFriendBinding.inflate(inflater,parent,false))
            }
            CHAT_USER -> {
                MyViewModel(SingleChatUserBinding.inflate(inflater,parent,false))
            }
            else -> MyViewModel(SingleChatUserBinding.inflate(inflater,parent,false))
        }
    }
    override fun onBindViewHolder(holder: MyViewModel, position: Int) {
        holder.bind(list[position],position,holder.itemViewType)
    }
    override fun getItemCount(): Int {
        return list.size
    }
    inner class MyViewModel : RecyclerView.ViewHolder{
        private lateinit var singleChatFriendBinding: SingleChatFriendBinding
        private lateinit var singleChatUserBinding: SingleChatUserBinding

        constructor(binding:SingleChatFriendBinding) : super(binding.root) {
            singleChatFriendBinding = binding
        }
        constructor(binding: SingleChatUserBinding) : super(binding.root){
            singleChatUserBinding = binding
        }

        fun bind(item : Message ,pos : Int,viewType : Int){
            when(viewType){
                CHAT_USER -> {
                    singleChatUserBinding.setVariable(BR.message,item)
                    singleChatUserBinding.executePendingBindings()
                }
                CHAT_FRIEND -> {
                    singleChatFriendBinding.setVariable(BR.message,item)
                    singleChatFriendBinding.executePendingBindings()
                }
            }

        }
    }

    override fun getItemViewType(position: Int): Int {
        return if(list[position].chatOwner == ChatOwner.FRIEND){
            CHAT_FRIEND
        }else {
            CHAT_USER
        }
    }
}