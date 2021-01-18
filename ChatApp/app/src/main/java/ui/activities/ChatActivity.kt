package com.mu.jan.sparkchat.ui.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import com.google.firebase.firestore.DocumentReference
import com.mu.jan.sparkchat.R
import com.mu.jan.sparkchat.SparkChat
import com.mu.jan.sparkchat.data.database.MyFirebase
import com.mu.jan.sparkchat.data.model.chat.ChatOwner
import com.mu.jan.sparkchat.data.model.chat.Message
import com.mu.jan.sparkchat.data.model.chat.MessagePhoto
import com.mu.jan.sparkchat.data.model.chat.UserFriend
import com.mu.jan.sparkchat.data.model.eventBus.ChatActivityEventBus
import com.mu.jan.sparkchat.data.model.user.User
import com.mu.jan.sparkchat.data.viewModel.ChatActivityViewModel
import com.mu.jan.sparkchat.databinding.ActivityChatBinding
import com.mu.jan.sparkchat.helper.ui.Calendar
import com.mu.jan.sparkchat.helper.ui.Job
import com.mu.jan.sparkchat.helper.ui.Toast
import com.mu.jan.sparkchat.helper.ui.ViewModelLoader
import com.mu.jan.sparkchat.ui.adapters.ChatAdapter
import com.mu.jan.sparkchat.utils.Const
import kotlinx.coroutines.launch
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import java.util.*

class ChatActivity : AppCompatActivity() {

    //binding
    private lateinit var binding : ActivityChatBinding
    //viewModel
    private lateinit var vm : ChatActivityViewModel
    //chat
    private lateinit var user : User
    private lateinit var userFriend: UserFriend
    private var isFriendIsUserOfApp : Boolean = false
    //database
    private lateinit var myFirebase: MyFirebase
    private lateinit var userFriendFirebaseDocumentRef : DocumentReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChatBinding.inflate(layoutInflater)
        setContentView(binding.root)
        //viewModel
        vm = ViewModelLoader.load(this@ChatActivity)
        myFirebase = MyFirebase.get()
        //toolbar
        setSupportActionBar(binding.activityChatToolbar)
        binding.activityChatToolbar.setNavigationOnClickListener { onBackPressed() }
        binding.activityChatToolbarTitle.text = intent.getStringExtra(Const.ACTIVITY_CHAT_FRIEND_NAME)
        //chat
        user = SparkChat.get().user
        binding.activityChatSend onClick {
            sendMessage()
        }

    }
    private fun refresh(){}
    private fun loadData(){
        //check if user friend is a user of app
        if(userFriend.id!=""){
            isFriendIsUserOfApp = true
            //firebase - get user friend document ref
            if(!this@ChatActivity::userFriendFirebaseDocumentRef.isInitialized){
                userFriendFirebaseDocumentRef = myFirebase.getUserFriendDocumentRef(userFriend.id)
            }
            //viewModel
            vm.loadChatList(this@ChatActivity,userFriend.id,binding)
        }else {
            //userFriend is not a user of app
            isFriendIsUserOfApp = false
            binding.activityChatAlertLayout.visibility = View.VISIBLE
            binding.activityChatAlertText.text = "User not available"
        }
    }
    private fun sendMessage(){
        if(isFriendIsUserOfApp){
            //send message
            val message = Message(UUID.randomUUID().toString(),
                    binding.activityChatEditText.text.toString(),
                    myFirebase?.uid!!,
                    userFriend.id,
                    MessagePhoto("",""),
                    ChatOwner.USER,
                    Calendar.stamp(),
                    Calendar.time(),
                    Calendar.todayDate(),
                    user.imageUrl,
                    false
            )
            //temp
            vm.messageList.add(message)
            vm.refreshAdapter(vm.messageList)
            //ui
            binding.activityChatEditText.setText("")
            binding.activityChatRv.scrollToPosition(vm.messageList.size-1)

            Job.BACK {
                it.launch {
                    myFirebase.storeMessage(message.copy(),userFriend.copy())
                }
                it.launch {
                    myFirebase.sendMessage(message.copy(),userFriendFirebaseDocumentRef,user,userFriend.copy())
                }
            }
        }else {
            Toast.show(this@ChatActivity,"Message Blocked")
        }
    }
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.activity_contacts_toolbar_menu,menu)
        return super.onCreateOptionsMenu(menu)
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.activity_contacts_toolbar_menu_refresh-> refresh()
        }
        return super.onOptionsItemSelected(item)
    }

    private infix fun View.onClick(OnClick : (View)->Unit){
        setOnClickListener {
            OnClick(it)
        }
    }

    @Subscribe(threadMode=ThreadMode.MAIN,sticky=true)
    fun onDataReceived(chatActivityEventBus: ChatActivityEventBus){
        userFriend = chatActivityEventBus.userFriend
        //load data
        loadData()
        //remove sticky event
        EventBus.getDefault().removeStickyEvent(ChatActivityEventBus::class.java)
    }
    override fun onStart() {
        super.onStart()
        EventBus.getDefault().register(this@ChatActivity)
    }
    override fun onStop() {
        super.onStop()
        EventBus.getDefault().unregister(this@ChatActivity)
    }
}