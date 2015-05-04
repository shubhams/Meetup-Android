package iiitd.ac.in.dsys.meetup.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import iiitd.ac.in.dsys.meetup.ObjectClasses.LocationObject;

/**
 * Created by vedantdasswain on 04/05/15.
 */
public class DbFunctions {

    public static String TAG = "DbFunctions";

    public static long insert(Context context, LocationObject lo) {
        // Gets the data repository in write mode
        DbHelper mDbHelper = new DbHelper(context);
        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        // Create a new map of values, where column names are the keys
        ContentValues values = new ContentValues();
        values.put(DatabaseContract.LocationEntry.TIME, lo.getTime());
        values.put(DatabaseContract.LocationEntry.LATITUDE, lo.getLat());
        values.put(DatabaseContract.LocationEntry.LONGITUDE, lo.getLon());
        values.put(DatabaseContract.LocationEntry.USERNAME, lo.getUsername());
        values.put(DatabaseContract.LocationEntry.MEETUPNAME, lo.getMeetupname());
        
        long newRowId = db.insert(
                DatabaseContract.LocationEntry.TABLE_NAME,
                null,
                values);

        db.close();

        return newRowId;

    }

    public static List<LocationObject> read(Context context,String meetupName){
        List<LocationObject> locations=new CopyOnWriteArrayList<>();

        DbHelper mDbHelper = new DbHelper(context);
        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        String query = "SELECT * FROM " + DatabaseContract.LocationEntry.TABLE_NAME+
                " WHERE "+DatabaseContract.LocationEntry.MEETUPNAME+" = '"+meetupName+"'";

        Cursor c = db.rawQuery(
                query, null);

        if (c != null && c.moveToFirst()) {
            while (c.isAfterLast() == false) {
                LocationObject ao = new LocationObject(c.getLong(1), c.getDouble(2), c.getDouble(3),
                        c.getString(4), c.getString(5));
                locations.add(ao);
                c.moveToNext();
            }
        }
        db.close();

        return locations;
    }

}
