package com.mu.jan.sparkchat

import android.app.Application
import com.mu.jan.sparkchat.data.database.MyFirebase
import com.mu.jan.sparkchat.data.model.user.User
import com.mu.jan.sparkchat.helper.auth.Auth
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class SparkChat : Application() {
    lateinit var user : User
    companion object{
        lateinit var instance : SparkChat
        fun get() : SparkChat{
            return instance
        }
    }
    override fun onCreate() {
        super.onCreate()
        instance = this
        loadUserInfo()
    }
    fun loadUserInfo(){
        //load user info from database
        if(Auth.isUserSigned()) {
            MyFirebase.get().getUserInfo {
                if(it!=null) user = it
            }
        }
    }


}