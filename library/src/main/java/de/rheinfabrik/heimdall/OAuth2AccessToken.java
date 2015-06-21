package de.rheinfabrik.heimdall;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.Calendar;

/**
 * OAuth2AccessToken represents an OAuth2AccessToken response as described in https://tools.ietf.org/html/rfc6749#section-5.1.
 */
public class OAuth2AccessToken implements Serializable {

    // Properties

    /**
     * REQUIRED
     * The type of the token issued as described in https://tools.ietf.org/html/rfc6749#section-7.1.
     * Value is case insensitive.
     */
    @SerializedName("token_type")
    public String tokenType;

    /**
     * REQUIRED
     * The access token issued by the authorization server.
     */
    @SerializedName("access_token")
    public String accessToken;

    /**
     * OPTIONAL
     * The refresh token, which can be used to obtain new
     * access tokens using the same authorization grant as described
     * in https://tools.ietf.org/html/rfc6749#section-6.
     */
    @SerializedName("refresh_token")
    public String refreshToken;

    /**
     * RECOMMENDED
     * The lifetime in seconds of the access token.  For
     * example, the value "3600" denotes that the access token will
     * expire in one hour from the time the response was generated.
     * If omitted, the authorization server SHOULD provide the
     * expiration time via other means or document the default value.
     */
    @SerializedName("expires_in")
    public Integer expiresIn;

    /**
     * The expiration date used by Heimdall.
     */
    @SerializedName("heimdall_expiration_date")
    public Calendar expirationDate;

    // Public Api

    /**
     * This method returns whether the access token is expired or not.
     *
     * @return True if expired. Otherwise false.
     */
    public boolean isExpired() {
        return expirationDate != null && Calendar.getInstance().after(expirationDate);
    }

    // Object

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }

        if (!(other instanceof OAuth2AccessToken)) {
            return false;
        }

        OAuth2AccessToken otherToken = (OAuth2AccessToken) other;

        return accessToken.equals(otherToken.accessToken) && tokenType.equals(otherToken.tokenType);
    }

    @Override
    public int hashCode() {
        int result = tokenType.hashCode();
        result = 31 * result + accessToken.hashCode();

        return result;
    }
}
