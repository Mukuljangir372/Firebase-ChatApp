package com.mu.jan.sparkchat.ui.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.mu.jan.sparkchat.data.database.MyFirebase
import com.mu.jan.sparkchat.data.model.user.User
import com.mu.jan.sparkchat.data.viewModel.MainActivityViewModel
import com.mu.jan.sparkchat.databinding.ActivityMainBinding
import com.mu.jan.sparkchat.helper.auth.Auth
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    //binding
    private lateinit var binding : ActivityMainBinding
    //viewModel
    private lateinit var mainActivityViewModel : MainActivityViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        //isUserSigned
        if(!Auth.isUserSigned()) startActivity(Intent(this@MainActivity,LoginActivity::class.java))
        //viewModel and recyclerView
        mainActivityViewModel = ViewModelProvider(this).get(MainActivityViewModel::class.java)
        mainActivityViewModel.loadUserChats(this@MainActivity,binding)

        binding.activityMainChatsBtn.setOnClickListener {
            startActivity(Intent(this@MainActivity,ContactsActivity::class.java))
        }
    }
}