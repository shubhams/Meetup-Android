package iiitd.ac.in.dsys.meetup;

import com.appspot.intense_terra_821.data_api.DataApi;
import com.appspot.intense_terra_821.users_api.UsersApi;

/**
 * Created by vedantdasswain on 25/03/15.
 */
public class CommonUtils {
    static UsersApi usersApiInst;
    static DataApi dataApiInst;

    public static UsersApi getUsersApiInst(){
        return usersApiInst;
    }

    public static DataApi getDataApiInst(){
        return dataApiInst;
    }

    public static void setUsersApiInst(UsersApi musersApiInst){
        usersApiInst=musersApiInst;
    }

    public static void setDataApiInst(DataApi mdataApiInst){
        dataApiInst=mdataApiInst;
    }
}
