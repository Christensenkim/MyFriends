package com.example.myfriends.data

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.myfriends.models.BEPerson

@Dao
interface PersonDAO {

    @Query("SELECT * from BEPerson order by id")
    fun getAll(): LiveData<List<BEPerson>>

    @Query("Select * from BEPerson where id = (:id)")
    fun getById(id: Int): LiveData<BEPerson>

    @Insert
    fun insert(p: BEPerson)

    @Update
    fun update(p: BEPerson)

    @Delete
    fun delete(p: BEPerson)


}