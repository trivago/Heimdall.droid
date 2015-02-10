package de.rheinfabrik.oauth2thing;

import java.io.Serializable;
import java.util.Calendar;

/**
 * OAuth2AccessToken is an interface representing an OAuth2AccessToken response (See https://tools.ietf.org/html/rfc6749#section-5.1 for reference).
 */
public interface OAuth2AccessToken extends Serializable {

    // Public API

    /**
     * REQUIRED (https://tools.ietf.org/html/rfc6749#section-5.1)
     * <p>
     * This method returns a valid access token string.
     *
     * @return - An access token string.
     */
    public String getAccessToken();


    /**
     * REQUIRED (https://tools.ietf.org/html/rfc6749#section-7.1)
     * <p>
     * This method returns a valid token type.
     *
     * @return - A token type string.
     */
    public String getTokenType();

    /**
     * OPTIONAL (https://tools.ietf.org/html/rfc6749#section-5.1)
     * Note: If the refresh token is null then the library will never automatically refresh the access token!
     *
     * @return - A refresh token string.
     */
    public String getRefreshToken();

    /**
     * OPTIONAL (https://tools.ietf.org/html/rfc6749#section-5.1)
     * Note: If the calendar is null then the library will never automatically refresh the access token!
     * <p>
     * This method returns an expiration date.
     *
     * @return - A calendar representing the expiration date.
     */
    public Calendar getExpirationDate();
}
