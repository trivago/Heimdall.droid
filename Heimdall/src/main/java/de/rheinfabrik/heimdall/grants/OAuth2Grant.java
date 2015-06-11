package de.rheinfabrik.heimdall.grants;

import de.rheinfabrik.heimdall.OAuth2AccessToken;
import rx.Observable;

/**
 * Interface describing an OAuth2 Grant as described in https://tools.ietf.org/html/rfc6749#page-23.
 *
 * @param <TAccessToken> The access token type.
 */
public interface OAuth2Grant<TAccessToken extends OAuth2AccessToken> {

    // Abstract Api

    /**
     * Performs the actual request to grant a new access token.
     *
     * @return - An Observable emitting the granted access token.
     */
    Observable<TAccessToken> grantNewAccessToken();
}
