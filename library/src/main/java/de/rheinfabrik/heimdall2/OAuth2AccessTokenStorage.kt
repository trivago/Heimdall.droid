package de.rheinfabrik.heimdall2

import io.reactivex.Single

/**
 * Interface used to define how to store and retrieve a stored access token.
 *
 * @param <TAccessToken> The access token type.
 */
interface OAuth2AccessTokenStorage<T : OAuth2AccessToken> {
    // Public API

    /**
     * Queries the stored access token.
     *
     * @return - An Observable emitting the stored access token.
     */
    fun getStoredAccessToken(): Single<T>

    /**
     * Stores the given access token.
     *
     * @param token The access token which will be stored.
     */
    fun storeAccessToken(token: T)

    /**
     * Checks whether there is or is not an access token
     *
     * @return - An Observable emitting true or false based on whether there is or is not an
     * access token.
     */
    fun hasAccessToken(): Single<Boolean>

    /**
     * Removes the stored access token.
     */
    fun removeAccessToken()
}
