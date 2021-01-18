package com.mu.jan.sparkchat.ui.activities

import android.Manifest
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.mu.jan.sparkchat.R
import com.mu.jan.sparkchat.data.viewModel.ContactsActivityViewModel
import com.mu.jan.sparkchat.databinding.ActivityContactsBinding
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class ContactsActivity : AppCompatActivity() {

    private lateinit var binding : ActivityContactsBinding
    //viewModel
    private lateinit var vm : ContactsActivityViewModel
    //const
    private val readContactsPermissionRequest : Int = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityContactsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        //toolbar
        setSupportActionBar(binding.activityContactsToolbar)
        binding.activityContactsToolbar.setNavigationOnClickListener { onBackPressed() }
        //handle permission and load data
        handlePermissionAndLoadData()

    }

    //menu
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.activity_contacts_toolbar_menu,menu)
        return super.onCreateOptionsMenu(menu)
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.activity_contacts_toolbar_menu_refresh-> refresh()
        }
        return super.onOptionsItemSelected(item)
    }
    //permission
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when(requestCode){
            readContactsPermissionRequest->{
                if(grantResults[0]==PackageManager.PERMISSION_GRANTED){
                    loadData()
                }
            }
        }
    }
    private fun handlePermissionAndLoadData(){
        //READ_CONTACTS PERMISSION
        if(ContextCompat.checkSelfPermission(this,Manifest.permission.READ_CONTACTS)!=PackageManager.PERMISSION_GRANTED){
            //ask for permission
            ActivityCompat.requestPermissions(this@ContactsActivity,arrayOf(Manifest.permission.READ_CONTACTS),readContactsPermissionRequest)
        }else {
            //already granted
            loadData()
        }
    }
    //data
    private fun refresh(){}
    private fun loadData(){
        if(!this::vm.isInitialized) {
            vm = ViewModelProvider(this).get(ContactsActivityViewModel::class.java)
        }
        //viewModel
        vm.loadList(this@ContactsActivity,this,binding,contentResolver)
    }

}