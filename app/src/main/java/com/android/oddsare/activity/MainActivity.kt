package com.android.oddsare.activity

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import com.android.oddsare.R
import com.android.oddsare.fragment.HomeFragment
import com.android.oddsare.fragment.NewOddsFragment
import com.android.oddsare.fragment.NotificationsFragment
import com.android.oddsare.fragment.ProfileFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.auth.FirebaseAuth
import android.content.ComponentName


class MainActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private var mFirebaseAnalytics: FirebaseAnalytics? = null
    private val fm = this.supportFragmentManager

    private val onNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        var selectedFragment: Fragment = HomeFragment(this)
        when (item.itemId) {
            R.id.navigation_home -> selectedFragment = HomeFragment(this)
            R.id.navigation_new_odds -> selectedFragment = NewOddsFragment(this)
            R.id.navigation_notifications -> selectedFragment = NotificationsFragment(this)
            R.id.navigation_profile -> selectedFragment = ProfileFragment(this)
        }
        fm.beginTransaction()
            .addToBackStack(null)
            .replace(R.id.frag_placeholder, selectedFragment)
            .commit()
        return@OnNavigationItemSelectedListener true
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        auth = FirebaseAuth.getInstance()
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this)

        fm.beginTransaction()
            .addToBackStack(null)
            .replace(R.id.frag_placeholder, HomeFragment(this))
            .commit()

        val navView: BottomNavigationView = findViewById(R.id.nav_view)
        navView.setOnNavigationItemSelectedListener(onNavigationItemSelectedListener)
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
        //searchView.setQueryHint(resources.getString(R.string.search_hint))
        return true

//        searchView.setOnQueryTextListener()
//        // Get the SearchView and set the searchable configuration
//        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
//        (menu.findItem(R.id.action_search).actionView as SearchView).apply {
//            // Assumes current activity is the searchable activity
//            setSearchableInfo(searchManager.getSearchableInfo(componentName))
//            setIconifiedByDefault(false) // Do not iconify the widget; expand it by default
//            isQueryRefinementEnabled
//            isSubmitButtonEnabled

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
}

