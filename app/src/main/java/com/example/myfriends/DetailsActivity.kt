package com.example.myfriends

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.CheckBox
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity

class DetailsActivity : AppCompatActivity(){

    val savedPerson: ArrayList<String> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_details)
    }

    fun saveNewFriend(view: View) {
        var name = findViewById<EditText>(R.id.personName).toString()
        var phone = findViewById<EditText>(R.id.phoneNumber).toString()
        var isFavorite = findViewById<CheckBox>(R.id.isFavorite).toString()

        val person = name + phone + isFavorite

        savedPerson.add(person)

        val i = Intent(this, MainActivity::class.java)
        i.putStringArrayListExtra("List", savedPerson)
        startActivity(i)
    }
}