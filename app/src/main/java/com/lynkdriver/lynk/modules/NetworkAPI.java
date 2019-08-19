package com.lynkdriver.lynk.modules;

import android.util.Base64;
import android.util.Log;
import com.lynkdriver.lynk.BuildConfig;
import com.lynkdriver.lynk.LynkApplication;
import com.lynkdriver.lynk.constant.AppConstants;
import com.lynkdriver.lynk.network.NetworkRetryInterceptor;
import com.lynkdriver.lynk.network.RetrofitEndPoint;
import com.lynkdriver.lynk.utils.SharedPref;
import com.lynkdriver.lynk.utils.Utility;

import java.io.UnsupportedEncodingException;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import javax.inject.Singleton;
import dagger.Module;
import dagger.Provides;
import io.reactivex.schedulers.Schedulers;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;



/**
 * Created by PRABHA R 19-08-2019
 */
@Module
public class NetworkAPI
{

    private static NetworkAPI instance;
    private Retrofit mRetrofitToken;


    public static NetworkAPI getInstance() {
        instance = new NetworkAPI();
        return instance;
    }


    @Provides
    @Singleton
    static Retrofit getRetrofit() {

        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient.Builder httpClient = getOkHttpClient().addInterceptor(interceptor);
        httpClient.connectTimeout(180, TimeUnit.SECONDS);
        httpClient.readTimeout(180, TimeUnit.SECONDS);
        httpClient.writeTimeout(180, TimeUnit.SECONDS);
        httpClient.addInterceptor(new NetworkRetryInterceptor().setContext(LynkApplication.getAppContext()));
        httpClient.addInterceptor(chain -> {
            Request original = chain.request();
            Request request = original.newBuilder().method(original.method(), original.body()).build();
            okhttp3.Response response =  chain.proceed(request);
            if(response.code() == 401){
                String userId = SharedPref.getStringValue(LynkApplication.getAppContext(),"user_id");
                String refreshToken = SharedPref.getStringValue(LynkApplication.getAppContext(),AppConstants.SharedConstants.RefreshToken);
               if(refreshToken!=null && !refreshToken.equalsIgnoreCase("")) {
                   retrofit2.Response<TokenModel> authResponse = NetworkAPI.getInstance().tokenRetrofit(BuildConfig.APPLICATION_ID).create(RetrofitEndPoint.class).getAccesstoken(getAuthToken(), refreshToken).execute();
                   if (authResponse.isSuccessful()) {
                       SharedPref.getInstance().setSharedValue(LynkApplication.getAppContext(), AppConstants.SharedConstants.RefreshToken, authResponse.body().refreshToken);
                       SharedPref.getInstance().setSharedValue(LynkApplication.getAppContext(), AppConstants.SharedConstants.AccessToken, authResponse.body().accessToken);

                       // New Retrofit Auth token API call should be made and update the token
                       Request.Builder newRequest = original.newBuilder()
                               .header("user_id", userId)
                               .header("Authorization", "Bearer " + authResponse.body().accessToken)
                               .method(original.method(), original.body());
                       response = chain.proceed(newRequest.build());

                       return response;
                   } else {
                       Utility.resetPreferredLauncherAndOpenChooser(LynkApplication.getAppContext());
                       //cleanDriver();
                   }
               }
            }
            return response;

        });

        return new Retrofit.Builder()
                .baseUrl(BuildConfig.APPLICATION_ID)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io()))
                .client(httpClient.build())
                .build();

    }

    public static String getAuthToken() {
        byte[] data = new byte[0];
        try {
            data = (BuildConfig.APPLICATION_ID+":"+BuildConfig.APPLICATION_ID).getBytes("UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return "Basic " + Base64.encodeToString(data, Base64.NO_WRAP);
    }

    public Retrofit tokenRetrofit(String endPointURL) {
        if (mRetrofitToken == null) {
            HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
            if (BuildConfig.DEBUG) {
                interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            } else {
                interceptor.setLevel(HttpLoggingInterceptor.Level.NONE);
            }
            mRetrofitToken = new Retrofit.Builder()
                    .baseUrl(endPointURL)
                    .client(getOkHttpTokenClient().addInterceptor(interceptor).connectTimeout(120, TimeUnit.SECONDS)
                            .readTimeout(120, TimeUnit.SECONDS)
                            .writeTimeout(120, TimeUnit.SECONDS).build())
                    .addConverterFactory(GsonConverterFactory.create()).build();
        }
        return mRetrofitToken;
    }



    public static OkHttpClient.Builder getOkHttpTokenClient() {
        try {

            OkHttpClient.Builder builder = new OkHttpClient.Builder();
            builder.addInterceptor(chain -> {
                Request original = chain.request();
                Request request;
                request = original.newBuilder()
                        .method(original.method(), original.body())
                        .build();

                return chain.proceed(request);
            });
            return builder;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    @Provides
    @Singleton
    static RetrofitEndPoint provideEndPoints(Retrofit retrofit)
    {
        return retrofit.create(RetrofitEndPoint.class);
    }



    public static OkHttpClient.Builder getOkHttpClient() {
        try {
            OkHttpClient.Builder builder = new OkHttpClient.Builder();
            builder.addInterceptor(chain -> {
                Request original = chain.request();
                Request request;
                String userId = SharedPref.getStringValue(LynkApplication.getAppContext(),"user_id");
                String AccessToken = SharedPref.getStringValue(LynkApplication.getAppContext(), AppConstants.SharedConstants.AccessToken);
                Log.d("AccessToken_prabha","-- user_id "+userId +" ,AccessToken-  "+AccessToken);
                if(userId!=null && !userId.equalsIgnoreCase("") && AccessToken!=null && !AccessToken.equalsIgnoreCase("")) {
                    request = original.newBuilder()
                            .header("user_id", userId)
                            .header("Authorization", "Bearer " + AccessToken)
                            .method(original.method(), original.body())
                            .build();
                }
                else {
                    request = original.newBuilder()
                            .header("reference_id", UUID.randomUUID()+"-"+System.currentTimeMillis())
                            .method(original.method(), original.body())
                            .build();
                }
                return chain.proceed(request);
            });
            return builder;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


}
