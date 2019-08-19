package com.lynkdriver.lynk.modules;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.lynkdriver.lynk.constant.AppConstants;


/**
 * Created by PRABHA R 19-08-2019
 */
public class TokenModel {
    @SerializedName(AppConstants.SharedConstants.AccessToken)
    @Expose
    public String accessToken;
    @SerializedName(AppConstants.SharedConstants.RefreshToken)
    @Expose
    public String refreshToken;
    @SerializedName("expires_in")
    @Expose
    public Integer expiresIn;
    @SerializedName("token_type")
    @Expose
    public String tokenType;
    @SerializedName("scope")
    @Expose
    public String scope;

}