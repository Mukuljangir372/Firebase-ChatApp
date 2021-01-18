package com.mu.jan.sparkchat.helper.auth

import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import com.mu.jan.sparkchat.ui.activities.LoginActivity
import java.util.concurrent.TimeUnit

object PhoneAuth {
    fun startPhoneAuth(activity : LoginActivity,callbacks : PhoneAuthProvider.OnVerificationStateChangedCallbacks,phoneNum : String){
        //send OTP
        PhoneAuthProvider.verifyPhoneNumber(PhoneAuthOptions.newBuilder()
                .setPhoneNumber(phoneNum)
                .setTimeout(2, TimeUnit.MINUTES)
                .setActivity(activity)
                .setCallbacks(callbacks)
                .build()
        )
    }
}