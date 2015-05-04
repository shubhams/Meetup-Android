package iiitd.ac.in.dsys.meetup.Services;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import iiitd.ac.in.dsys.meetup.LocationListener.MyLocationListener;

/**
 * Created by Shubham on 02 May 15.
 */
public class HeartBeatService extends Service implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener
{

    private static String TAG;
    private MyLocationListener locationListener;
    private LocationManager locationManager;
    private GoogleApiClient mGAC;
    private LocationRequest lr1;

    @Override
    public void onCreate() {
        super.onCreate();
        TAG = this.getClass().getSimpleName();
        Log.d(TAG, "service created");
        locationListener = new MyLocationListener(this);
        mGAC = new GoogleApiClient.Builder(this).addApi(LocationServices.API).addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this).build();
        mGAC.connect();
        createLocationRequest();
    }

    private void createLocationRequest() {
        lr1 = new LocationRequest();
        lr1.setInterval(30000);
        lr1.setFastestInterval(30000);
        lr1.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        Log.d(TAG, "service started");
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        return 1;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG,"service destroyed");
        LocationServices.FusedLocationApi.removeLocationUpdates(mGAC, locationListener);

    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onConnected(Bundle bundle) {
        Log.d(TAG,"service got connected");
        LocationServices.FusedLocationApi.requestLocationUpdates(mGAC, lr1, locationListener);
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }
}
