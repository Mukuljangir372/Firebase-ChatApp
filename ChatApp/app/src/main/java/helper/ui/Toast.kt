package com.mu.jan.sparkchat.helper.ui

import android.content.Context
import android.widget.Toast

object Toast {
    fun show(context : Context,text : String){
        Toast.makeText(context,text,Toast.LENGTH_SHORT).show()
    }
}