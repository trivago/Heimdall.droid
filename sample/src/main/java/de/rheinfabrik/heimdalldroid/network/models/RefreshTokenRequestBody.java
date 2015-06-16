package de.rheinfabrik.heimdalldroid.network.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Body used to refresh an access token.
 */
public class RefreshTokenRequestBody implements Serializable {

    // Properties

    @SerializedName("refresh_token")
    public String refreshToken;

    @SerializedName("client_id")
    public String clientId;

    @SerializedName("client_secret")
    public String clientSecret;

    @SerializedName("redirect_uri")
    public String redirectUri;

    @SerializedName("grant_type")
    public String grantType;

    // Constructor

    public RefreshTokenRequestBody(String refreshToken, String clientId, String clientSecret, String redirectUri, String grantType) {
        super();

        this.refreshToken = refreshToken;
        this.clientId = clientId;
        this.clientSecret = clientSecret;
        this.redirectUri = redirectUri;
        this.grantType = grantType;
    }
}
