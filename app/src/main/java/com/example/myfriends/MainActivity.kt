package com.example.myfriends

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.ArrayAdapter
import android.widget.ListAdapter
import android.widget.ListView
import androidx.lifecycle.Observer
import com.example.myfriends.data.PersonRepositoryInDB
import com.example.myfriends.data.observeOnce
import com.example.myfriends.models.BEPerson

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        PersonRepositoryInDB.initialize(this)
        //insertMockData()

        val listOfFriends: ListView = findViewById(R.id.listOfFriends)

        dataObserver(listOfFriends)

        listOfFriends.setOnItemClickListener { _, _, position, _ -> onListItemClick(position) }
    }

    private fun insertMockData() {
        val mRep = PersonRepositoryInDB.get()
        mRep.insert(BEPerson(0, "Bob", "somestreet 1", "123456", "not@chance.com", "Notyourbusiness.com", "2020-10-10", ""))
        mRep.insert(BEPerson(0, "Bub", "otherstreet 1", "123456", "nad@chance.com", "Mindyourownbusiness.com", "2020-09-10", ""))
        mRep.insert(BEPerson(0, "Bab", "laststreet 1", "123456", "no@chance.com", "Keepyournoseoutofmybusiness.com", "2020-08-10", ""))
    }
    var cache: List<BEPerson>? = null;

    private fun dataObserver(listOfFriends: ListView) {
        val mRep = PersonRepositoryInDB.get()
        val nameObserver = Observer<List<BEPerson>>{ persons ->
            cache = persons;
            val asStrings = persons.map { p -> "${p.id}: ${p.name}"}
            val adapter: ListAdapter = ArrayAdapter(
                    this,
                    android.R.layout.simple_list_item_1,
                    asStrings.toTypedArray()
            )
            listOfFriends.adapter = adapter
        }
        mRep.getAll().observe(this, nameObserver)
    }

    fun onListItemClick (position: Int) {
        val intent = Intent(this, DetailsActivity::class.java)
        val id = cache!![position].id
        val personObserver = Observer<BEPerson> { person ->
            if (person != null)
            {
                intent.putExtra("friend", person)
                startActivity(intent)
                //Toast.makeText(this, "you have clicked ${person.name} ", Toast.LENGTH_SHORT).show()
            }
        }
        val mRep = PersonRepositoryInDB.get()
        mRep.getById(id).observeOnce(this, personObserver)
        //intent.putExtra("friend", friend)
        //startActivity(intent)
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