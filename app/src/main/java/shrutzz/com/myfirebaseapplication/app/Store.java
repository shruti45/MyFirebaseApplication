package shrutzz.com.myfirebaseapplication.app;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by shrutz on 6/30/17.
 */

public class Store {
    private static String XEL_PREF = "complexPref";

    public static SharedPreferences getPrefs(Context context) {
        return context.getSharedPreferences(XEL_PREF, Context.MODE_PRIVATE);
    }

    public static String getStrPref(Context context, String userName, String defVal) {
        return getPrefs(context).getString(userName, defVal);
    }


    public static void setStrPref(Context context, String userName, String value) {

        getPrefs(context).edit().putString(userName, value).apply();
    }
}
