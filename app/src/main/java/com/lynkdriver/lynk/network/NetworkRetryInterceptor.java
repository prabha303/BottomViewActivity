package com.lynkdriver.lynk.network;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import java.io.IOException;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;


/**
 * Created by PRABHA R 19-08-2019
 */

public class NetworkRetryInterceptor implements Interceptor {
    public NetworkRetryInterceptor setMintryCount(int mintryCount) {
        if(mintryCount>0)
             this.mintryCount = mintryCount;
        return this;
    }

    public NetworkRetryInterceptor setMaxtryCount(int maxtryCount) {
        if(maxtryCount>0 && maxtryCount>mintryCount)
            this.maxtryCount = maxtryCount;
        return this;
    }

    public NetworkRetryInterceptor setBaseDelay(int baseDelay) {
        if(baseDelay>0)
            this.baseDelay = baseDelay;
        return this;
    }


    private int mintryCount =1;
    private int maxtryCount =3;
    private int baseDelay =1000;
    private int retrycount =1;
    private Context mContext;

    public NetworkRetryInterceptor setEnableRetry(boolean enableRetry) {
        this.enableRetry = enableRetry;
        return this;
    }

    private boolean enableRetry =false;

    public NetworkRetryInterceptor setDelayRate(int delayRate) {
        if(delayRate>0)
            this.delayRate = delayRate;
        return this;
    }
    public NetworkRetryInterceptor setContext(Context mContext) {
        this.mContext=mContext;
        return this;
    }

    private int delayRate = 2;
    @Override
    public Response intercept(Chain chain) throws IOException {
        Request original = chain.request();
        Request request = original.newBuilder().method(original.method(), original.body()).build();
        Response response =  chain.proceed(request);
//        String AccessToken = SharedPref.getInstance().getStringVlue(LynkApplication.getAppContext(), LynkApplication.getAppContext().getResources().getString(R.string.AccessToken));
//        String userId = SharedPref.getInstance().getStringVlue(LynkApplication.getAppContext(), LynkApplication.getAppContext().getResources().getString(R.string.UserId));
//        String customerId       = SharedPref.getInstance().getStringVlue(LynkApplication.getAppContext(), LynkApplication.getAppContext().getString(R.string.CustomerId));
        Log.e("OkHttp","response intercepted");
        if(response.code() >= 500 && response.code() <= 600  /*&& enableRetry*/){
                Log.e("OkHttp","send broadcast"+response.code()+"---"+request.url());
                Intent intent = new Intent("InternalError");
                intent.putExtra("error_code", response.code());
                intent.putExtra("req_url", request.url().toString());
                LocalBroadcastManager.getInstance(mContext).sendBroadcast(intent);

           /* while (!response.isSuccessful() && retrycount <=maxtryCount) {

                Log.d("intercept", "Request is not successful - " + retrycount);
                Log.d("intercept delay", "Requesting in " + Math.round(Math.pow(delayRate, retrycount)) * baseDelay);

                retrycount++;
            //Your custom codes to Restart the app or handle this crash
           // HandleCrashes(thread, ex);


                // retry the request
                try {
                    Thread.sleep(Math.round(Math.pow(delayRate, retrycount)) * baseDelay);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Request.Builder newRequest = original.newBuilder()
                        .header("session_reference", customerId)
                        .header("reference_id", UUID.randomUUID()+"-"+System.currentTimeMillis())
                        .header("user_id", userId)
                        .header("Authorization", "Bearer " + AccessToken)
                        .method(original.method(), original.body());

                response = chain.proceed(original);
                //response = chain.proceed(request);
            }*/


        }
        return response;
    }


}
