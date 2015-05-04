package iiitd.ac.in.dsys.meetup.Database;

import android.provider.BaseColumns;

/**
 * Created by vedantdasswain on 04/05/15.
 */
public class DatabaseContract {
    // To prevent someone from accidentally instantiating the contract class,
    // give it an empty constructor.
    public DatabaseContract() {
    }

    private static final String TEXT_TYPE = " TEXT";
    private static final String INT_TYPE = " INTEGER";
    private static final String REAL_TYPE = " REAL";
    private static final String COMMA_SEP = ",";

    /* Inner class that defines the table contents */
    public static abstract class LocationEntry implements BaseColumns {
        public static final String TABLE_NAME = "Location";
        public static final String TIME = "Time";
        public static final String LATITUDE = "Latitude";
        public static final String LONGITUDE="Longitude";
        public static final String USERNAME="User_Name";
        public static final String MEETUPNAME="Meetup_Name";

        public static final String SQL_CREATE_ENTRIES =
                "CREATE TABLE " + LocationEntry.TABLE_NAME + " (" +
                        LocationEntry._ID + " INTEGER PRIMARY KEY," +
                        LocationEntry.TIME + REAL_TYPE + COMMA_SEP +
                        LocationEntry.LATITUDE + REAL_TYPE + COMMA_SEP +
                        LocationEntry.LONGITUDE + REAL_TYPE + COMMA_SEP +
                        LocationEntry.USERNAME + TEXT_TYPE + COMMA_SEP +
                        LocationEntry.MEETUPNAME +TEXT_TYPE +
                        " )";
        public static final String SQL_DELETE_ENTRIES =
                "DROP TABLE IF EXISTS " + LocationEntry.TABLE_NAME;

    }
}
