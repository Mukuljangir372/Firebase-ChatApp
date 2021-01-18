package com.mu.jan.sparkchat.data.repository

import android.content.ContentResolver
import com.mu.jan.sparkchat.data.model.chat.UserFriend
import com.mu.jan.sparkchat.helper.ContactHelper
import com.mu.jan.sparkchat.helper.ui.Job

object ContactsActivityRepository {
    fun getAvailableUserList(contentResolver : ContentResolver,onSuccess : (MutableList<UserFriend>)->Unit){
        //load all contact list from phone
        var mList : MutableList<UserFriend> = mutableListOf()
        ContactHelper.getContactList(contentResolver){
            /**
             * Check contact phone num, If this user is a user of SparkChat
             */
            for(contact in it){
                mList.add(UserFriend("",
                        contact.name,
                        "",
                        "",
                        "",
                        "",
                        "",
                        contact.phoneNum,
                        false,
                        false
                ))
            }
            Job.MAIN{
                onSuccess(mList)
            }

        }
    }
}