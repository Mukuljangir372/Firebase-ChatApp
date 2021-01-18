package com.mu.jan.sparkchat.data.model.user

class User {
    lateinit var id : String
    lateinit var name : String
    lateinit var phoneNumWithCountryCode : String
    lateinit var phoneNumWithoutCountryCode : String
    lateinit var imageUrl : String
    constructor(){}
    constructor(id: String, name: String, phoneNumWithCountryCode: String,phoneNumWithoutCountryCode : String, imageUrl: String) {
        this.id = id
        this.name = name
        this.phoneNumWithCountryCode = phoneNumWithCountryCode
        this.phoneNumWithoutCountryCode = phoneNumWithoutCountryCode
        this.imageUrl = imageUrl
    }
    fun toMap() : HashMap<String,Any>{
        return hashMapOf<String,Any>(
            "id" to id,
            "name" to name,
            "phoneNumWithCountryCode" to phoneNumWithCountryCode,
            "phoneNumWithoutCountryCode" to phoneNumWithoutCountryCode,
            "imageUrl" to imageUrl
        )
    }
}