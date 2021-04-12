package com.example.myfriends.data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.myfriends.models.BEPerson

@Database(entities = [BEPerson::class], version = 1)
abstract class PersonDB: RoomDatabase() {

    abstract fun personDAO(): PersonDAO
}