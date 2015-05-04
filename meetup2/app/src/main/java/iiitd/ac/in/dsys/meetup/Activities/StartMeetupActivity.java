package iiitd.ac.in.dsys.meetup.Activities;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.location.Location;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.*;
import com.appspot.intense_terra_821.data_api.DataApi;
import com.appspot.intense_terra_821.data_api.model.ApiCustomMessagesUpMeetupCreateMessage;
import com.appspot.intense_terra_821.users_api.UsersApi;
import com.appspot.intense_terra_821.users_api.model.ApiCustomMessagesFriendsProfilesMessage;
import com.appspot.intense_terra_821.users_api.model.ApiCustomMessagesProfileMessageFriendMessage;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.*;
import com.google.api.client.util.DateTime;
import iiitd.ac.in.dsys.meetup.CommonUtils;
import iiitd.ac.in.dsys.meetup.CustomUI.ContactsListAdapter;
import iiitd.ac.in.dsys.meetup.ObjectClasses.ContactObject;
import iiitd.ac.in.dsys.meetup.ObjectClasses.MeetupAlarmIntent;
import iiitd.ac.in.dsys.meetup.ObjectClasses.MeetupObject;
import iiitd.ac.in.dsys.meetup.R;
import iiitd.ac.in.dsys.meetup.Receivers.DeactivateAlarmReceiver;
import iiitd.ac.in.dsys.meetup.TaskCompleteInterfaces.OnContactsTaskCompleted;
import iiitd.ac.in.dsys.meetup.TaskCompleteInterfaces.OnMakeMeetupTaskCompleted;
import iiitd.ac.in.dsys.meetup.messages.contactsTask;
import iiitd.ac.in.dsys.meetup.messages.makeMeetupTask;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.TimeZone;

public class StartMeetupActivity extends ActionBarActivity
        implements OnContactsTaskCompleted,android.widget.CompoundButton.OnCheckedChangeListener
                    ,OnMakeMeetupTaskCompleted,GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {

    private static final String TAG ="StartMeetupActivity" ;
    LatLng meetUpLocation;
    LinearLayout markerLayout;
    ImageView markerIcon;
    GoogleApiClient googleApiClient;
    GoogleMap googleMap;
    Location mLastLocation;
    LatLng latLng;
    LatLng center;
    ContactsListAdapter contactsListAdapter;
    ArrayList<String> invitees;
    ArrayList<ContactObject> contacts;
    ListView lv;
    DatePicker dp;
    TimePicker tp;
    EditText ed;
    UsersApi usersApiInst;
    DataApi dataApiInst;
    ProgressDialog progressDialog;
    ViewFlipper vf;
    MenuItem prev,next,done;
    DateTime timeToArrive;
    String meetupName="";
    double lat=28.546207,lon=77.272208;
    static final LatLng NewDelhi = new LatLng(28.6139, 77.2089);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_meetup);
        markerLayout = (LinearLayout) findViewById(R.id.locationMarker);
        markerIcon = (ImageView) findViewById(R.id.icon_marker);
        lv=(ListView)findViewById(R.id.contactsListView);
        dp=(DatePicker)findViewById(R.id.datePicker);
        tp=(TimePicker)findViewById(R.id.timePicker);
        vf=(ViewFlipper) findViewById(R.id.viewFlipper);
        ed=(EditText) findViewById(R.id.meetupEditText);
        contacts=new ArrayList<ContactObject>();
        invitees=new ArrayList<String>();
        progressDialog=new ProgressDialog(this);
        dataApiInst= CommonUtils.getDataApiInst();
        googleApiClient = new GoogleApiClient.Builder(this).addApi(LocationServices.API).addConnectionCallbacks(this).addOnConnectionFailedListener(this).build();
    }

    @Override
    protected void onResume(){
        super.onResume();
        makeMyMap();
        googleApiClient.connect();
        if(!contacts.isEmpty())
            contacts.clear();
    }

    private void getContacts() {
        Log.v(TAG, "Start fetching meetups...");
        usersApiInst= CommonUtils.getUsersApiInst();
        progressDialog=ProgressDialog.show(this, "Wait", "Fetching contacts...");
        (new contactsTask(this, usersApiInst, this)).execute();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_start_meetup, menu);
        prev=menu.findItem(R.id.action_previous);
//        prev.setVisible(false);

        next=menu.findItem(R.id.action_next);

        done=menu.findItem(R.id.action_done);
//        next.setVisible(false);

//        invalidateOptionsMenu();

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch (id){
            case R.id.action_previous:
                vf.showPrevious();
                if(vf.getDisplayedChild()==0)
                    prev.setVisible(false);
                else if(vf.getDisplayedChild()<2)
                    done.setVisible(false);
                invalidateOptionsMenu();
                return true;

            case R.id.action_next:
                vf.showNext();
                if(vf.getDisplayedChild()==2) {
                    getContacts();
                    next.setVisible(false);
                    done.setVisible(true);
                }
                invalidateOptionsMenu();
                return true;

            case R.id.action_done:
                storeInputs();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void makeMyMap()
    {
        googleMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
                .getMap();
//        setUpMap(NewDelhi);
    }

    private void setUpMap(LatLng myLatLng)
    {
        Log.d(TAG,"setUpMyMap called");
        googleMap.clear();
//        mMap.addMarker(new MarkerOptions().position(latLng).title("Here I am").snippet("Cool Bro!").draggable(true));
        googleMap.setMyLocationEnabled(true);
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(myLatLng));
        googleMap.animateCamera(CameraUpdateFactory.zoomTo(18),2000,null);
        meetUpLocation = googleMap.getCameraPosition().target;
        googleMap.setOnCameraChangeListener(new GoogleMap.OnCameraChangeListener() {
            @Override
            public void onCameraChange(CameraPosition cameraPosition) {
                center = googleMap.getCameraPosition().target;
                googleMap.clear();
                markerLayout.setVisibility(View.VISIBLE);
            }
        });
        markerLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bitmap bitmap = ((BitmapDrawable)markerIcon.getDrawable()).getBitmap();
                bitmap = bitmap.createScaledBitmap(bitmap,75,75,false);
                meetUpLocation = new LatLng(center.latitude,
                        center.longitude);
                Toast.makeText(getApplicationContext(), String.valueOf(center.latitude+" "+center.longitude),
                        Toast.LENGTH_LONG).show();
                Log.d(TAG, "Location coordinates:"+String.valueOf(center.latitude+" "+center.longitude));
                        Marker m = googleMap.addMarker(new MarkerOptions()
                        .position(meetUpLocation)
                        .title(" Set Meetup Location ")
                        .snippet("")
                        .icon(BitmapDescriptorFactory
                                .fromBitmap(bitmap)));
                m.setDraggable(true);
                markerLayout.setVisibility(View.GONE);
            }
        });

    }

    private void storeInputs() {
        Calendar c=Calendar.getInstance();
        c.setTimeZone(TimeZone.getTimeZone("UTC"));

        int year=dp.getYear();
        int month=dp.getMonth();
        int day=dp.getDayOfMonth();
        int hourOfDay=tp.getCurrentHour();
        int minute=tp.getCurrentMinute();

        Log.v(TAG,"DateTime "+day+" "+hourOfDay);

//        c.set(year,month,day,hourOfDay+1,minute);
        c.setTimeZone(TimeZone.getDefault());
        c.set(Calendar.YEAR,year);
        c.set(Calendar.MONTH,month);
        c.set(Calendar.DAY_OF_MONTH,day);
        c.set(Calendar.HOUR_OF_DAY,hourOfDay);
        c.set(Calendar.MINUTE,minute);

        Log.v(TAG,"CalendarTime "+c.getTime());
        meetupName=ed.getText().toString();

//        c.setTimeZone(TimeZone.getTimeZone("UTC"));

//        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss");
//        sdf.setTimeZone(TimeZone.getDefault());
//
//        Log.v(TAG, "FormatTime " + sdf.format(c.getTime()).toString());

//        timeToArrive=new DateTime(sdf.format(c.getTime()).toString());

        timeToArrive=new DateTime(c.getTime());


        Log.v(TAG,"DateTime "+timeToArrive);

//        try {
//            Log.v(TAG,meetupName+" on "+timeToArrive+" "+sdf.parse(sdf.format(c.getTime())));
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }

        createUpMessage();
    }

    private void createUpMessage() {
        ApiCustomMessagesUpMeetupCreateMessage createMessage=new ApiCustomMessagesUpMeetupCreateMessage();
        if(meetupName.isEmpty()){
            Toast.makeText(this,"Please enter a meetupName",Toast.LENGTH_SHORT).show();
            return;
        }
        createMessage.setName(meetupName);
        if(invitees==null || invitees.isEmpty()){
            Toast.makeText(this,"No contacts have been invited",Toast.LENGTH_SHORT).show();
            return;
        }
        createMessage.setInvited(invitees);

        createMessage.setTimeToArrive(timeToArrive);
        Log.d(TAG, "line:266  " + meetUpLocation.latitude+" "+meetUpLocation.longitude);
        createMessage.setLat( meetUpLocation.latitude);
        createMessage.setLon( meetUpLocation.longitude);

        progressDialog=ProgressDialog.show(this, "Wait", "Creating meetup...");
        (new makeMeetupTask(this, dataApiInst,this,createMessage)).execute();
    }

    @Override
    public void onTaskCompleted(ApiCustomMessagesFriendsProfilesMessage contactsList) {
        progressDialog.dismiss();
        progressDialog.cancel();
        if(contactsList!=null) {
            Log.v(TAG,"Success message"+contactsList.getSuccess().getStrValue());
            if (!contactsList.isEmpty() && contactsList.getProfiles() != null)
                for (ApiCustomMessagesProfileMessageFriendMessage name : contactsList.getProfiles()) {
                    ContactObject contact=new ContactObject(name.getEmail(),false);
                    if(!contacts.contains(contact))
                        contacts.add(contact);
                    Log.v(TAG, "Email name: " + name.getEmail());
                }
            else {
                Log.v(TAG, "No contacts");
                Toast.makeText(this, "Your contacts aren't on Meetup", Toast.LENGTH_SHORT).show();
                Toast.makeText(this,"Ask them to join",Toast.LENGTH_SHORT).show();
            }
        }

        if(contacts!=null && !contacts.isEmpty()){
            contactsListAdapter=new ContactsListAdapter(this,contacts,this);
            lv.setAdapter(contactsListAdapter);
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        int pos = lv.getPositionForView(buttonView);
        if (pos != ListView.INVALID_POSITION) {
            if(isChecked) {
                invitees.add(contacts.get(pos).getName());
                contacts.get(pos).setInvited(true);
            }
            else {
                invitees.remove(contacts.get(pos).getName());
                contacts.get(pos).setInvited(false);
            }
        }
    }

    @Override
    public void onTaskCompleted(String message) {
        progressDialog.dismiss();
        progressDialog.cancel();
        Log.v(TAG,"Meetup creation: "+message);
        Toast.makeText(this,"Meetup creation: "+message,Toast.LENGTH_SHORT).show();
        if(message.equals("Success")) {
            long currTime=System.currentTimeMillis();
            long timeDiffHours=(timeToArrive.getValue()-currTime)/1000/60;
            if(timeDiffHours<180){
                setAlarm();
            }
            else{
                finish();
            }
        }
    }

    @Override
    public void onConnected(Bundle bundle) {
        Log.d(TAG,"onConnected called");
        Toast.makeText(this, "Connected", Toast.LENGTH_SHORT).show();
        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);
        if(mLastLocation!=null)
        {
            latLng = new LatLng(mLastLocation.getLatitude(),mLastLocation.getLongitude());
            setUpMap(latLng);
        }
        else
        {
            Log.d(TAG,"last location found to be null");
            setUpMap(NewDelhi);
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

        Log.d(TAG,"onConnectionFailed called");
    }

    public void setAlarm(){
        SharedPreferences settings = getSharedPreferences("MeetupPreferences", 0);
        String mEmail = settings.getString("ACCOUNT_NAME", "");
        MeetupObject mo=new MeetupObject(meetupName,mEmail,true,true);

        AlarmManager alarmMgr = (AlarmManager) this.getSystemService(Context.ALARM_SERVICE);
        Intent i = new Intent(this, DeactivateAlarmReceiver.class);
        i.putExtra("name", mo.getName());
        i.putExtra("owner", mo.getOwner());
        i.putExtra("active", mo.getActive());
        i.putExtra("accepted", mo.getAccepted());

        int index=CommonUtils.getNextIndexOfAlarmIntents();
        PendingIntent alarmIntent = PendingIntent.getBroadcast(this, index, i, 0);

        CommonUtils.setAlarmIntentToList(new MeetupAlarmIntent(mo.getName(),alarmIntent));

        alarmMgr.setInexactRepeating(AlarmManager.RTC_WAKEUP, mo.getTimeOfArrival() + AlarmManager.INTERVAL_FIFTEEN_MINUTES,
                AlarmManager.INTERVAL_FIFTEEN_MINUTES, alarmIntent);

        Toast.makeText(this,"Activated",Toast.LENGTH_SHORT).show();
        mo.setActive(true);

        finish();
    }

}
