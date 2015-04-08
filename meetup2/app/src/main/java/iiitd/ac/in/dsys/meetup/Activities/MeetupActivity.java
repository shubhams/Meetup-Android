package iiitd.ac.in.dsys.meetup.Activities;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.appspot.intense_terra_821.data_api.DataApi;
import com.appspot.intense_terra_821.data_api.model.ApiCustomMessagesMeetupDescMessage;
import com.appspot.intense_terra_821.data_api.model.ApiCustomMessagesSuccessMessage;

import java.text.SimpleDateFormat;
import java.util.TimeZone;

import iiitd.ac.in.dsys.meetup.CommonUtils;
import iiitd.ac.in.dsys.meetup.ObjectClasses.MeetupObject;
import iiitd.ac.in.dsys.meetup.R;
import iiitd.ac.in.dsys.meetup.TaskCompleteInterfaces.OnAcceptMeetupTaskCompleted;
import iiitd.ac.in.dsys.meetup.TaskCompleteInterfaces.OnGetMeetupDetailsTaskCompleted;
import iiitd.ac.in.dsys.meetup.messages.acceptMeetupTask;
import iiitd.ac.in.dsys.meetup.messages.getMeetupDetailsTask;

public class MeetupActivity extends ActionBarActivity
        implements OnGetMeetupDetailsTaskCompleted,OnAcceptMeetupTaskCompleted {

    private static final String TAG ="MeetupActivity" ;
    private DataApi dataApiInst;
    private MeetupObject mo;

    TextView meetupName, owner,timeToArrive;
    Switch switchActive;
    Button acceptBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meetup);
//        getActionBar().setDisplayHomeAsUpEnabled(true);

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

        owner=(TextView)findViewById(R.id.owner);
        owner.setText(mo.getOwner());

        timeToArrive=(TextView)findViewById(R.id.timeToArrive);
        switchActive=(Switch)findViewById(R.id.switchActive);
        switchActive.setChecked(mo.getActive());

        acceptBtn=(Button)findViewById(R.id.acceptBtn);
        if(mo.getAccepted())
            acceptBtn.setVisibility(View.INVISIBLE);
    }

    public void onAccept(View v){
        (new acceptMeetupTask(this, dataApiInst,mo, this)).execute();
    }

    private void fillUI(){
        SimpleDateFormat df = new SimpleDateFormat("MMM dd, yyyy HH:mm");
        df.setTimeZone(TimeZone.getDefault());
        timeToArrive.setText(df.format(mo.getTimeOfArrival()).toString());
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
        if(meetupDesc!=null) {
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
        if(meetupSuccess!=null){
            Log.v(TAG,"Accept: "+ meetupSuccess.getStrValue());
            if(meetupSuccess.getStrValue().equals("Success")){
                Toast.makeText(this,"Welcome to the meetup",Toast.LENGTH_SHORT).show();
                acceptBtn.setVisibility(View.INVISIBLE);
            }
        }
    }
}
