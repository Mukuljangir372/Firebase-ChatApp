package com.mu.jan.sparkchat.ui.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.library.baseAdapters.BR
import androidx.recyclerview.widget.RecyclerView
import com.mu.jan.sparkchat.data.database.MyFirebase
import com.mu.jan.sparkchat.data.model.chat.UserFriend
import com.mu.jan.sparkchat.data.model.eventBus.ChatActivityEventBus
import com.mu.jan.sparkchat.databinding.SingleUserFriendItemBinding
import com.mu.jan.sparkchat.helper.ui.Job
import com.mu.jan.sparkchat.helper.ui.Toast
import com.mu.jan.sparkchat.ui.activities.ChatActivity
import com.mu.jan.sparkchat.ui.listeners.RvAdapterClickListener
import com.mu.jan.sparkchat.utils.Const
import org.greenrobot.eventbus.EventBus

class RvAdapter(val context:Context, val list : MutableList<UserFriend>) : RecyclerView.Adapter<RvAdapter.MyViewModel>(),RvAdapterClickListener{
    //MainActivityAdapter
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewModel {
        val binding = SingleUserFriendItemBinding.inflate(LayoutInflater.from(context),parent,false)
        return MyViewModel(binding)
    }
    override fun onBindViewHolder(holder: MyViewModel, position: Int) {
        holder.bind(list[position],position)
        holder.binding.itemClickListener = this
    }
    override fun getItemCount(): Int {
        return list.size
    }
    inner class MyViewModel(val binding: SingleUserFriendItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item : UserFriend,pos : Int){
            binding.setVariable(BR.userFriendModel,item)
            binding.executePendingBindings()
        }
    }
    override fun onItemClick(userFriend: UserFriend?) {
        //eventBus
        EventBus.getDefault().postSticky(ChatActivityEventBus(userFriend!!))
        //activity
        context.startActivity(Intent(context, ChatActivity::class.java)
                .putExtra(Const.ACTIVITY_CHAT_FRIEND_NAME,userFriend?.name)
                .putExtra(Const.ACTIVITY_CHAT_START_FROM_CONTACTS,false)
        )
    }

}