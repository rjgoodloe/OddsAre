package com.android.oddsare.activity

import android.app.SearchManager
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import com.android.oddsare.R
import com.android.oddsare.fragment.HomeFragment
import com.android.oddsare.fragment.NewOddsFragment
import com.android.oddsare.fragment.NotificationsFragment
import com.android.oddsare.fragment.ProfileFragment
import com.android.oddsare.generic.Notifications
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*


class MainActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var database: DatabaseReference
    private var mFirebaseAnalytics: FirebaseAnalytics? = null
    private val fm = this.supportFragmentManager


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        auth = FirebaseAuth.getInstance()
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this)
        database = FirebaseDatabase.getInstance().reference


        fm.beginTransaction()
            .addToBackStack(null)
            .replace(R.id.frag_placeholder, HomeFragment())
            .commit()

        requestListener()

        val navView: BottomNavigationView = findViewById(R.id.nav_view)
        navView.setOnNavigationItemSelectedListener(onNavigationItemSelectedListener)
    }

    private val onNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        var selectedFragment: Fragment = HomeFragment()
        when (item.itemId) {
            R.id.navigation_home -> selectedFragment = HomeFragment()
            R.id.navigation_new_odds -> selectedFragment = NewOddsFragment()
            R.id.navigation_notifications -> selectedFragment = NotificationsFragment()
            R.id.navigation_profile -> selectedFragment = ProfileFragment()
        }
        fm.beginTransaction()
            .addToBackStack(null)
            .replace(R.id.frag_placeholder, selectedFragment)
            .commit()
        return@OnNavigationItemSelectedListener true
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.top_bar_menu, menu)

        val searchView = (menu.findItem(R.id.action_search).actionView as SearchView)

        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        searchView.setSearchableInfo(
            searchManager.getSearchableInfo(
                ComponentName(
                    this,
                    SearchableActivity::class.java
                )
            )
        )
        return true
    }

    override fun onSearchRequested(): Boolean {
        startSearch(null, false, null, false)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_sign_out -> {
                auth.signOut()
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
                finish()
            }

        }
        return super.onOptionsItemSelected(item)
    }

    var number = 0
    private fun requestListener() {
        database.child("Users").child(splitString(auth.currentUser!!.email!!)).child("Requests")
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {

                    try {
                        val requests = dataSnapshot.value as HashMap<*, *>
                        var value: String
                        for (key in requests.keys) {
                            value = requests[key] as String
                            Toast.makeText(
                                this@MainActivity, value,
                                Toast.LENGTH_SHORT
                            ).show()

                            val notifyUser = Notifications()
                            Log.d(TAG, "HERE")
                            notifyUser.notify(applicationContext, "sent by $value", number)
                            number++
                        }


                    } catch (t: Throwable) {

                    }
//                    val userInfo = dataSnapshot.getValue(User::class.java)
//                    userInfo!!.getRequests()
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    // Getting Post failed, log a message
                    Log.w(TAG, "loadPost:onCancelled", databaseError.toException())
                    // ...
                }
            })
    }

    private fun splitString(str: String): String {
        val split = str.split("@")
        return split[0]
    }

    companion object {
        const val TAG = "MainActivity"
    }
}

