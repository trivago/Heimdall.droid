package de.rheinfabrik.oauth2;

import rx.Observable;

/**
 * Interface used to define how to store and retrieve a stored access token.
 *
 * @param <TAccessToken> The access token type.
 */
public interface OAuth2AccessTokenStorage<TAccessToken extends OAuth2AccessToken> {

    // Public API

    /**
     * Checks whether there is or is not an access token.
     *
     * @return - An Observable emitting a boolean representing whether there is or is not an access token.
     */
    public Observable<Boolean> hasAccessToken();

    /**
     * Queries the stored access token.
     *
     * @return - An Observable emitting the stored access token.
     */
    public Observable<TAccessToken> getStoredAccessToken();

    /**
     * Stores the given access token.
     *
     * @param token The access token which will be stored.
     */
    public void storeAccessToken(TAccessToken token);
}
