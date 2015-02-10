package de.rheinfabrik.oauth2thing.tests;

import com.google.gson.annotations.SerializedName;

import java.util.Calendar;

import de.rheinfabrik.oauth2thing.OAuth2AccessToken;

public class DummyOAuth2AccessToken implements OAuth2AccessToken {

    // Properties

    @SerializedName("something")
    public String something;

    @SerializedName("refreshToken")
    public String refreshToken;

    @SerializedName("expirationDate")
    public Calendar expirationDate;

    // OAuth2AccessToken

    @Override
    public String getAccessToken() {
        return "such access much wow";
    }

    @Override
    public String getTokenType() {
        return "bearer";
    }

    @Override
    public String getRefreshToken() {
        return refreshToken;
    }

    @Override
    public Calendar getExpirationDate() {
        return expirationDate;
    }

    // Equals and Hash

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (!(o instanceof DummyOAuth2AccessToken)) {
            return false;
        }

        DummyOAuth2AccessToken that = (DummyOAuth2AccessToken) o;

        if (expirationDate != null ? !expirationDate.equals(that.expirationDate) : that.expirationDate != null) {
            return false;
        }

        if (refreshToken != null ? !refreshToken.equals(that.refreshToken) : that.refreshToken != null) {
            return false;
        }

        if (something != null ? !something.equals(that.something) : that.something != null) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        int result = something != null ? something.hashCode() : 0;
        result = 31 * result + (refreshToken != null ? refreshToken.hashCode() : 0);
        result = 31 * result + (expirationDate != null ? expirationDate.hashCode() : 0);

        return result;
    }
}
