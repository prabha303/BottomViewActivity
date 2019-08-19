package com.lynkdriver.lynk.components;

import com.lynkdriver.lynk.modules.NetworkAPI;

import javax.inject.Singleton;

import dagger.Component;



/**
 * Created by PRABHA R 19-08-2019
 */

@Singleton
@Component(modules = {NetworkAPI.class, })
public interface ApplicationComponent {




    //void injectTripRating(TripRating tripRating);
}
