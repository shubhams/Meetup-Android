package iiitd.ac.in.dsys.meetup.LocationListener;

import android.content.Context;
import android.location.Location;
import android.util.Log;
import android.widget.Toast;
import com.appspot.intense_terra_821.data_api.model.ApiCustomMessagesMeetupLocationsUpdateFullMessage;
import com.appspot.intense_terra_821.data_api.model.ApiCustomMessagesUpLocationMessage;
import com.google.android.gms.location.LocationListener;
import iiitd.ac.in.dsys.meetup.TaskCompleteInterfaces.OnHeartBeatCompleted;

/**
 * Created by Shubham on 02 May 15.
 */
public class MyLocationListener implements LocationListener, OnHeartBeatCompleted
{

    private Location currentLocation;
    private Context context;
    private static String TAG;
    private ApiCustomMessagesUpLocationMessage upLocationMessage;

    public MyLocationListener(Context cnt)
    {
        TAG = this.getClass().getSimpleName();
        this.context=cnt;
    }

    @Override
    public void onLocationChanged(Location location) {
        if(location!=null)
        {
            currentLocation = location;
            Toast.makeText(context, String.valueOf(currentLocation.getLatitude() + " " + currentLocation.getLongitude()), Toast.LENGTH_LONG).show();
//            sendHeartBeat(currentLocation);
//
        }
        else
            Log.d(TAG, "location object found as null");
    }

    @Override
    public void onHeatBeatReceived(ApiCustomMessagesMeetupLocationsUpdateFullMessage fullMessage) {

    }
}
