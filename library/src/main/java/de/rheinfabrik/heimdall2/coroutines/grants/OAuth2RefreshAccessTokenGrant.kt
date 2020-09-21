package de.rheinfabrik.heimdall2.coroutines.grants

abstract class OAuth2RefreshAccessTokenGrant(
    /**
     * REQUIRED
     * The "refresh_token" issued to the client.
     */
    var refreshToken: String? = null,

    /**
     * OPTIONAL
     * The "scope" of the access request as described by here (https://tools.ietf.org/html/rfc6749#section-3.3).
     */
    var scope: String? = null
) :
    OAuth2Grant {
    // Constants

    /**
     * REQUIRED
     * The OAuth2 "grant_type".
     */
    companion object {
        @JvmField
        val GRANT_TYPE = "refresh_token"
    }
}
