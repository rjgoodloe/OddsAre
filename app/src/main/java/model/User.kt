package model

import java.io.Serializable

class User() : Serializable {
    private var name : String = ""
    private var username: String = ""
    private var email : String = ""


    constructor(

        name:String,
        username:String,
        email:String

    ) : this(){
        this.name = name
        this.username = username
        this.email = email
    }

    fun getName() : String{
        return this.name
    }

    fun getUsername() : String{
        return this.username
    }

    fun getEmail() : String{
        return this.email
    }
}