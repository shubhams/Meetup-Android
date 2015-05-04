package iiitd.ac.in.dsys.meetup.LocationListener;

import android.content.Context;
import android.location.Location;
import android.util.Log;
import android.widget.Toast;

import com.appspot.intense_terra_821.data_api.DataApi;
import com.appspot.intense_terra_821.data_api.model.ApiCustomMessagesLocationMessage;
import com.appspot.intense_terra_821.data_api.model.ApiCustomMessagesMeetupListMessage;
import com.appspot.intense_terra_821.data_api.model.ApiCustomMessagesMeetupLocationsUpdateFullMessage;
import com.appspot.intense_terra_821.data_api.model.ApiCustomMessagesMeetupMessage;
import com.appspot.intense_terra_821.data_api.model.ApiCustomMessagesPeepLocationsMessage;
import com.appspot.intense_terra_821.data_api.model.ApiCustomMessagesUpLocationMessage;
import com.google.android.gms.location.LocationListener;
import com.google.api.client.util.DateTime;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import iiitd.ac.in.dsys.meetup.CommonUtils;
import iiitd.ac.in.dsys.meetup.Database.DbFunctions;
import iiitd.ac.in.dsys.meetup.ObjectClasses.LocationObject;
import iiitd.ac.in.dsys.meetup.ObjectClasses.MeetupObject;
import iiitd.ac.in.dsys.meetup.TaskCompleteInterfaces.OnGetMeetupsTaskCompleted;
import iiitd.ac.in.dsys.meetup.TaskCompleteInterfaces.OnHeartBeatCompleted;
import iiitd.ac.in.dsys.meetup.messages.getMeetupsTask;
import iiitd.ac.in.dsys.meetup.messages.sendHeartBeatTask;

/**
 * Created by Shubham on 02 May 15.
 */
public class MyLocationListener implements LocationListener, OnHeartBeatCompleted, OnGetMeetupsTaskCompleted
{

    private Location currentLocation;
    private Context context;
    private static String TAG;
    private ApiCustomMessagesUpLocationMessage upLocationMessage;
    private DataApi dataApiInst;
    private List<MeetupObject> meetupObjectsList;

    public MyLocationListener(Context cnt)
    {
        TAG = this.getClass().getSimpleName();
        this.context=cnt;
        meetupObjectsList = new CopyOnWriteArrayList<>();
    }

    @Override
    public void onLocationChanged(Location location) {
        if(location!=null)
        {
            currentLocation = location;
            Toast.makeText(context, String.valueOf(currentLocation.getLatitude() + " " + currentLocation.getLongitude()), Toast.LENGTH_LONG).show();
            getMeetups();
//            sendHeartBeat(currentLocation);
//
        }
        else
            Log.d(TAG, "location object found as null");
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
                sendHeartBeats();
            }

        }

    }

    private void getMeetups() {
        Log.v(TAG,"Start fetching meetups...");
        dataApiInst= CommonUtils.getDataApiInst();
        if(dataApiInst==null)
            Log.d(TAG, "dataApiInst null");
        else
            (new getMeetupsTask(context, dataApiInst,this)).execute();
    }

    void filterActiveMeetups(){
        for(MeetupObject mo:meetupObjectsList){
            if (!mo.getActive() || !(mo.getAccepted()))
                meetupObjectsList.remove(mo);
        }
    }

    void sendHeartBeats()
    {
        for(MeetupObject mo:meetupObjectsList)
        {
            upLocationMessage = new ApiCustomMessagesUpLocationMessage();
            upLocationMessage.setMeetupOwner(mo.getOwner());
            upLocationMessage.setMeetupName(mo.getName());
            upLocationMessage.setDetails(false);    //set to True to get all location updates
            upLocationMessage.setLat(currentLocation.getLatitude());
            upLocationMessage.setLon(currentLocation.getLongitude());
            dataApiInst = CommonUtils.getDataApiInst();
            (new sendHeartBeatTask(context, dataApiInst, upLocationMessage, this)).execute();
        }
    }


    @Override
    public void onHeatBeatReceived(ApiCustomMessagesMeetupLocationsUpdateFullMessage fullMessage) {
        List<ApiCustomMessagesPeepLocationsMessage> userMeetupLocations = fullMessage.getUserMeetupLocations();
        String meetupName = fullMessage.getMeetupName();
        if (userMeetupLocations != null) {
            for (int i = 0; i < userMeetupLocations.size(); ++i) {
                Log.d(TAG, userMeetupLocations.get(i).toString());
                ApiCustomMessagesPeepLocationsMessage peepLocationsMessage = userMeetupLocations.get(i);
//                String userName = peepLocationsMessage.getName();
                String userEmail = peepLocationsMessage.getEmail();
                ApiCustomMessagesLocationMessage userLoc = peepLocationsMessage.getLatestLocation();
//                LatLng latLng = new LatLng(userLoc.getLat(), userLoc.getLon());
                DateTime timeString = userLoc.getTime();
                long timeLong = timeString.getValue();
                Log.d(TAG,"timeLong"+timeLong);
                //Comment the line below to see your location also, as a marker on the map
//                if (!peepLocationsMessage.getEmail().equals(mo.getOwner()))
//                    mMap.addMarker(new MarkerOptions().position(latLng).title(userName).snippet(userEmail));
                LocationObject locationObject = new LocationObject(timeLong,userLoc.getLat(),userLoc.getLon(),
                        userEmail,meetupName);
                DbFunctions.insert(context,locationObject);
            }
        }
    }
}
