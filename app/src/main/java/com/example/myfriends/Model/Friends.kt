package com.example.myfriends.Model

object Friends {

    var mFriends: MutableList<BEFriend> = arrayListOf<BEFriend>(
        BEFriend("Jonas", "123", true),
        BEFriend("Anders", "1234", false),
        BEFriend("Nikolaj", "12345", true),
        BEFriend("Nadia", "12345678", true),
        BEFriend("Michael", "23456789", true),
        BEFriend("Kacper", "87654321", false),
        BEFriend("Rocio", "12121212", true),
        BEFriend("Alan", "12876543", false),
        BEFriend("Rado", "14755432", true),
        BEFriend("Cecilia", "21212121", true),
        BEFriend("Alan", "12876543", false),
        BEFriend("Christina", "12344321", false),
        BEFriend("Anne", "12876543", false),
        BEFriend("Kim", "12876543", true)
    )

    fun getAll(): Array<BEFriend> = mFriends.toTypedArray()

    fun addFriend(friend: BEFriend){
        mFriends.add(friend)
    }

    fun getAllNames(): Array<String>  =  mFriends.map { aFriend -> aFriend.name }.toTypedArray()



}