package com.mu.jan.sparkchat.ui.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider
import com.mu.jan.sparkchat.SparkChat
import com.mu.jan.sparkchat.data.database.MyFirebase
import com.mu.jan.sparkchat.databinding.ActivityLoginBinding
import com.mu.jan.sparkchat.helper.ui.Toast
import com.mu.jan.sparkchat.helper.auth.PhoneAuth
import com.mu.jan.sparkchat.helper.ui.Job
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

class LoginActivity : AppCompatActivity() {

    //viewBinding
    private lateinit var binding : ActivityLoginBinding
    //firebaseAuth
    private lateinit var auth : FirebaseAuth
    //PhoneAuthProvider callbacks
    private lateinit var callbacks : PhoneAuthProvider.OnVerificationStateChangedCallbacks
    //credential as OTP
    private lateinit var authCredential : String

    private var isOtpSent : Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //firebaseAuth
        auth = FirebaseAuth.getInstance()
        //attach callbacks
        callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            override fun onVerificationCompleted(p0: PhoneAuthCredential) {
                //success
                //Sign in with Firebase
                auth.signInWithCredential(p0).addOnCompleteListener {
                    if(it.isSuccessful){
                        //save user info
                        MyFirebase.get().saveUserInfo(){
                            //load user Info
                            SparkChat.get().loadUserInfo()
                            finish()
                            //startActivity
                            startActivity(Intent(this@LoginActivity,MainActivity::class.java))
                        }
                    }else {
                        Toast.show(this@LoginActivity,"failed to verify")
                    }
                }.addOnFailureListener {
                    Toast.show(this@LoginActivity,it.localizedMessage)
                }

            }
            override fun onVerificationFailed(p0: FirebaseException) {
                isOtpSent = false
                //enable button and change text
                enableButton()
                binding.activityLoginBtn.text = "Get OTP"
                //show error
                Toast.show(this@LoginActivity,p0.localizedMessage)
            }
            override fun onCodeSent(p0: String, p1: PhoneAuthProvider.ForceResendingToken) {
                super.onCodeSent(p0, p1)
                //code has been sent
                authCredential = p0
                isOtpSent = true
                //visible
                binding.activityLoginOtpLayout.visibility = View.VISIBLE
                //enable button and change text
                enableButton()
                binding.activityLoginBtn.text = "Done"

            }
        }
        //otp editText
        binding.activityLoginCountryCode.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                if(binding.activityLoginCountryCode.text.toString().trim().isEmpty()){
                    binding.activityLoginCountryCode.setText("+")
                    binding.activityLoginCountryCode.setSelection(1)
                }
            }
        })
        //login button
        binding.activityLoginBtn.setOnClickListener { handleAuth() }

    }
    private fun verifyOtp(){
        if(binding.activityLoginOtp.text.toString().trim().isNotEmpty() && this::authCredential.isInitialized){
            val userOtp = binding.activityLoginOtp.text.toString().trim()
            val credentials = PhoneAuthProvider.getCredential(authCredential,userOtp)
            //sign in with firebase
            auth.signInWithCredential(credentials).addOnCompleteListener {
                if(it.isSuccessful){
                    //save user info
                    MyFirebase.get().saveUserInfo(){
                        //load user Info
                        SparkChat.get().loadUserInfo()
                        finish()
                        //startActivity
                        startActivity(Intent(this@LoginActivity,MainActivity::class.java))
                    }
              }else {
                    Toast.show(this@LoginActivity,"failed to sign in")
                }
            }.addOnFailureListener {
                Toast.show(this@LoginActivity,it.localizedMessage)
            }
        }else {
            Toast.show(this@LoginActivity,"Fill Otp")
        }

    }
    private fun handleAuth(){
        if(binding.activityLoginPhoneNumber.text.toString().trim().isNotEmpty() &&
                binding.activityLoginCountryCode.text.toString().trim().isNotEmpty()) {
            if(isOtpSent){
                //verify OTP
                verifyOtp()
            }else {
                //send OTP
                isOtpSent = true
                val num = "${binding.activityLoginCountryCode.text.toString().trim()}${
                    binding.activityLoginPhoneNumber.text.toString().trim()}"
                //disable button for a while
                disableButton()
                //otp
                PhoneAuth.startPhoneAuth(this@LoginActivity,callbacks,num)
            }
        }else {
            Toast.show(this@LoginActivity,"Fill Phone number and Country code")
        }
    }
    private fun disableButton(){
        binding.activityLoginBtn.isEnabled = false
    }
    private fun enableButton(){
        binding.activityLoginBtn.isEnabled = true
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finishAffinity()
    }
}