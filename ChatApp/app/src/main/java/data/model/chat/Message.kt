package com.mu.jan.sparkchat.data.model.chat

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.google.gson.Gson
import com.mu.jan.sparkchat.R

class Message{
    lateinit var id: String
    lateinit var msg : String
    lateinit var senderId : String
    lateinit var receiverId : String
    lateinit var photo: MessagePhoto
    lateinit var chatOwner: ChatOwner
    lateinit var timeStamp: String
    lateinit var time : String
    lateinit var date : String
    lateinit var chatOwnerProfileUrl: String
    var hasSeen : Boolean = false

    constructor(){}
    constructor(id: String, msg: String, senderId: String, receiverId: String, photo: MessagePhoto, chatOwner: ChatOwner, timeStamp: String, time: String, date: String, chatOwnerProfileUrl: String,hasSeen : Boolean) {
        this.id = id
        this.msg = msg
        this.senderId = senderId
        this.receiverId = receiverId
        this.photo = photo
        this.chatOwner = chatOwner
        this.timeStamp = timeStamp
        this.time = time
        this.date = date
        this.chatOwnerProfileUrl = chatOwnerProfileUrl
        this.hasSeen = hasSeen
    }


    fun toMap() : HashMap<String,Any>{
        return hashMapOf<String,Any>(
            "id" to id,
            "msg" to msg,
            "senderId" to senderId,
            "receiverId" to receiverId,
            "photo" to photo,
            "chatOwner" to chatOwner,
            "timeStamp" to timeStamp,
            "time" to time,
            "date" to date,
            "chatOwnerProfileUrl" to chatOwnerProfileUrl,
            "hasSeen" to hasSeen
        )
    }
    fun copy():Message{
        val json = Gson().toJson(this,Message::class.java)
        return Gson().fromJson(json,Message::class.java)
    }
}
enum class ChatOwner{
    USER,FRIEND
}
@BindingAdapter("isMessageSeen")
fun isMessageSeen(view : ImageView,isMessageSeen : Boolean){
    if(isMessageSeen) view.setImageResource(R.drawable.ic_baseline_check_circle_accent_24)
    else view.setImageResource(R.drawable.ic_baseline_check_circle_24)
}