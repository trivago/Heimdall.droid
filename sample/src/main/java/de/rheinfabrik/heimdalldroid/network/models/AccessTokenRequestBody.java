package de.rheinfabrik.heimdalldroid.network.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Body object used to exchange a code with an access token.
 */
public class AccessTokenRequestBody implements Serializable {

    // Properties

    @SerializedName("code")
    public String code;

    @SerializedName("client_id")
    public String clientId;

    @SerializedName("client_secret")
    public String clientSecret;

    @SerializedName("redirect_uri")
    public String redirectUri;

    @SerializedName("grant_type")
    public String grantType;

    // Constructor

    public AccessTokenRequestBody(String code, String clientId, String redirectUri, String clientSecret, String grantType) {
        super();

        this.code = code;
        this.clientId = clientId;
        this.redirectUri = redirectUri;
        this.clientSecret = clientSecret;
        this.grantType = grantType;
    }
}
