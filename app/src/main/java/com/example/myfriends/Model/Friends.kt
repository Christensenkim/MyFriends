package com.example.myfriends.Model

object Friends {

    var mFriends: MutableList<BEFriend> = arrayListOf<BEFriend>(
    )

    fun getAll(): Array<BEFriend> = mFriends.toTypedArray()

    fun addFriend(friend: BEFriend){
        mFriends.add(friend)
    }

    fun getAllNames(): Array<String>  =  mFriends.map { aFriend -> aFriend.name }.toTypedArray()



}