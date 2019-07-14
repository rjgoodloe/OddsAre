package com.android.oddsare.activity

import android.app.SearchManager
import android.content.Intent
import android.content.SearchRecentSuggestionsProvider
import android.os.Bundle
import android.provider.SearchRecentSuggestions
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import provider.RecentSearchProvider

class SearchableActivity : AppCompatActivity(){


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        handleIntent(intent)
    }



    override fun onNewIntent(intent: Intent) {
        setIntent(intent)
        handleIntent(intent)
    }

    private fun handleIntent(intent: Intent) {
        if (Intent.ACTION_SEARCH == intent.action) {
            intent.getStringExtra(SearchManager.QUERY)?.also { query ->
                SearchRecentSuggestions(this, RecentSearchProvider.AUTHORITY, RecentSearchProvider.MODE)
                    .saveRecentQuery(query, null)
            Log.d(TAG, query)}
        }
    }

//    private fun clearHistory(){
//        SearchRecentSuggestions(this, RecentSearchProvider.AUTHORITY, RecentSearchProvider.MODE)
//            .clearHistory()
//    }

    companion object {
        private const val TAG = "SearchableActivity"
    }
}