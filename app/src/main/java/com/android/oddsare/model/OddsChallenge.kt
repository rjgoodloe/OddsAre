package com.android.oddsare.model

import com.google.firebase.database.Exclude
import java.io.Serializable

class OddsChallenge() : Serializable {

    var user: String = ""
    var info: String = ""
    var numberOutOf: Int = 0
    var myNumber: Int = 0
    var theirNumber: Int = 0


    constructor(

        user: String,
        info: String,
        numberOutOf: Int,
        myNumber: Int,
        theirNumber: Int


    ) : this() {
        this.user = user
        this.info = info
        this.numberOutOf = numberOutOf
        this.myNumber = myNumber
        this.theirNumber = theirNumber

    }


    // [START stat_to_map]
    @Exclude
    fun toMap(): Map<String, Any?> {
        return mapOf(
            "user" to user,
            "info" to info,
            "numberOutOf" to numberOutOf,
            "myNumber" to myNumber,
            "theirNumber" to theirNumber

        )
    }
}
