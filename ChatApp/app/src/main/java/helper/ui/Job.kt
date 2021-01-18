package com.mu.jan.sparkchat.helper.ui

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

object Job {
    fun MAIN(onLaunch : (CoroutineScope)->Unit){
        CoroutineScope(Dispatchers.Main).launch {
            onLaunch(this)
        }
    }
    fun BACK(onLaunch : (CoroutineScope)->Unit){
        CoroutineScope(Dispatchers.IO).launch {
            onLaunch(this)
        }
    }
}