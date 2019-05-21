package de.rheinfabrik.heimdall2.grants

import de.rheinfabrik.heimdall2.OAuth2AccessToken

abstract class OAuth2ResourceOwnerPasswordCredentialsGrant<TAccessToken : OAuth2AccessToken>(
    /**
     * REQUIRED
     * The resource owner "username".
     */
    var username: String? = null,

    /**
     * REQUIRED
     * The resource owner "password".
     */
    var password: String? = null,

    /**
     * OPTIONAL
     * The "scope" of the access request as described by here (https://tools.ietf.org/html/rfc6749#section-3.3).
     */
    var scope: String? = null
) : OAuth2Grant<TAccessToken> {

    companion object {
        @JvmStatic
        val GRANT_TYPE = "password"
    }
}
