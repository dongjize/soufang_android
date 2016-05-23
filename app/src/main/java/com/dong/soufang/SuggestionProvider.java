package com.dong.soufang;

import android.content.SearchRecentSuggestionsProvider;

/**
 * Description:
 * <p>
 * Author: dong
 * Date: 16/5/3
 */
public class SuggestionProvider extends SearchRecentSuggestionsProvider {
    public static final String AUTHORITY = SuggestionProvider.class.getSimpleName();
    public static final int MODE = SearchRecentSuggestionsProvider.DATABASE_MODE_2LINES
            | SearchRecentSuggestionsProvider.DATABASE_MODE_QUERIES;

    public SuggestionProvider() {
        super();
        setupSuggestions(AUTHORITY, MODE);
    }

}
