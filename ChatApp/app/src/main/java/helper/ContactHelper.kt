package com.mu.jan.sparkchat.helper

import android.content.ContentResolver
import android.provider.ContactsContract
import com.mu.jan.sparkchat.data.model.user.Contact
import com.mu.jan.sparkchat.helper.ui.Job

object ContactHelper {
    /**
     * ContentProvider helps you to query content or query content over SQLite
     * ContentResolver provides you URI from ContentProvider
     */
    fun getContactList(contentResolver: ContentResolver,onComplete:(MutableList<Contact>)->Unit){
        Job.BACK {
            val list = mutableListOf<Contact>()
            val cursor = contentResolver.query(ContactsContract.Contacts.CONTENT_URI
            ,null,null,null,null,null)
            if(cursor!!.count>0){
                while(cursor!!.moveToNext()){
                    //contact id helps you to query phoneNum
                    val id = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID))
                    //query phoneNum from contactId
                    if((cursor.getInt(cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER)))>0){
                        //user has phoneNum
                        val pCursor = contentResolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,null,
                            ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
                            arrayOf(id),null
                            )
                        if(pCursor!!.count>0){
                            while (pCursor.moveToNext()){
                                val phoneNum = pCursor.getString(pCursor
                                    .getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER))
                                val name = pCursor.getString(pCursor
                                        .getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME))
                                //remove space between phone num
                                list.add(Contact(name,phoneNum.replace("\\s".toRegex(),"")))
                            }
                            pCursor.close()
                        }
                   }

                }
                onComplete(list)
                cursor.close()
            }
        }
    }

}