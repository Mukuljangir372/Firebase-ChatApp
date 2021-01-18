package com.mu.jan.sparkchat.data.model.chat

import android.view.View
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.google.gson.Gson
import de.hdodenhof.circleimageview.CircleImageView

class UserFriend{
    lateinit var id: String
    lateinit var name: String
    lateinit var phoneNumWithCountryCode : String
    lateinit var phoneNumWithoutCountryCode : String
    lateinit var imageUrl: String
    lateinit var timeStamp: String
    lateinit var lastChatDate: String
    lateinit var lastChatMsg: String
    var isNewMsgReceived : Boolean = false
    var isOnline : Boolean = false
    constructor(){}
    constructor(id: String, name: String, phoneNumWithCountryCode: String,phoneNumWithoutCountryCode : String,imageUrl: String, timeStamp: String, lastChatDate: String, lastChatMsg: String, isNewMsgReceived: Boolean, isOnline: Boolean) {
        this.id = id
        this.name = name
        this.phoneNumWithCountryCode = phoneNumWithCountryCode
        this.phoneNumWithoutCountryCode = phoneNumWithoutCountryCode
        this.imageUrl = imageUrl
        this.timeStamp = timeStamp
        this.lastChatDate = lastChatDate
        this.lastChatMsg = lastChatMsg
        this.isNewMsgReceived = isNewMsgReceived
        this.isOnline = isOnline
    }


    fun toMap() : HashMap<String,Any>{
        return hashMapOf<String,Any>(
            "id" to id,
            "name" to name,
            "phoneNumWithCountryCode" to phoneNumWithCountryCode,
            "phoneNumWithoutCountryCode" to phoneNumWithoutCountryCode,
            "imageUrl" to imageUrl,
            "timeStamp" to timeStamp,
            "lastChatDate" to lastChatDate,
            "lastChatMsg" to lastChatMsg,
            "isNewMsgReceived" to isNewMsgReceived,
            "isOnline" to isOnline
        )
    }
    fun copy():UserFriend{
        val json = Gson().toJson(this,UserFriend::class.java)
        return Gson().fromJson(json,UserFriend::class.java)
    }

}
@BindingAdapter("imageUrl")
fun loadImage(view : CircleImageView,url : String?) {
    if(!url.isNullOrEmpty()){
        //load image using Glide
        Glide.with(view.context).load(url).into(view)
    }
}
@BindingAdapter("isNewMsgReceived")
fun newMsgReceivedStatus(view : CircleImageView,isNewMsgReceived: Boolean){
    if(isNewMsgReceived) view.visibility = View.VISIBLE
    else view.visibility = View.GONE
}