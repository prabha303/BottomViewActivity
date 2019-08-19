package com.lynkdriver.lynk.utils;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Environment;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.telephony.TelephonyManager;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;
import com.crashlytics.android.Crashlytics;
import com.lynkdriver.lynk.BottomViewActivity;
import com.lynkdriver.lynk.LynkApplication;
import com.lynkdriver.lynk.constant.AppConstants;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.nio.charset.Charset;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

import static android.content.Context.LOCATION_SERVICE;

//import com.lynkdriver.lynk.Analytics.MPanelAnalytics;

/**
 * This class holds the common method used in the app.
 *
 * @author Jeevanandhan
 */
public class Utility implements AppConstants.SharedConstants, AppConstants.urlConstants
{

    public static final double R = 6372.8; // In kilometers
    private static ProgressDialog pDialog;
    //Single ton object...
    private static Utility utility = null;
    //Single ton method...
    public static Utility getInstance()
    {
        if (utility != null)
        {
            return utility;
        }
        else
        {
            utility = new Utility();
            return utility;
        }
    }

    public static void hideSoftKeyboard(Activity activity) {
        InputMethodManager inputMethodManager =
                (InputMethodManager) activity.getSystemService(
                        Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(
                activity.getCurrentFocus().getWindowToken(), 0);
    }


    public static void printLog(String consoleKey,String  valuePrint)
    {
        try {
            if(Log.isLoggable(consoleKey, Log.INFO))
            {
              Log.i(consoleKey, "- "+valuePrint);
            }
        }catch (Exception e)
        {
            e.printStackTrace();
        }
    }


    public static void resetPreferredLauncherAndOpenChooser(Context context) {
        if(context != null)
        {
            try {
                SharedPref.getInstance().setSharedValue(context, AppConstants.SharedConstants.AccessToken, "");
                SharedPref.getInstance().setSharedValue(context, AppConstants.SharedConstants.RefreshToken, "");
                SharedPref.getInstance().setSharedValue(context, hasLoggedIn,false);
                SharedPref.getInstance().setSharedValue(context, consumerSecret,"");
                SharedPref.getInstance().setSharedValue(context, "user_id", "");
                Intent i = new Intent(context, BottomViewActivity.class);
                context.startActivity(i);
            }catch (Exception e)
            {
                e.printStackTrace();
            }
        }
    }


}

