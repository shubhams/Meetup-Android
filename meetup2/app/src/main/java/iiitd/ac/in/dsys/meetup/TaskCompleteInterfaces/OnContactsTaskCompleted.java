package iiitd.ac.in.dsys.meetup.TaskCompleteInterfaces;

import com.appspot.intense_terra_821.users_api.model.ApiCustomMessagesFriendsProfilesMessage;

/**
 * Created by vedantdasswain on 25/03/15.
 */
public interface OnContactsTaskCompleted {
    void onTaskCompleted(ApiCustomMessagesFriendsProfilesMessage contactsList);
}
