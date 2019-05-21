package de.rheinfabrik.heimdall2.grants

import de.rheinfabrik.heimdall2.OAuth2AccessToken

abstract class OAuth2ClientCredentialsGrant<T : OAuth2AccessToken>(
    /**
     * OPTIONAL
     * The scope of the access request as described in https://tools.ietf.org/html/rfc6749#section-3.3.
     */
    val scope: String? = null
) :
    OAuth2Grant<T> {

    // Constants

    companion object {
        /**
         * REQUIRED
         * The OAuth2 "grant_type".
         */
        @JvmStatic
        val GRANT_TYPE = "client_credentials"
    }
}
