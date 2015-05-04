package iiitd.ac.in.dsys.meetup.messages;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.appspot.intense_terra_821.users_api.UsersApi;
import com.appspot.intense_terra_821.users_api.model.ApiCustomMessagesFriendsProfilesMessage;

import java.io.IOException;

/**
 * Created by vedantdasswain on 04/05/15.
 */
public class refreshContactsTask extends AsyncTask<Void, Void, String> {
    Context context;
    UsersApi usersApi;
    ApiCustomMessagesFriendsProfilesMessage contactsList;

    public final String TAG="contactsTask";

    public refreshContactsTask(Context context, UsersApi usersApi) {
        this.context = context;
//        this.builder = builder;
        this.usersApi = usersApi;
    }

    @Override
    protected String doInBackground(Void... params) {
        try {

            // This function fetches the current friends.
            contactsList=usersApi.refreshContacts().execute();

            return "Retrieved contacts";
        } catch (IOException e) {
            Log.d(TAG, e.getMessage(), e);
            e.printStackTrace();
        }
        return "Failed to get contacts";
    }

    @Override
    protected void onPostExecute(String message){
        Log.v(TAG,message);
        Toast.makeText(context,"Contacts refreshed",Toast.LENGTH_SHORT).show();
        return ;
    }
}
