package com.mu.jan.sparkchat.data.model.chat

class MessagePhoto{
    lateinit var id: String
    lateinit var url: String

    constructor(){}
    constructor(id: String, url: String) {
        this.id = id
        this.url = url
    }


}