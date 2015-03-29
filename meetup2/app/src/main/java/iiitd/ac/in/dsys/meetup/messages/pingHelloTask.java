package iiitd.ac.in.dsys.meetup.messages;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.appspot.intense_terra_821.users_api.UsersApi;
import com.appspot.intense_terra_821.users_api.model.ApiCustomMessagesSuccessMessage;

import java.io.IOException;

/**
 * Created by aditya on 12/02/15.
 */
public class pingHelloTask extends AsyncTask<Void, Void, ApiCustomMessagesSuccessMessage> {
    Context context;
    UsersApi usersApi;

    public pingHelloTask(Context context, UsersApi usersApi) {
        this.context = context;
//        this.builder = builder;
        this.usersApi = usersApi;
    }

    @Override
    protected ApiCustomMessagesSuccessMessage doInBackground(Void... params) {
        ApiCustomMessagesSuccessMessage reply = null;
        try {
            // UsersApi service = builder.build();
            reply = usersApi.pingHello().execute();
        } catch (IOException e) {
            Log.d("Meetup", e.getMessage(), e);
            e.printStackTrace();
        }
        return reply;
    }

    @Override
    protected void onPostExecute(ApiCustomMessagesSuccessMessage reply) {
        if (reply != null)
            Toast.makeText(context, reply.getStrValue(), Toast.LENGTH_LONG).show();
    }
}
