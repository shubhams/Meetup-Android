package iiitd.ac.in.dsys.meetup.LocationListener;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;

/**
 * Created by Shubham on 02 May 15.
 */
public class MyLocationListener implements LocationListener
{

    Location currentLocation;
    Context context;
    private static String TAG;

    public MyLocationListener(Context cnt)
    {
        TAG = this.getClass().getSimpleName();
        this.context=cnt;
    }

    @Override
    public void onLocationChanged(Location location) {
        currentLocation = location;
//        Toast.makeText(getApplicationContext(), String.valueOf(currentLocation.getLatitude() + " " + currentLocation.getLongitude()), Toast.LENGTH_LONG).show();
//        sendHeartBeat(location);
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }
}
