package com.mu.jan.sparkchat.data.repository

import androidx.lifecycle.MutableLiveData
import com.mu.jan.sparkchat.data.database.MyFirebase
import com.mu.jan.sparkchat.data.model.chat.UserFriend

object MainActivityRepository {
    fun loadUserChats(onComplete : (MutableList<UserFriend>)->Unit){
        //load from Firebase
        MyFirebase.get().getUserFriends {
            onComplete(it)
        }

    }
}