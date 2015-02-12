package de.rheinfabrik.oauth2.implementation;

import com.google.gson.annotations.SerializedName;

import java.util.Calendar;

import de.rheinfabrik.oauth2.OAuth2AccessToken;

/**
 * Standard implementation for an AccessTokenResponse as described in https://tools.ietf.org/html/rfc6749#section-5.1.
 */
public class StandardOAuth2AccessToken implements OAuth2AccessToken {

    // Members

    @SerializedName("expires_in")
    private int mExpiresIn;

    @SerializedName("token_type")
    private String mTokenType;

    @SerializedName("access_token")
    private String mAccessToken;

    @SerializedName("refresh_token")
    private String mRefreshToken;

    @SerializedName("expiration_date")
    private Calendar mExpirationDate;

    // Public API

    /**
     * Returns the lifetime of the access token in seconds.
     *
     * @return - An int representing the life time of the access token.
     */
    public int getExpiresIn() {
        return mExpiresIn;
    }

    // OAuth2AccessToken

    @Override
    public void generateExpirationDate(Calendar now) {
        Calendar clone = (Calendar) now.clone();
        clone.add(Calendar.SECOND, getExpiresIn());

        mExpirationDate = clone;
    }

    @Override
    public String getAccessToken() {
        return mAccessToken;
    }

    @Override
    public String getTokenType() {
        return mTokenType;
    }

    @Override
    public String getRefreshToken() {
        return mRefreshToken;
    }

    @Override
    public Calendar getExpirationDate() {
        return mExpirationDate;
    }
}
