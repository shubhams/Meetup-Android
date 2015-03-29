package iiitd.ac.in.dsys.meetup.messages;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.appspot.intense_terra_821.users_api.UsersApi;
import com.appspot.intense_terra_821.users_api.model.ApiCustomMessagesSuccessMessage;
import com.appspot.intense_terra_821.users_api.model.ApiCustomMessagesUpFirstLoginMessage;

import java.io.IOException;

/**
 * Created by aditya on 14/02/15.
 */
public class firstLoginTask extends AsyncTask<Void, Void, ApiCustomMessagesSuccessMessage> {
    Context context;
    UsersApi usersApi;
    ApiCustomMessagesUpFirstLoginMessage firstLoginMessage;
    String TAG="firstLoginTask";

    public firstLoginTask(Context context, UsersApi usersApi, String fullName, String phNumber, String regID) {
        this(context, usersApi,
                new ApiCustomMessagesUpFirstLoginMessage().setRegID(regID).setName(fullName).setPhNumber(phNumber));
    }

    public firstLoginTask(Context context, UsersApi usersApi, ApiCustomMessagesUpFirstLoginMessage firstLoginMessage) {
        this.context = context;
        this.usersApi = usersApi;
        this.firstLoginMessage = firstLoginMessage;
    }

    @Override
    protected ApiCustomMessagesSuccessMessage doInBackground(Void... params) {
        ApiCustomMessagesSuccessMessage reply = null;
        try {
            UsersApi.FirstLogin aRequest = usersApi.firstLogin(firstLoginMessage);
            reply = aRequest.execute();
        } catch (IOException e) {
            Log.d(TAG, e.getMessage(), e);
            e.printStackTrace();
        }
        return reply;
    }

    @Override
    protected void onPostExecute(ApiCustomMessagesSuccessMessage reply) {
        if (reply != null) {
            Toast.makeText(context, reply.getStrValue(), Toast.LENGTH_LONG).show();
            Log.v(TAG,reply.getStrValue());
        }
    }

    public void addTokenToMessage(String token) {
        firstLoginMessage.setShortLivedAuthorizationToken(token);
    }

}
