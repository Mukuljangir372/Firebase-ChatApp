package com.mu.jan.sparkchat.data.repository

import com.mu.jan.sparkchat.data.database.MyFirebase
import com.mu.jan.sparkchat.data.model.chat.Message

object ChatActivityRepository {
    fun getChats(userFriendId : String,onComplete:(MutableList<Message>)->Unit){
        MyFirebase.get().getChatMessages(userFriendId){
            onComplete(it)
        }
    }

}