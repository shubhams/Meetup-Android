package iiitd.ac.in.dsys.meetup.Activities;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.*;
import com.appspot.intense_terra_821.data_api.DataApi;
import com.appspot.intense_terra_821.data_api.model.*;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import iiitd.ac.in.dsys.meetup.CommonUtils;
import iiitd.ac.in.dsys.meetup.Database.DbFunctions;
import iiitd.ac.in.dsys.meetup.ObjectClasses.LocationObject;
import iiitd.ac.in.dsys.meetup.ObjectClasses.MeetupAlarmIntent;
import iiitd.ac.in.dsys.meetup.ObjectClasses.MeetupObject;
import iiitd.ac.in.dsys.meetup.R;
import iiitd.ac.in.dsys.meetup.Receivers.DeactivateAlarmReceiver;
import iiitd.ac.in.dsys.meetup.TaskCompleteInterfaces.*;
import iiitd.ac.in.dsys.meetup.messages.*;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.TimeZone;
import java.util.concurrent.CopyOnWriteArrayList;

public class MeetupActivity extends FragmentActivity implements OnGetMeetupDetailsTaskCompleted,
        OnHeartBeatCompleted, OnAcceptMeetupTaskCompleted, OnActivateMeetupTaskCompleted,
        OnDeactivateMeetupTaskCompleted, GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {

    private static final String TAG = "MeetupActivity";
    private DataApi dataApiInst;
    private MeetupObject mo;
    private List<LocationObject> locationObjectList = new CopyOnWriteArrayList<>();

    GoogleApiClient mGAC;
    LatLng mLoc;
    Location currentLocation;
    Location mLastLocation;
//    LocationRequest lr1;
    public static GoogleMap mMap;
    private String userEmail;

    static final LatLng NewDelhi = new LatLng(28.6139, 77.2089);

    private ApiCustomMessagesUpLocationMessage upLocationMessage;

    TextView meetupName, owner, timeToArrive;
    Switch switchActive;
    Button acceptBtn;
    Button startButton;
    AlarmManager alarmMgr;
    PendingIntent alarmIntent;
    SharedPreferences settings;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meetup_maps);
        startButton = (Button) findViewById(R.id.button_map);
//        getActionBar().setDisplayHomeAsUpEnabled(true);

        mGAC = new GoogleApiClient.Builder(this).addApi(LocationServices.API).addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this).build();
        mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
                .getMap();
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            mo = new MeetupObject(extras.getString("name"), extras.getString("owner"),
                    extras.getBoolean("active"), extras.getBoolean("accepted"));
            dataApiInst = CommonUtils.getDataApiInst();
            Log.v(TAG,"Getting Details for: "+mo.getName()+" "+mo.getOwner());
            (new getMeetupDetailsTask(this, dataApiInst, mo, this)).execute();
        }

        settings = getSharedPreferences("MeetupPreferences", 0);
        SharedPreferences settings = getSharedPreferences("MeetupPreferences", 0);
        userEmail = settings.getString("ACCOUNT_NAME", "");
//        serviceIntent = new Intent(this, HeartBeatService.class);
//        startService(serviceIntent);
        setUI();
    }

    

    private void setUI() {
        meetupName = (TextView) findViewById(R.id.meetupName);
        meetupName.setText(mo.getName());

        owner = (TextView) findViewById(R.id.owner);
        owner.setText(mo.getOwner());

        timeToArrive = (TextView) findViewById(R.id.timeToArrive);
        switchActive = (Switch) findViewById(R.id.switchActive);
        switchActive.setChecked(mo.getActive());

        switchActive.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                SharedPreferences settings = getSharedPreferences("MeetupPreferences", 0);
                String mEmail = settings.getString("ACCOUNT_NAME", "");

                if (!mo.getOwner().equals(mEmail)) {
                    Toast.makeText(MeetupActivity.this, "Only owner can do this", Toast.LENGTH_SHORT).show();
                    switchActive.setChecked(!isChecked);
                }
            }
        });

        acceptBtn = (Button) findViewById(R.id.acceptBtn);
        if (mo.getAccepted())
            acceptBtn.setVisibility(View.INVISIBLE);
    }

    public void onAccept(View v) {
        (new acceptMeetupTask(this, dataApiInst, mo, this)).execute();
    }

    public void onActiveToggle(View v) {
        SharedPreferences settings = getSharedPreferences("MeetupPreferences", 0);
        String mEmail = settings.getString("ACCOUNT_NAME", "");

        if (!switchActive.isChecked()) {
            //deactivate()
            if (mo.getOwner().equalsIgnoreCase((mEmail)))
                (new deactivateMeetupTask(this, dataApiInst, mo, this)).execute();
            else {
                switchActive.setChecked(true);
                Toast.makeText(this, "Only owner can deactivate the meetup", Toast.LENGTH_SHORT).show();
            }
        } else {

            if (mo.getOwner().equalsIgnoreCase(mEmail)) {
                //activate()
                (new activateMeetupTask(this, dataApiInst, mo, this)).execute();
            } else {
                switchActive.setChecked(false);
                Toast.makeText(this, "Only owner can activate the meetup", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void fillUI() {
        SimpleDateFormat df = new SimpleDateFormat("MMM dd, yyyy HH:mm aa");
        Log.v(TAG, df.format(mo.getTimeOfArrival()).toString());
        df.setTimeZone(TimeZone.getDefault());
        timeToArrive.setText(df.format(mo.getTimeOfArrival()).toString());
    }

    public void sendHeartBeat(Location myLoc) {
        upLocationMessage = new ApiCustomMessagesUpLocationMessage();
        upLocationMessage.setMeetupOwner(mo.getOwner());
        upLocationMessage.setMeetupName(mo.getName());
        upLocationMessage.setDetails(false);    //set to True to get all location updates
        upLocationMessage.setLat(myLoc.getLatitude());
        upLocationMessage.setLon(myLoc.getLongitude());
        dataApiInst = CommonUtils.getDataApiInst();
        (new sendHeartBeatTask(MeetupActivity.this, dataApiInst, upLocationMessage, this)).execute();
    }

    private void setUpMyMap(LatLng latLng) {
        Log.d(TAG, "setUpMyMap called");
        mMap.clear();
//        mMap.addMarker(new MarkerOptions().position(latLng).title("Here I am").snippet("Cool Bro!").draggable(true));
        mMap.setMyLocationEnabled(true);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(18), 2000, null);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_meetup, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    //GetMeetups Task
    @Override
    public void onTaskCompleted(ApiCustomMessagesMeetupDescMessage meetupDesc) {
        if (meetupDesc != null) {
            Log.v(TAG, meetupDesc.getLatDestination() + ", " + meetupDesc.getLonDestination()
                    + ": " + meetupDesc.getTimeToArrive());
            mo.setLat(meetupDesc.getLatDestination());
            mo.setLon(meetupDesc.getLonDestination());
            mo.setTimeOfArrival(meetupDesc.getTimeToArrive().getValue());
            fillUI();
        }
    }

    //AcceptMeetups Task
    @Override
    public void onTaskCompleted(ApiCustomMessagesSuccessMessage meetupSuccess) {
        if (meetupSuccess != null) {
            Log.v(TAG, "Accept: " + meetupSuccess.getStrValue());
            if (meetupSuccess.getStrValue().equals("Success")) {
                Toast.makeText(this, "Welcome to the meetup", Toast.LENGTH_SHORT).show();
                acceptBtn.setVisibility(View.INVISIBLE);
            }
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        mMap.clear();
    }

    @Override
    protected void onPause() {
        super.onPause();
//        LocationServices.FusedLocationApi.removeLocationUpdates(mGAC, this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume called");
        mGAC.connect();
        locationObjectList = DbFunctions.read(MeetupActivity.this, mo.getName());
        for(LocationObject lo : locationObjectList)
        {
            if(!(lo.getUsername().equals(userEmail)))
            {
                LatLng latLng = new LatLng(lo.getLat(), lo.getLon());
                mMap.addMarker(new MarkerOptions().position(latLng).title(lo.getUsername())
                        .snippet(String.valueOf(lo.getTime())));
            }
        }
//        createLocationRequest();
    }

    @Override
    public void onConnected(Bundle bundle) {
        Log.d("DEBUG", "onConnected reached");
        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGAC);
        if (mLastLocation != null) {
            Toast.makeText(getApplicationContext(), String.valueOf(mLastLocation.getLatitude() + " " + mLastLocation.getLongitude()), Toast.LENGTH_LONG).show();
            mLoc = new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude());
            setUpMyMap(mLoc);
        } else
            setUpMyMap(NewDelhi);
//        LocationServices.FusedLocationApi.requestLocationUpdates(mGAC, lr1, this);
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.d("DEBUG", "onConnectedSuspended reached");
    }

//    @Override
//    public void onLocationChanged(Location location) {
//        if(location!=null)
//        {
//            currentLocation = location;
//            Toast.makeText(getApplicationContext(), String.valueOf(currentLocation.getLatitude() + " " + currentLocation.getLongitude()), Toast.LENGTH_LONG).show();
//            sendHeartBeat(currentLocation);
//
//        }
//        else
//            Log.d(TAG,"location object found as null");
//    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.d("DEBUG", "onConnectedFailed reached");
    }

    @Override
    public void onHeatBeatReceived(ApiCustomMessagesMeetupLocationsUpdateFullMessage fullMessage) {
        List<ApiCustomMessagesPeepLocationsMessage> userMeetupLocations = fullMessage.getUserMeetupLocations();
        if (userMeetupLocations != null) {
            for (int i = 0; i < userMeetupLocations.size(); ++i) {
                Log.d(TAG, userMeetupLocations.get(i).toString());
                mMap.clear();
                ApiCustomMessagesPeepLocationsMessage peepLocationsMessage = userMeetupLocations.get(i);
                String userName = peepLocationsMessage.getName();
                String userEmail = peepLocationsMessage.getEmail();
                ApiCustomMessagesLocationMessage userLoc = peepLocationsMessage.getLatestLocation();
                LatLng latLng = new LatLng(userLoc.getLat(), userLoc.getLon());
                //Comment the line below to see your location also, as a marker on the map
                if (!peepLocationsMessage.getEmail().equals(mo.getOwner()))
                    mMap.addMarker(new MarkerOptions().position(latLng).title(userName).snippet(userEmail));
            }
        }
    }

    @Override
    public void onActivateTaskCompleted(ApiCustomMessagesSuccessMessage meetupSuccess) {

        if (meetupSuccess.getStrValue().contains("cannot be activated")) {
            switchActive.setChecked(false);
            // 1. Instantiate an AlertDialog.Builder with its constructor
            AlertDialog.Builder builder = new AlertDialog.Builder(this);

            // 2. Chain together various setter methods to set the dialog characteristics
            builder.setMessage(meetupSuccess.getStrValue())
                    .setTitle("Dayyuuum!");

            // 3. Get the AlertDialog from create()
            AlertDialog dialog = builder.create();

            dialog.show();
            mo.setActive(false);

        } else if (meetupSuccess.getStrValue().contains("activated")) {
            alarmMgr = (AlarmManager) this.getSystemService(Context.ALARM_SERVICE);
            Intent i = new Intent(this, DeactivateAlarmReceiver.class);
            i.putExtra("name", mo.getName());
            i.putExtra("owner", mo.getOwner());
            i.putExtra("active", mo.getActive());
            i.putExtra("accepted", mo.getAccepted());

            int index=CommonUtils.getNextIndexOfAlarmIntents();
            alarmIntent = PendingIntent.getBroadcast(this, index, i, 0);

            CommonUtils.setAlarmIntentToList(new MeetupAlarmIntent(mo.getName(),alarmIntent));

            alarmMgr.setInexactRepeating(AlarmManager.RTC_WAKEUP, mo.getTimeOfArrival() + AlarmManager.INTERVAL_FIFTEEN_MINUTES,
                    AlarmManager.INTERVAL_FIFTEEN_MINUTES, alarmIntent);

            Toast.makeText(this,"Activated",Toast.LENGTH_SHORT).show();
            mo.setActive(true);
        }
    }

    @Override
    public void onDeactivateTaskCompleted(ApiCustomMessagesSuccessMessage meetupSuccess) {
        if (meetupSuccess.getStrValue().contains("deactivated")) {
            mo.setActive(false);
            if (alarmMgr != null) {
                alarmIntent = CommonUtils.getAlarmIntent();
                alarmIntent=CommonUtils.getAlarmIntentByName(mo.getName());
                alarmMgr.cancel(alarmIntent);
                Toast.makeText(this,"Deactivated",Toast.LENGTH_SHORT).show();
            }
        }
        else{
            mo.setActive(true);
            switchActive.setChecked(true);
            // 1. Instantiate an AlertDialog.Builder with its constructor
            AlertDialog.Builder builder = new AlertDialog.Builder(this);

            // 2. Chain together various setter methods to set the dialog characteristics
            builder.setMessage(meetupSuccess.getStrValue())
                    .setTitle("Dayyuuumn!");

            // 3. Get the AlertDialog from create()
            AlertDialog dialog = builder.create();

            dialog.show();
        }
    }
}

