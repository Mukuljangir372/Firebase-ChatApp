package com.mu.jan.sparkchat.ui.listeners

import com.mu.jan.sparkchat.data.model.chat.UserFriend

interface RvAdapterClickListener {
    fun onItemClick(userFriend : UserFriend?)
}