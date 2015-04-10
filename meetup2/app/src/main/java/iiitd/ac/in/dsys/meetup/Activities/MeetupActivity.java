package iiitd.ac.in.dsys.meetup.Activities;

import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import com.appspot.intense_terra_821.data_api.DataApi;
import com.appspot.intense_terra_821.data_api.model.*;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import iiitd.ac.in.dsys.meetup.CommonUtils;
import iiitd.ac.in.dsys.meetup.ObjectClasses.MeetupObject;
import iiitd.ac.in.dsys.meetup.R;
import iiitd.ac.in.dsys.meetup.TaskCompleteInterfaces.OnGetMeetupDetailsTaskCompleted;
import iiitd.ac.in.dsys.meetup.TaskCompleteInterfaces.OnHeartBeatCompleted;
import iiitd.ac.in.dsys.meetup.messages.getMeetupDetailsTask;
import iiitd.ac.in.dsys.meetup.messages.sendHeartBeatTask;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.TimeZone;

public class MeetupActivity extends FragmentActivity implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, LocationListener,OnGetMeetupDetailsTaskCompleted,
        OnHeartBeatCompleted
{

    private static final String TAG ="MeetupActivity" ;
    private DataApi dataApiInst;
    private MeetupObject mo;

    GoogleApiClient mGAC;
    LatLng mLoc;
    Location currentLocation;
    Location mLastLocation;
    LocationRequest lr1;
    public static GoogleMap mMap;

    static final LatLng NewDelhi = new LatLng(28.6139, 77.2089);

    private ApiCustomMessagesUpLocationMessage upLocationMessage;

    TextView meetupName, owner,timeToArrive;
    Switch switchActive;
    Button acceptBtn;
    Button startButton;

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
        if(extras !=null) {
            mo=new MeetupObject(extras.getString("name"),extras.getString("owner"),
                    extras.getBoolean("active"),extras.getBoolean("accepted"));
            dataApiInst= CommonUtils.getDataApiInst();
            (new getMeetupDetailsTask(this, dataApiInst,mo, this)).execute();
        }

        setUI();
    }

    private void setUI(){
        meetupName=(TextView)findViewById(R.id.meetupName);
        meetupName.setText(mo.getName());
        if(!mo.getAccepted())
            meetupName.setTextColor(getResources()
                    .getColor(R.color.dim_foreground_disabled_material_light));

        owner=(TextView)findViewById(R.id.owner);
        owner.setText(mo.getOwner());

        timeToArrive=(TextView)findViewById(R.id.timeToArrive);
        switchActive=(Switch)findViewById(R.id.switchActive);
        switchActive.setChecked(mo.getActive());

        acceptBtn=(Button)findViewById(R.id.acceptBtn);
        if(mo.getAccepted())
            acceptBtn.setVisibility(View.INVISIBLE);
    }

    private void fillUI(){
        SimpleDateFormat df = new SimpleDateFormat("MMM dd, yyyy HH:mm");
        df.setTimeZone(TimeZone.getDefault());
        timeToArrive.setText(df.format(mo.getTimeOfArrival()).toString());
    }

    public void sendHeartBeat(Location myLoc)
    {
        upLocationMessage = new ApiCustomMessagesUpLocationMessage();
        upLocationMessage.setMeetupOwner(mo.getOwner());
        upLocationMessage.setMeetupName(mo.getName());
        upLocationMessage.setDetails(false);    //set to True to get all location updates
        upLocationMessage.setLat(myLoc.getLatitude());
        upLocationMessage.setLon(myLoc.getLongitude());
        dataApiInst= CommonUtils.getDataApiInst();
        (new sendHeartBeatTask(MeetupActivity.this, dataApiInst, upLocationMessage, this)).execute();
    }

    private void setUpMyMap(LatLng latLng){
        Log.d(TAG,"setUpMyMap called");
        mMap.clear();
//        mMap.addMarker(new MarkerOptions().position(latLng).title("Here I am").snippet("Cool Bro!").draggable(true));
        mMap.setMyLocationEnabled(true);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(18),2000,null);
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

    @Override
    public void onTaskCompleted(ApiCustomMessagesMeetupDescMessage meetupDesc) {
        if(meetupDesc!=null) {
            Log.v(TAG, meetupDesc.getLatDestination() + ", " + meetupDesc.getLonDestination()
                    + ": " + meetupDesc.getTimeToArrive());
            mo.setLat(meetupDesc.getLatDestination());
            mo.setLon(meetupDesc.getLonDestination());
            mo.setTimeOfArrival(meetupDesc.getTimeToArrive().getValue());
            fillUI();
        }
    }

    private void createLocationRequest()
    {
        lr1 = new LocationRequest();
        lr1.setInterval(20000);
        lr1.setFastestInterval(10000);
        lr1.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    @Override
    protected void onPause() {
        super.onPause();
        LocationServices.FusedLocationApi.removeLocationUpdates(mGAC,this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mGAC.connect();
        createLocationRequest();
    }

    @Override
    public void onConnected(Bundle bundle) {
        Log.d("DEBUG", "onConnected reached");
        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGAC);
        if(mLastLocation!=null)
        {
            Toast.makeText(getApplicationContext(), String.valueOf(mLastLocation.getLatitude() + " " + mLastLocation.getLongitude()), Toast.LENGTH_LONG).show();
            mLoc = new LatLng(mLastLocation.getLatitude(),mLastLocation.getLongitude());
            setUpMyMap(mLoc);
        }
        else
            setUpMyMap(NewDelhi);
        LocationServices.FusedLocationApi.requestLocationUpdates(mGAC,lr1,this);
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.d("DEBUG", "onConnectedSuspended reached");
    }

    @Override
    public void onLocationChanged(Location location) {
        currentLocation = location;
        Toast.makeText(getApplicationContext(), String.valueOf(currentLocation.getLatitude()+" "+currentLocation.getLongitude()),Toast.LENGTH_LONG).show();
        sendHeartBeat(location);
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.d("DEBUG", "onConnectedFailed reached");
    }

    @Override
    public void onHeatBeatReceived(ApiCustomMessagesMeetupLocationsUpdateFullMessage fullMessage) {
        List<ApiCustomMessagesPeepLocationsMessage> userMeetupLocations = fullMessage.getUserMeetupLocations();
        for(int i=0;i<userMeetupLocations.size();++i)
        {
            Log.d(TAG,userMeetupLocations.get(i).toString());
            ApiCustomMessagesPeepLocationsMessage peepLocationsMessage = userMeetupLocations.get(i);
            String userName = peepLocationsMessage.getName();
            String userEmail = peepLocationsMessage.getEmail();
            ApiCustomMessagesLocationMessage userLoc = peepLocationsMessage.getLatestLocation();
            LatLng latLng = new LatLng(userLoc.getLat(),userLoc.getLon());
            //Comment the line below to see your location also, as a marker on the map
            if(!peepLocationsMessage.getEmail().equals(mo.getOwner()))
                MeetupActivity.mMap.addMarker(new MarkerOptions().position(latLng).title(userName).snippet(userEmail));
        }
    }
}
