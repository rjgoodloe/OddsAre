package provider

import android.content.SearchRecentSuggestionsProvider

class RecentSearchProvider : SearchRecentSuggestionsProvider() {
    init {
        setupSuggestions(AUTHORITY, MODE)

    }


    companion object {
        const val AUTHORITY = "com.android.oddsare.provider.RecentSearchProvider"
        const val MODE: Int = DATABASE_MODE_QUERIES
    }
}