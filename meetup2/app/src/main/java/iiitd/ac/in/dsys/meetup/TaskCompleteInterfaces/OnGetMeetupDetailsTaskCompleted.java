package iiitd.ac.in.dsys.meetup.TaskCompleteInterfaces;

import com.appspot.intense_terra_821.data_api.model.ApiCustomMessagesMeetupDescMessage;

/**
 * Created by vedantdasswain on 08/04/15.
 */
public interface OnGetMeetupDetailsTaskCompleted {
    void onTaskCompleted(ApiCustomMessagesMeetupDescMessage meetupDesc);
}
