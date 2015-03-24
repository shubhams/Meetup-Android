package iiitd.ac.in.dsys.meetup.messages;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.appspot.intense_terra_821.users_api.UsersApi;
import com.appspot.intense_terra_821.users_api.model.ApiCustomMessagesFriendsProfilesMessage;
import com.appspot.intense_terra_821.users_api.model.ApiCustomMessagesProfileMessageFriendMessage;

import java.io.IOException;

/**
 * Created by aditya on 16/02/15.
 */
public class contactsTask extends AsyncTask<Void, Void, String> {
    Context context;
    UsersApi usersApi;

    public final String TAG="contactsTask";

    public contactsTask(Context context, UsersApi usersApi) {
        this.context = context;
//        this.builder = builder;
        this.usersApi = usersApi;
    }

    @Override
    protected String doInBackground(Void... params) {
        try {
            ApiCustomMessagesFriendsProfilesMessage contactsList=usersApi.refreshContacts().execute();
            if(contactsList!=null) {
                Log.v(TAG,"Success message"+contactsList.getSuccess().getStrValue());
                if (!contactsList.isEmpty() && contactsList.getProfiles() != null)
                    for (ApiCustomMessagesProfileMessageFriendMessage name : contactsList.getProfiles())
                        Log.v(TAG, "Email name: " + name.getEmail());
                else
                    Log.v(TAG, "No contacts");
            }
            return "Retrieved contacts";
        } catch (IOException e) {
            Log.d(TAG, e.getMessage(), e);
            e.printStackTrace();
        }
        return "Failed to get contacts";
    }

    protected void onPostExecute(String message){
        Log.v(TAG,message);
        return ;
    }
}
