package de.rheinfabrik.heimdall2.rxjava

import de.rheinfabrik.heimdall2.model.OAuth2AccessToken
import io.reactivex.Single

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
     * @return - An Observable emitting the stored access token.
     */
    fun getStoredAccessToken(): Single<OAuth2AccessToken>

    /**
     * Stores the given access token.
     *
     * @param token The access token which will be stored.
     */
    fun storeAccessToken(token: OAuth2AccessToken)

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
