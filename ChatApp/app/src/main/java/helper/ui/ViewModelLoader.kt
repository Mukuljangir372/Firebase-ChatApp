package com.mu.jan.sparkchat.helper.ui

import androidx.lifecycle.ViewModelProvider
import com.mu.jan.sparkchat.data.viewModel.ChatActivityViewModel
import com.mu.jan.sparkchat.ui.activities.ChatActivity

object ViewModelLoader {
    fun load(activity : ChatActivity) : ChatActivityViewModel{
        return ViewModelProvider(activity).get(ChatActivityViewModel::class.java)
    }
}