package com.mu.jan.sparkchat.data.viewModel

import android.content.Context
import androidx.lifecycle.ViewModel
import com.mu.jan.sparkchat.data.repository.MainActivityRepository
import com.mu.jan.sparkchat.databinding.ActivityMainBinding
import com.mu.jan.sparkchat.ui.adapters.RvAdapter

class MainActivityViewModel
constructor() : ViewModel()
{
    fun loadUserChats(context : Context,binding : ActivityMainBinding){
        //Note: use live data
        MainActivityRepository.loadUserChats {
            binding.activityMainAdapter = RvAdapter(context,it)
        }

    }
}