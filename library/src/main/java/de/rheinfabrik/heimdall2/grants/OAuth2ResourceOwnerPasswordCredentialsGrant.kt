package de.rheinfabrik.heimdall2.grants

abstract class OAuth2ResourceOwnerPasswordCredentialsGrant(
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
) : OAuth2Grant {

    companion object {
        @JvmField
        val GRANT_TYPE = "password"
    }
}
