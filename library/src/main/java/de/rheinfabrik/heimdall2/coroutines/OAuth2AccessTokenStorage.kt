package de.rheinfabrik.heimdall2.coroutines

import de.rheinfabrik.heimdall2.accesstoken.OAuth2AccessToken

/**
 * Interface used to define how to store and retrieve a stored access token.
 *
 * @param <TAccessToken> The access token type.
 */
interface OAuth2AccessTokenStorage {

    // Public API

    /**
     * Queries the stored access token.
     *
     * @return - An stored access token.
     */
    suspend fun getStoredAccessToken(): OAuth2AccessToken

    /**
     * Stores the given access token.
     *
     * @param token The access token which will be stored.
     */
    suspend fun storeAccessToken(token: OAuth2AccessToken)

    /**
     * Checks whether there is or is not an access token
     *
     * @return - An Boolean based on whether there is or is not an access token.
     */
    suspend fun hasAccessToken(): Boolean

    /**
     * Removes the stored access token.
     */
    suspend fun removeAccessToken()
}
