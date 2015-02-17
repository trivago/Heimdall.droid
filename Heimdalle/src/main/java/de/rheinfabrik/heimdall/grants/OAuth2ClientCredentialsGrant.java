package de.rheinfabrik.heimdall.grants;

import de.rheinfabrik.heimdall.OAuth2AccessToken;

/**
 * Class representing the Client Credentials Grant as described in https://tools.ietf.org/html/rfc6749#section-4.4.
 *
 * @param <TAccessToken> The access token type.
 */
public abstract class OAuth2ClientCredentialsGrant<TAccessToken extends OAuth2AccessToken> implements OAuth2Grant<TAccessToken> {

    // Constants

    /**
     * REQUIRED
     * The OAuth2 "grant_type".
     */
    public static final String GRANT_TYPE = "client_credentials";

    // Properties

    /**
     * OPTIONAL
     * The scope of the access request as described in https://tools.ietf.org/html/rfc6749#section-3.3.
     */
    public String scope;
}
