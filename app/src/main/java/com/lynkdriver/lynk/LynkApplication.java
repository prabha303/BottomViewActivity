package com.lynkdriver.lynk;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.multidex.MultiDex;
import android.support.multidex.MultiDexApplication;
import android.util.Log;
import com.crashlytics.android.Crashlytics;
import com.lynkdriver.lynk.components.ApplicationComponent;
import com.lynkdriver.lynk.di.ActivityInjector;
import com.lynkdriver.lynk.utils.SharedPref;
import com.lynkdriver.lynk.utils.Utility;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.TimeZone;

import javax.inject.Inject;

import io.fabric.sdk.android.Fabric;


/**
 * Created by PRABHA R 19-08-2019
 */
public class LynkApplication extends MultiDexApplication {
//    @SuppressLint("StaticFieldLeak")
//    public static MixpanelAPI mixPannel = null;
    @SuppressLint("StaticFieldLeak")
    private static Context context = null;
    private String regIdGCM = null;
    String DeviceId = null;
    SharedPref sharedPref = null;
    private AsyncTask<Void, Void, Void> mRegisterTask;
    @SuppressLint("StaticFieldLeak")
    private static LynkApplication mInstance;

    ApplicationComponent applicationComponent;

    @Inject
    ActivityInjector activityInjector;
    private ArrayList<String> docList;


    @Override
    public void onCreate()
    {
        super.onCreate();
        context = getApplicationContext();
        try
        {



            sharedPref = SharedPref.getInstance();
            initiateAnalytics();


            initCrashlytics();
        }
        catch (Exception e)
        {
            Crashlytics.logException(e);
        }
    }

    public ApplicationComponent getApplicationComponent()
    {
        return applicationComponent;
    }

    public static LynkApplication getApplication(Activity activity){
        return (LynkApplication) activity.getApplication();
    }


    private void initCrashlytics() {

        installCrashHandler();
        startCrashlytics();
    }
    private void startCrashlytics() {
        //Crashlytics
        Fabric.with(this, new Crashlytics());
    }


    public static Context getAppContext() {
        return LynkApplication.context;
    }
    private void initiateAnalytics()
    {
        try
        {
            //QA
//             mixPannel = MixpanelAPI.getInstance(context, BuildConfig.mixPannelToken);
            //Live
            //mixPannel = MixpanelAPI.getInstance(context, "bff01e0a21f99785d03f31288f2a2a0a");
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    private void getUTCTime()
    {
        Date dateToReturn = null;
        try
        {
            final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
            final String utcTime = sdf.format(new Date());
            System.out.println("TIME "+utcTime);
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            dateToReturn = (Date)dateFormat.parse(utcTime);
            System.out.println("DATE obj "+dateToReturn);
        }
        catch (Exception e)
        {
          System.out.println("Exception getUTCTime "+e.getMessage());
        }
    }


    public ActivityInjector getActivityInjector(){
        return activityInjector;
    }

    public enum TrackerName {
        APP_TRACKER,
        GLOBAL_TRACKER,
        E_COMMERCE_TRACKER,
        EVENT_TRACKER
    }

    public static synchronized LynkApplication getInstance() {
        return mInstance;
    }
    @Override
    protected void attachBaseContext(Context base)
    {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    private void installCrashHandler()
    {
        //The real UncaughtExceptionHandler
        //final Thread.UncaughtExceptionHandler realExceptionHandler = Thread.getDefaultUncaughtExceptionHandler();

        Thread.setDefaultUncaughtExceptionHandler((thread, ex) -> {

            Log.e("Lynk_Emp","Inside uncaught exception..");
           /* if(ex instanceof CrashReporting.GenericReportingException) {
                //Starting this again because it is unregistered on the completion of Crashlytics UncaughtExceptionHandler
                startCrashlytics();
                return;
            }



            realExceptionHandler.uncaughtException(thread, ex);*/

            //Send Report to Crashlytics. Crashlytics will send it as soon as it starts to work
            Crashlytics.logException(ex);

            startCrashlytics();

            //Your custom codes to Restart the app or handle this crash
           // HandleCrashes(thread, ex);

        });
    }




}
