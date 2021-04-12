package com.example.myfriends

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.ContextMenu
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ArrayAdapter
import android.widget.ListAdapter
import android.widget.ListView
import android.widget.Toast
import com.example.myfriends.Model.Friends

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val friends = Friends

        val friendNames = friends.getAllNames()

        val adapter: ListAdapter = ArrayAdapter(
                this,
                android.R.layout.simple_list_item_1, friendNames
        )

        val listView: ListView = findViewById(R.id.listOfFriends)

        listView.adapter = adapter

        listView.setOnItemClickListener { _, _, position, _ -> onListItemClick(position) }
    }

    fun onListItemClick (position: Int) {
        val intent = Intent(this, DetailsActivity::class.java)
        val friend = Friends.getAll()[position]
        intent.putExtra("friend", friend)
        startActivity(intent)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.addfriendbutton, menu);
        return true;
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        val id: Int = item.getItemId()

        when (id) {
            R.id.action_favorite -> {
                val i = Intent(this, DetailsActivity::class.java)
                startActivity(i)
            }
        }
        return super.onOptionsItemSelected(item)
    }
}