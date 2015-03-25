package iiitd.ac.in.dsys.meetup.Activities;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import com.appspot.intense_terra_821.users_api.UsersApi;
import com.appspot.intense_terra_821.users_api.model.ApiCustomMessagesFriendsProfilesMessage;
import com.appspot.intense_terra_821.users_api.model.ApiCustomMessagesProfileMessageFriendMessage;

import java.util.ArrayList;

import iiitd.ac.in.dsys.meetup.CommonUtils;
import iiitd.ac.in.dsys.meetup.CustomUI.ContactsListAdapter;
import iiitd.ac.in.dsys.meetup.R;
import iiitd.ac.in.dsys.meetup.TaskCompleteInterfaces.OnContactsTaskCompleted;
import iiitd.ac.in.dsys.meetup.messages.contactsTask;

public class StartMeetupActivity extends ActionBarActivity implements OnContactsTaskCompleted{

    private static final String TAG ="StartMeetupActivity" ;
    ContactsListAdapter contactsListAdapter;
    ArrayList<String> contacts;
    ListView lv;
    UsersApi usersApiInst;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_meetup);
        lv=(ListView)findViewById(R.id.contactsListView);
        contacts=new ArrayList<String>();
    }

    @Override
    protected void onResume(){
        getContacts();
    }

    private void getContacts() {
        Log.v(TAG, "Start fetching meetups...");
        usersApiInst= CommonUtils.getUsersApiInst();
        (new contactsTask(this, usersApiInst,this)).execute();

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_start_meetup, menu);
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
    public void onTaskCompleted(ApiCustomMessagesFriendsProfilesMessage contactsList) {
        if(contactsList!=null) {
            Log.v(TAG,"Success message"+contactsList.getSuccess().getStrValue());
            if (!contactsList.isEmpty() && contactsList.getProfiles() != null)
                for (ApiCustomMessagesProfileMessageFriendMessage name : contactsList.getProfiles()) {
                    
                    Log.v(TAG, "Email name: " + name.getEmail());
                }
            else
                Log.v(TAG, "No contacts");
        }

        if(contacts!=null && !contacts.isEmpty()){
            contactsListAdapter=new ContactsListAdapter(this,contacts);
            lv.setAdapter(contactsListAdapter);
        }
    }
}
