package iiitd.ac.in.dsys.meetup.Activities;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.appspot.intense_terra_821.users_api.UsersApi;
import com.appspot.intense_terra_821.users_api.model.ApiCustomMessagesFriendsProfilesMessage;
import com.appspot.intense_terra_821.users_api.model.ApiCustomMessagesProfileMessageFriendMessage;

import java.util.ArrayList;

import iiitd.ac.in.dsys.meetup.CommonUtils;
import iiitd.ac.in.dsys.meetup.CustomUI.ContactsListAdapter;
import iiitd.ac.in.dsys.meetup.R;
import iiitd.ac.in.dsys.meetup.TaskCompleteInterfaces.OnContactsTaskCompleted;
import iiitd.ac.in.dsys.meetup.messages.contactsTask;

public class StartMeetupActivity extends ActionBarActivity
        implements OnContactsTaskCompleted,android.widget.CompoundButton.OnCheckedChangeListener{

    private static final String TAG ="StartMeetupActivity" ;
    ContactsListAdapter contactsListAdapter;
    ArrayList<String> contacts,invitees;
    ListView lv;
    UsersApi usersApiInst;
    ProgressDialog progressDialog;
    ViewFlipper vf;
    MenuItem prev,next,done;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_meetup);
        lv=(ListView)findViewById(R.id.contactsListView);
        vf=(ViewFlipper) findViewById(R.id.viewFlipper);
        contacts=new ArrayList<String>();
        invitees=new ArrayList<String>();
        progressDialog=new ProgressDialog(this);
    }

    @Override
    protected void onResume(){
        super.onResume();
        if(!contacts.isEmpty())
            contacts.clear();
    }

    private void getContacts() {
        Log.v(TAG, "Start fetching meetups...");
        usersApiInst= CommonUtils.getUsersApiInst();
        progressDialog=ProgressDialog.show(this, "Wait", "Fetching contacts...");
        (new contactsTask(this, usersApiInst,this)).execute();
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
                    if(!contacts.isEmpty())
                        getContacts();
                    next.setVisible(false);
                    done.setVisible(true);
                }
                invalidateOptionsMenu();
                return true;

            case R.id.action_done:
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onTaskCompleted(ApiCustomMessagesFriendsProfilesMessage contactsList) {
        progressDialog.dismiss();
        progressDialog.cancel();
        if(contactsList!=null) {
            Log.v(TAG,"Success message"+contactsList.getSuccess().getStrValue());
            if (!contactsList.isEmpty() && contactsList.getProfiles() != null)
                for (ApiCustomMessagesProfileMessageFriendMessage name : contactsList.getProfiles()) {
                    contacts.add(name.getEmail());
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
            if(isChecked)
                invitees.add(contacts.get(pos));
            else
                invitees.remove(contacts.get(pos));
        }
    }
}
