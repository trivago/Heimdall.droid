package de.rheinfabrik.heimdall;

import java.util.Calendar;

import de.rheinfabrik.heimdall.grants.OAuth2Grant;
import de.rheinfabrik.heimdall.grants.OAuth2RefreshAccessTokenGrant;
import rx.Observable;

import static rx.Observable.error;
import static rx.Observable.just;

/**
 * The all-seeing and all-hearing guardian sentry of your application who
 * stands on the rainbow bridge to handle all your access tokens needs!
 *
 * @param <TAccessToken> The token type.
 */
public class OAuth2AccessTokenManager<TAccessToken extends OAuth2AccessToken> {

    // Members

    private final OAuth2AccessTokenStorage<TAccessToken> mStorage;

    // Constructor

    /**
     * The designated constructor.
     *
     * @param storage The OAuth2AccessTokenStorage used to store and retrieve the access token.
     */
    public OAuth2AccessTokenManager(OAuth2AccessTokenStorage<TAccessToken> storage) {
        super();

        if (storage == null) {
            throw new IllegalArgumentException("Storage MUST NOT be null.");
        }

        mStorage = storage;
    }

    // Public API

    /**
     * Returns the underlying storage.
     *
     * @return - An OAuth2AccessTokenStorage.
     */
    public OAuth2AccessTokenStorage<TAccessToken> getStorage() {
        return mStorage;
    }

    /**
     * Grants a new access token using the given OAuth2 grant.
     *
     * @param grant A class implementing the OAuth2Grant interface.
     * @return - An observable emitting the granted access token.
     */
    public Observable<TAccessToken> grantNewAccessToken(OAuth2Grant<TAccessToken> grant) {
        if (grant == null) {
            throw new IllegalArgumentException("Grant MUST NOT be null.");
        }

        return grant
                .grantNewAccessToken()
                .doOnNext(accessToken -> {
                    Calendar expirationDate = Calendar.getInstance();
                    expirationDate.add(Calendar.SECOND, accessToken.expiresIn);
                    accessToken.expirationDate = expirationDate;

                    mStorage.storeAccessToken(accessToken);
                });
    }

    /**
     * Returns an Observable emitting an unexpired access token.
     * NOTE: In order to work the library needs an access token which has a refresh_token and an
     * expires_in field.
     *
     * @param refreshAccessTokenGrant The refresh grant that will be used if the access token is expired.
     * @return - An Observable emitting an unexpired access token.
     */
    public Observable<TAccessToken> getValidAccessToken(final OAuth2RefreshAccessTokenGrant<TAccessToken> refreshAccessTokenGrant) {
        if (refreshAccessTokenGrant == null) {
            throw new IllegalArgumentException("RefreshAccessTokenGrant MUST NOT be null.");
        }

        return mStorage.getStoredAccessToken()
                .concatMap(accessToken -> {
                    if (accessToken == null) {
                        return error(new IllegalStateException("No access token found."));
                    } else if (accessToken.isExpired()) {
                        refreshAccessTokenGrant.refreshToken = accessToken.refreshToken;

                        return grantNewAccessToken(refreshAccessTokenGrant);
                    } else {
                        return just(accessToken);
                    }
                });
    }

}
