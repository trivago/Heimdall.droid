package de.rheinfabrik.heimdall2.rxjava.grants

abstract class OAuth2ClientCredentialsGrant(
    /**
     * OPTIONAL
     * The scope of the access request as described in https://tools.ietf.org/html/rfc6749#section-3.3.
     */
    val scope: String? = null
) :
    OAuth2Grant {

    // Constants

    companion object {
        /**
         * REQUIRED
         * The OAuth2 "grant_type".
         */
        @JvmField
        val GRANT_TYPE = "client_credentials"
    }
}
