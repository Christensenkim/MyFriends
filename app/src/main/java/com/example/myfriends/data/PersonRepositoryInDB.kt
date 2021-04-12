package com.example.myfriends.data

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.room.Room
import com.example.myfriends.models.BEPerson
import java.lang.IllegalStateException
import java.util.concurrent.Executors

class PersonRepositoryInDB private constructor(private val context: Context){
    private val database: PersonDB = Room.databaseBuilder(context.applicationContext, PersonDB::class.java, "person-DB").build()
    private val personDAO = database.personDAO()

    private val executor = Executors.newSingleThreadExecutor()

    fun getAll(): LiveData<List<BEPerson>> = personDAO.getAll()

    fun getById(id: Int) = personDAO.getById(id)

    fun insert(p: BEPerson) {
        executor.execute { personDAO.insert(p) }
    }

    fun update(p: BEPerson) {
        executor.execute { personDAO.update(p) }
    }

    fun delete(p: BEPerson) {
        executor.execute { personDAO.delete(p) }
    }

    companion object {
        private var Instance: PersonRepositoryInDB? = null

        fun initialize(context: Context) {
            if (Instance == null)
                Instance = PersonRepositoryInDB(context)
        }

        fun get(): PersonRepositoryInDB {
            if (Instance != null) return Instance!!
            throw IllegalStateException("Person Repo. not initialized")
        }
    }
}