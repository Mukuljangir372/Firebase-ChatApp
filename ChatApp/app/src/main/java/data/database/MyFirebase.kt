package com.mu.jan.sparkchat.data.database

import android.telephony.PhoneNumberUtils
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.*
import com.google.firebase.firestore.EventListener
import com.mu.jan.sparkchat.data.model.chat.*
import com.mu.jan.sparkchat.data.model.user.User
import com.mu.jan.sparkchat.helper.ui.Job
import com.mu.jan.sparkchat.utils.Const
import kotlinx.coroutines.launch
import java.util.*

class MyFirebase
{
    //fireStore
    private val store = FirebaseFirestore.getInstance()
    //firebaseUser
    private val user = FirebaseAuth.getInstance().currentUser
    val uid = user?.uid
    //userDocument
    private val userDocument = store.collection(Const.FIREBASE_USERS).document(uid+"")

    companion object{
        private lateinit var instance : MyFirebase
        fun get() : MyFirebase{
            return if(this::instance.isInitialized) instance
            else MyFirebase()
        }
    }
    //loginActivity
    fun saveUserInfo(onSaved : ()->Unit){
        userDocument.get().addOnCompleteListener {
            if(it.isSuccessful){
                //if user already set data, no need to set user data again
                if(!it.result!!.exists()){
                    val phoneNumWithCountryCode = user?.phoneNumber!!
                    val phoneNumWithoutCountryCode = "${PhoneNumberUtils.formatNumber(phoneNumWithCountryCode,Locale.getDefault().country)}"
                    it.result!!.reference.set(User(user?.uid!!,
                        "",
                        phoneNumWithCountryCode,
                        phoneNumWithoutCountryCode,
                        ""
                        ,
                    ).toMap())
                    onSaved()
                }else {
                    onSaved()
                }
            }
        }
    }
    //find
    fun findUser(phoneNumWithCountryCode:String,phoneNumWithoutCountryCode:String,onFindUser : (User) -> Unit,onNotFindUser : ()->Unit){
       var isFind : Boolean = false
       queryUser("phoneNumWithCountryCode",phoneNumWithCountryCode,{
           isFind = true
           onFindUser(it)
       },{
           isFind = false
       })
       if(!isFind){
           queryUser("phoneNumWithoutCountryCode",phoneNumWithoutCountryCode,{
               onFindUser(it)
           },{
               onNotFindUser()
           })
       }
    }
    private fun queryUser(field : String,phoneNum : String,onFindUser : (User) -> Unit,onNotFindUser : ()->Unit){
        store.collection(Const.FIREBASE_USERS).whereEqualTo(field,phoneNum)
                .get().addOnCompleteListener {
                    if(it.isSuccessful) {
                        if(it.result!!.size()<=0){
                                onNotFindUser()
                        }else {
                            for (doc in it.result!!) {
                                val user = doc.toObject(User::class.java)
                                onFindUser(user)
                            }
                        }

                    }else {
                        onNotFindUser()
                    }
                }
    }
    fun getUserFriendDocumentRef(friendId : String) : DocumentReference{
        return store.collection(Const.FIREBASE_USERS).document(friendId)
    }
    fun getUserInfo(onUserInfo:(User)->Unit){
        userDocument.get().addOnCompleteListener {
            if(it.isSuccessful){
                val user = it.result!!.toObject(User::class.java)
                onUserInfo(user!!)
            }
        }
    }
    //chat
    fun getUserFriends(onComplete : (MutableList<UserFriend>)->Unit){
        userDocument.collection(Const.FIREBASE_USER_FRIENDS).get().addOnCompleteListener {
            if(it.isSuccessful){
                val list = mutableListOf<UserFriend>()
                for(doc in it.result!!) {
                    val friend = doc.toObject(UserFriend::class.java)
                    list.add(friend)
                }
                onComplete(list)
            }
        }
    }
    //message
    fun sendMessage(message: Message,userFriendDocRef : DocumentReference,user: User,userFriend : UserFriend){
        //userFriend - send message
        //send user info to friend,so that he can identify you
        userFriend.id = user.id
        userFriend.name = user.name
        if(user.name == "") userFriend.name = userFriend.phoneNumWithoutCountryCode
        userFriend.phoneNumWithCountryCode = user.phoneNumWithCountryCode
        userFriend.phoneNumWithoutCountryCode = user.phoneNumWithoutCountryCode
        userFriend.imageUrl = user.imageUrl
        userFriend.isNewMsgReceived = true
        userFriend.lastChatDate = message.date
        userFriend.lastChatMsg = message.msg
        Job.BACK {
            it.launch {
                //msg
                message.chatOwner = ChatOwner.FRIEND
                userFriendDocRef.collection(Const.FIREBASE_USER_FRIENDS).document(user.id)
                        .collection(Const.FIREBASE_CHATS).document(message.id).set(message.toMap())
            }
            it.launch {
                //update
                userFriendDocRef.collection(Const.FIREBASE_USER_FRIENDS).document(user.id)
                        .set(userFriend.toMap())
            }
        }


    }
    fun storeMessage(message: Message,userFriend : UserFriend){
        //user - store message
        userFriend.lastChatMsg = message.msg
        userFriend.lastChatDate = message.date
        Job.BACK {
            it.launch {
                //msg
                message.chatOwner = ChatOwner.USER
                userDocument.collection(Const.FIREBASE_USER_FRIENDS).document(userFriend.id)
                        .collection(Const.FIREBASE_CHATS).document(message.id).set(message.toMap())
            }
            it.launch {
                //update
                userDocument.collection(Const.FIREBASE_USER_FRIENDS).document(userFriend.id)
                        .set(userFriend.toMap())
            }

        }


    }
    fun getChatMessages(userFriendId : String,onComplete: (MutableList<Message>) -> Unit){
        userDocument.collection(Const.FIREBASE_USER_FRIENDS).document(userFriendId)
                .collection(Const.FIREBASE_CHATS).get().addOnCompleteListener {
                    if(it.isSuccessful){
                        var list = mutableListOf<Message>()
                        for(doc in it.result!!){
                            var message = doc.toObject(Message::class.java)
                            list.add(message)
                        }
                        list.sortByDescending { it.timeStamp }
                        list.reverse()
                        onComplete(list)
                    }
                }
    }
    fun registerChatListener(userFriendId : String,onChatChanged:(MutableList<Message>)->Unit){
        userDocument.collection(Const.FIREBASE_USER_FRIENDS).document(userFriendId)
                .collection(Const.FIREBASE_CHATS).addSnapshotListener { value, error ->
                    value?.query!!.get().addOnCompleteListener {
                        if(it.isSuccessful){
                            var list = mutableListOf<Message>()
                            for(doc in it.result!!){
                                var message = doc.toObject(Message::class.java)
                                list.add(message)
                            }
                            list.sortByDescending { it.timeStamp }
                            list.reverse()
                            Job.BACK {
                                markAllMessagesSeen(getUserFriendDocumentRef(userFriendId))
                            }
                            onChatChanged(list)
                        }
                    }
                }
    }
    private fun markAllMessagesSeen(userFriendDocRef: DocumentReference){
        userFriendDocRef.collection(Const.FIREBASE_USER_FRIENDS).document(user?.uid!!)
            .collection(Const.FIREBASE_CHATS).get().addOnCompleteListener {
                if(it.isSuccessful){
                    for(doc in it.result!!){
                        doc.reference.update("hasSeen",true)
                    }
                }
            }
    }
}