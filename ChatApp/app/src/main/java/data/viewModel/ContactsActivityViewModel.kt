package com.mu.jan.sparkchat.data.viewModel

import android.content.ContentResolver
import android.content.Context
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import com.mu.jan.sparkchat.data.model.chat.UserFriend
import com.mu.jan.sparkchat.data.repository.ContactsActivityRepository
import com.mu.jan.sparkchat.databinding.ActivityContactsBinding
import com.mu.jan.sparkchat.ui.adapters.ContactsActivityRvAdapter

class ContactsActivityViewModel
constructor() : ViewModel()
{
    private lateinit var context: Context
    private lateinit var binding : ActivityContactsBinding
    private lateinit var contentResolver: ContentResolver

    fun loadList(context : Context,lifecycleOwner: LifecycleOwner, binding : ActivityContactsBinding,contentResolver: ContentResolver){
        this.context = context
        this.binding = binding
        this.contentResolver = contentResolver
        //get available user list
        ContactsActivityRepository.getAvailableUserList(contentResolver){
            val mList = it
            //convert list into liveData
            val mLiveData = MutableLiveData<MutableList<UserFriend>>()
            mLiveData.value = mList

            mLiveData.observe(lifecycleOwner, Observer {
                loadRvAdapter(it)
            })
        }
    }
    private fun loadRvAdapter(list : MutableList<UserFriend>){
        binding.activityContactsRvAdapter = ContactsActivityRvAdapter(context,list)
    }
}