package de.rheinfabrik.heimdall2.grants;

import de.rheinfabrik.heimdall2.OAuth2AccessToken;

/**
 * Class representing the Refreshing Access Token Grant as described in https://tools.ietf.org/html/rfc6749#section-6.
 *
 * @param <TAccessToken> The access token type.
 */
public abstract class OAuth2RefreshAccessTokenGrant<TAccessToken extends OAuth2AccessToken> implements OAuth2Grant<TAccessToken> {

    // Constants

    /**
     * REQUIRED
     * The OAuth2 "grant_type".
     */
    public static final String GRANT_TYPE = "refresh_token";

    // Properties

    /**
     * REQUIRED
     * The "refresh_token" issued to the client.
     */
    public String refreshToken;

    /**
     * OPTIONAL
     * The "scope" of the access request as described by here (https://tools.ietf.org/html/rfc6749#section-3.3).
     */
    public String scope;

}
