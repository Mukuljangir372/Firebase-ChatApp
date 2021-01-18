package com.mu.jan.sparkchat.helper.auth

import com.google.firebase.auth.FirebaseAuth

object Auth {
    fun isUserSigned() : Boolean{
        return FirebaseAuth.getInstance().currentUser!=null
    }
    fun logOut(){
        FirebaseAuth.getInstance().signOut()
    }
}