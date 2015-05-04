package iiitd.ac.in.dsys.meetup.LocationListener;

import android.content.Context;
import android.location.Location;
import android.util.Log;
import android.widget.Toast;

import com.appspot.intense_terra_821.data_api.DataApi;
import com.appspot.intense_terra_821.data_api.model.ApiCustomMessagesMeetupListMessage;
import com.appspot.intense_terra_821.data_api.model.ApiCustomMessagesMeetupLocationsUpdateFullMessage;
import com.appspot.intense_terra_821.data_api.model.ApiCustomMessagesMeetupMessage;
import com.appspot.intense_terra_821.data_api.model.ApiCustomMessagesUpLocationMessage;
import com.google.android.gms.location.LocationListener;

import java.util.ArrayList;

import iiitd.ac.in.dsys.meetup.CommonUtils;
import iiitd.ac.in.dsys.meetup.ObjectClasses.MeetupObject;
import iiitd.ac.in.dsys.meetup.TaskCompleteInterfaces.OnGetMeetupsTaskCompleted;
import iiitd.ac.in.dsys.meetup.TaskCompleteInterfaces.OnHeartBeatCompleted;
import iiitd.ac.in.dsys.meetup.messages.getMeetupsTask;

/**
 * Created by Shubham on 02 May 15.
 */
public class MyLocationListener implements LocationListener, OnHeartBeatCompleted, OnGetMeetupsTaskCompleted
{

    private Location currentLocation;
    private Context context;
    private static String TAG;
    private ApiCustomMessagesUpLocationMessage upLocationMessage;
    DataApi dataApiInst;
    private ArrayList<MeetupObject> meetupObjectsList;

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

    private void getMeetups() {
        Log.v(TAG,"Start fetching meetups...");
        dataApiInst= CommonUtils.getDataApiInst();
        (new getMeetupsTask(context, dataApiInst,this)).execute();
    }

    @Override
    public void onTaskCompleted(ApiCustomMessagesMeetupListMessage meetupsList,Boolean accepted) {
        if(meetupsList!=null) {
            if(meetupsList.getSuccess()!=null)
                Log.v(TAG, "Success message: " + meetupsList.getSuccess().getStrValue());
            if (!meetupsList.isEmpty() && meetupsList.getMeetups() != null) {
                for (ApiCustomMessagesMeetupMessage meetup : meetupsList.getMeetups()) {
                    MeetupObject mo = new MeetupObject(meetup.getName(), meetup.getOwner(), meetup.getActive(),accepted);
                    if(!meetupObjectsList.contains(mo))
                        meetupObjectsList.add(mo);
                }

                filterActiveMeetups();
            }

        }

    }

    void filterActiveMeetups(){
        for(MeetupObject mo:meetupObjectsList){
            if (!mo.getActive())
                meetupObjectsList.remove(mo);
        }
    }
}
