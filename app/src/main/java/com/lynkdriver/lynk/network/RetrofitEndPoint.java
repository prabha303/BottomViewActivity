package com.lynkdriver.lynk.network;


import com.lynkdriver.lynk.BuildConfig;
import com.lynkdriver.lynk.constant.AppConstants;
import com.lynkdriver.lynk.modules.TokenModel;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Path;


/**
 * Created by PRABHA R 19-08-2019
 */
public interface RetrofitEndPoint {



    @GET(BuildConfig.APPLICATION_ID+"v1/token")
    Call<TokenModel> getAccesstoken (@Header("Authorization") String authkey, @Header(AppConstants.SharedConstants.RefreshToken) String RefreshToken);


}

