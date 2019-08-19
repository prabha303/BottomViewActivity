package com.lynkdriver.lynk.di;

import android.app.Activity;
import android.content.Context;

import com.lynkdriver.lynk.LynkApplication;

import java.util.Map;

import javax.inject.Inject;
import javax.inject.Provider;

import dagger.android.AndroidInjector;



/**
 * Created by PRABHA R 19-08-2019
 */
public class ActivityInjector {

    private final Map<Class<? extends Activity>, Provider<AndroidInjector.Factory<? extends Activity>>> activityInjectors;

    @Inject
    ActivityInjector(Map<Class<? extends Activity>, Provider<AndroidInjector.Factory<? extends Activity>>> activityInjectors) {
        this.activityInjectors = activityInjectors;
    }

    void inject(Activity activity)
    {
        AndroidInjector.Factory<Activity> injectorFactory =
                (AndroidInjector.Factory<Activity>) activityInjectors.get(activity.getClass()).get();
        AndroidInjector<Activity> injector = injectorFactory.create(activity);
        injector.inject(activity);
    }

    static ActivityInjector get(Context ctx){
        return  ((LynkApplication)ctx.getApplicationContext()).getActivityInjector();
    }
}
