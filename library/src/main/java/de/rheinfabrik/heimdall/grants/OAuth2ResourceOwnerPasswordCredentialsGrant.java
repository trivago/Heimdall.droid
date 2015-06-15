package de.rheinfabrik.heimdall.grants;

import de.rheinfabrik.heimdall.OAuth2AccessToken;

/**
 * Class representing the Resource Owner Password Credentials Grant as described in https://tools.ietf.org/html/rfc6749#section-4.3.
 *
 * @param <TAccessToken> The access token type.
 */
public abstract class OAuth2ResourceOwnerPasswordCredentialsGrant<TAccessToken extends OAuth2AccessToken> implements OAuth2Grant<TAccessToken> {

    // Constants

    /**
     * REQUIRED
     * The OAuth2 "grant_type".
     */
    public static final String GRANT_TYPE = "password";

    // Properties

    /**
     * REQUIRED
     * The resource owner "username".
     */
    public String username;

    /**
     * REQUIRED
     * The resource owner "password".
     */
    public String password;

    /**
     * OPTIONAL
     * The "scope" of the access request as described by here (https://tools.ietf.org/html/rfc6749#section-3.3).
     */
    public String scope;

}
