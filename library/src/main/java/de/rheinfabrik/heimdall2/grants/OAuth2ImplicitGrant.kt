package de.rheinfabrik.heimdall2.grants

import de.rheinfabrik.heimdall2.OAuth2AccessToken

/**
 * Class representing the Implicit Grant as described in https://tools.ietf.org/html/rfc6749#section-4.2.
 *
 * @param <TAccessToken> The access token type.
 */

abstract class OAuth2ImplicitGrant<T : OAuth2AccessToken>(
    /**
     * REQUIRED
     * The client identifier as described in https://tools.ietf.org/html/rfc6749#section-2.2.
     */
    var clientId: String = "",

    /**
     * OPTIONAL
     * As described in https://tools.ietf.org/html/rfc6749#section-3.1.2.
     */
    var redirectUri: String? = null,

    /**
     * OPTIONAL
     * The scope of the access request as described in https://tools.ietf.org/html/rfc6749#section-3.3.
     */
    var scope: String? = null,

    /**
     * RECOMMENDED
     * An opaque value used by the client to maintain
     * state between the request and callback. The authorization
     * server includes this value when redirecting the user-agent back
     * to the client. The parameter SHOULD be used for preventing
     * cross-site request forgery as described in https://tools.ietf.org/html/rfc6749#section-10.12.
     */
    var state: String? = null

) : OAuth2Grant {

    // Constants

    /**
     * REQUIRED
     * The "response_type" which MUST be "token".
     */
    companion object {
        @JvmStatic
        val RESPONSE_TYPE = "token"
    }
}
