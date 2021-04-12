package com.example.myfriends.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity
data class BEPerson(
    @PrimaryKey(autoGenerate = true)    var id: Int,
                                        var name: String,
                                        var address: String,
                                        var phone: String,
                                        var mailAddress: String,
                                        var website: String,
                                        var birthday: String,
                                        var picture: String
): Serializable {
}
