package shrutzz.com.myfirebaseapplication.app;

import android.content.Context;

/**
 * Created by shrutz on 6/30/17.
 */

public class AppInfo {

    public static String USER_NAME = "first_log";

    public AppInfo() {
    }

    public static String getUserName(Context context) {
        return Store.getStrPref(context, USER_NAME, "");
    }
    public static void setUserName(Context context,String ownerName) {
        Store.setStrPref(context, USER_NAME, ownerName);
    }


}
