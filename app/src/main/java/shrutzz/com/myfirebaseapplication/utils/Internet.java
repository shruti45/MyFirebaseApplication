package shrutzz.com.myfirebaseapplication.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;


/**
 * Created by user on 11/23/2016.
 */

public class Internet {

    private Context _context;
    public static String interNetMsg = "Please enable internet connection!";
    public static String noResultFoundMsg = "No Result Found!";
    public Internet(Context context){
        this._context = context;
    }
    public static boolean isConnectedToInternet(Context context){
        ConnectivityManager connectivity =  ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE));
        if (connectivity != null )
        {

            NetworkInfo[] info = connectivity.getAllNetworkInfo();
            if (info != null)
            {
                for (int i = 0; i < info.length; i++)
                {

                    if (info[i].getState() == NetworkInfo.State.CONNECTED && info[i].isConnectedOrConnecting())
                    {
                        return true;
                    }
                }
            }
        }
        return false;
    }
}
