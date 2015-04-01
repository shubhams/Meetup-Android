package iiitd.ac.in.dsys.meetup.messages;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.appspot.intense_terra_821.users_api.UsersApi;
import com.appspot.intense_terra_821.users_api.model.ApiCustomMessagesFriendsProfilesMessage;

import java.io.IOException;

import iiitd.ac.in.dsys.meetup.TaskCompleteInterfaces.OnContactsTaskCompleted;

/**
 * Created by aditya on 16/02/15.
 */
public class contactsTask extends AsyncTask<Void, Void, String> {
    Context context;
    UsersApi usersApi;
    ApiCustomMessagesFriendsProfilesMessage contactsList;
    private OnContactsTaskCompleted listener;

    public final String TAG="contactsTask";

    public contactsTask(Context context, UsersApi usersApi, OnContactsTaskCompleted listener) {
        this.context = context;
//        this.builder = builder;
        this.usersApi = usersApi;
        this.listener = listener;
    }

    @Override
    protected String doInBackground(Void... params) {
        try {

            // This function fetches the current friends.
            contactsList=usersApi.getFriends().execute();

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
        listener.onTaskCompleted(contactsList);
        return ;
    }
}
