package de.rheinfabrik.heimdall2.coroutines.grants

import de.rheinfabrik.heimdall2.accesstoken.OAuth2AccessToken

/**
 * Interface describing an OAuth2 Grant as described in https://tools.ietf.org/html/rfc6749#page-23.
 *
 * @param <TAccessToken> The access token type.
 */
interface OAuth2Grant {

    // Abstract Api

    /**
     * Performs the actual request to grant a new access token.
     *
     * @return - The granted access token.
     */
    suspend fun grantNewAccessToken(): OAuth2AccessToken
}
