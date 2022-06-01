package com.example.assessment.Util;
public class Constants {

    public static final class Tabs {
        /* Number of tabs */
        public static final int TABS_COUNT = 3;

        /* Position of tabs on the screen */
        public static final int MOST_EMAILED_TAB = 0;
        public static final int MOST_SHARED_TAB = 1;
        public static final int MOST_VIEWED_TAB = 2;
        public static final int FAVORITES_TAB = 3;

        /* Tab titles */
        public static final String MOST_EMAILED_TITLE = "Most Emailed";
        public static final String MOST_SHARED_TITLE = "Most Shared";
        public static final String MOST_VIEWED_TITLE = "Most Viewed";
    }

    public static final class Retrofit {
        public static final String NYTApiKey = "GA5Zls7pgTguDS3El609CfU44qMzAxnu";
        public static final String NYTBaseApiUrl = "https://api.nytimes.com/svc/mostpopular/v2/";
    }

    public static final class Queries {
        public static final String queryLabel = "queryLabel";
    }

    public static final class IntentArticleExtras {
        public static final String articleUrl = "articleUrl";
    }

    public static final class DB{
        public static final String localDBName = "localDB";
    }
}
