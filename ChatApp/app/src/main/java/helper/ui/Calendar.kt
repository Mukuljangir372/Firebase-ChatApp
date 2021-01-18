package com.mu.jan.sparkchat.helper.ui

import java.text.SimpleDateFormat
import java.util.*
import java.util.Calendar

object Calendar {
    fun todayDate() : String{
        val date = Calendar.getInstance().time
        //format
        val simpleDateFormat = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())
        return simpleDateFormat.format(date)
    }
    fun stamp() : String{
        val timeStamp : Long = System.currentTimeMillis()/1000
        return timeStamp.toString()
    }
    fun time() : String{
        val time = Calendar.getInstance().time
        val simpleDateFormat = SimpleDateFormat("hh:mm aaa", Locale.getDefault())
        return simpleDateFormat.format(time)
    }
}