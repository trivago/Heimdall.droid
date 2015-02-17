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
    public String tokenType = null;

    /**
     * REQUIRED
     * The access token issued by the authorization server.
     */
    @SerializedName("access_token")
    public String accessToken = null;

    /**
     * OPTIONAL
     * The refresh token, which can be used to obtain new
     * access tokens using the same authorization grant as described
     * in https://tools.ietf.org/html/rfc6749#section-6.
     */
    @SerializedName("refresh_token")
    public String refreshToken = null;

    /**
     * RECOMMENDED
     * The lifetime in seconds of the access token.  For
     * example, the value "3600" denotes that the access token will
     * expire in one hour from the time the response was generated.
     * If omitted, the authorization server SHOULD provide the
     * expiration time via other means or document the default value.
     */
    @SerializedName("expires_in")
    public Integer expiresIn = null;

    /**
     * The expiration date used by Heimdall.
     */
    @SerializedName("heimdall_expiration_date")
    public Calendar expirationDate = null;

    // Public Api

    /**
     * This method returns whether the access token is expired or not.
     *
     * @return True if expired. Otherwise false.
     */
    public boolean isExpired() {
        return Calendar.getInstance().after(expirationDate);
    }

    // equals and hashCode

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof OAuth2AccessToken)) return false;

        OAuth2AccessToken that = (OAuth2AccessToken) o;

        if (accessToken != null ? !accessToken.equals(that.accessToken) : that.accessToken != null)
            return false;
        if (expirationDate != null ? !expirationDate.equals(that.expirationDate) : that.expirationDate != null)
            return false;
        if (expiresIn != null ? !expiresIn.equals(that.expiresIn) : that.expiresIn != null)
            return false;
        if (refreshToken != null ? !refreshToken.equals(that.refreshToken) : that.refreshToken != null)
            return false;
        if (tokenType != null ? !tokenType.equals(that.tokenType) : that.tokenType != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = tokenType != null ? tokenType.hashCode() : 0;
        result = 31 * result + (accessToken != null ? accessToken.hashCode() : 0);
        result = 31 * result + (refreshToken != null ? refreshToken.hashCode() : 0);
        result = 31 * result + (expiresIn != null ? expiresIn.hashCode() : 0);
        result = 31 * result + (expirationDate != null ? expirationDate.hashCode() : 0);

        return result;
    }
}
