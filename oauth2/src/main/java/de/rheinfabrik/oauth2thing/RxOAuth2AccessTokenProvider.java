package de.rheinfabrik.oauth2thing;

import rx.Observable;

/**
 * Interface used to define how to generate and how to refresh an access token.
 *
 * @param <TAccessToken> The access token type.
 */
public interface RxOAuth2AccessTokenProvider<TAccessToken extends OAuth2AccessToken> {

    // Public API

    /**
     * This method generates a new access token.
     * The method signature is based to work with the Resource Owner
     * Password Credentials Grant method (https://tools.ietf.org/html/rfc6749#section-4.3).
     *
     * @param username The username to get the access token.
     * @param password The password to get the access token.
     * @param clientId The clientId to get the access token.
     * @return - An observable emitting the new access token.
     */
    public abstract Observable<TAccessToken> getNewAccessToken(String username, String password, String clientId);

    /**
     * This method is responsible to refresh (https://tools.ietf.org/html/rfc6749#section-6) the
     * access token based on the given refresh token.
     * The method signature is based to work with the Resource Owner
     * Password Credentials Grant method (https://tools.ietf.org/html/rfc6749#section-4.3).
     *
     * @param refreshToken The refresh token which will be used to query a new access token.
     * @return - An observable emitting the refreshed access token.
     */
    public abstract Observable<TAccessToken> refreshAccessToken(String refreshToken);
}
