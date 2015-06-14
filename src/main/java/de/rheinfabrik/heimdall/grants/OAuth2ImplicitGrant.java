package de.rheinfabrik.heimdall.grants;

import de.rheinfabrik.heimdall.OAuth2AccessToken;

/**
 * Class representing the Implicit Grant as described in https://tools.ietf.org/html/rfc6749#section-4.2.
 *
 * @param <TAccessToken> The access token type.
 */
public abstract class OAuth2ImplicitGrant<TAccessToken extends OAuth2AccessToken> implements OAuth2Grant<TAccessToken> {

    // Constants

    /**
     * REQUIRED
     * The "response_type" which MUST be "token".
     */
    public final static String RESPONSE_TYPE = "token";

    // Properties

    /**
     * REQUIRED
     * The client identifier as described in https://tools.ietf.org/html/rfc6749#section-2.2.
     */
    public String clientId;

    /**
     * OPTIONAL
     * As described in https://tools.ietf.org/html/rfc6749#section-3.1.2.
     */
    public String redirectUri;

    /**
     * OPTIONAL
     * The scope of the access request as described in https://tools.ietf.org/html/rfc6749#section-3.3.
     */
    public String scope;

    /**
     * RECOMMENDED
     * An opaque value used by the client to maintain
     * state between the request and callback. The authorization
     * server includes this value when redirecting the user-agent back
     * to the client. The parameter SHOULD be used for preventing
     * cross-site request forgery as described in https://tools.ietf.org/html/rfc6749#section-10.12.
     */
    public String state;
}
