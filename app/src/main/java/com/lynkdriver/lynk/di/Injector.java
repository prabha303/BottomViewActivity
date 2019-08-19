package com.lynkdriver.lynk.di;

import android.app.Activity;


/**
 * Created by PRABHA R 19-08-2019
 */
public class Injector
{
    public static void inject(Activity activity)
    {
        ActivityInjector.get(activity).inject(activity);
    }
}
