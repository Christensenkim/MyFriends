package com.example.myfriends.Model

import java.io.Serializable
import java.util.*

class BEFriend(
    var id: Int,
    var name: String,
    var address: String,
    var phone: String,
    var mailAddress: String,
    var website: String,
    var birthday: String,
    var picture: String
) : Serializable {
}