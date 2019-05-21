package de.rheinfabrik.heimdall2;

import de.rheinfabrik.heimdall2.grants.OAuth2Grant;
import de.rheinfabrik.heimdall2.grants.OAuth2RefreshAccessTokenGrant;
import io.reactivex.Single;
import java.util.Calendar;

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
    public Single<TAccessToken> grantNewAccessToken(OAuth2Grant<TAccessToken> grant) {
        return grantNewAccessToken(grant, Calendar.getInstance());
    }

    /**
     * Grants a new access token using the given OAuth2 grant.
     *
     * @param grant    A class implementing the OAuth2Grant interface.
     * @param calendar A calendar instance used to calculate the expiration date of the token.
     * @return - An observable emitting the granted access token.
     */
    public Single<TAccessToken> grantNewAccessToken(OAuth2Grant<TAccessToken> grant, Calendar calendar) {
        if (grant == null) {
            throw new IllegalArgumentException("Grant MUST NOT be null.");
        }

        return grant.grantNewAccessToken()
                .doOnSuccess(accessToken -> {
                    if (accessToken.getExpiresIn() != null) {
                        Calendar expirationDate = (Calendar) calendar.clone();
                        expirationDate.add(Calendar.SECOND, accessToken.getExpiresIn());
                        accessToken.setExpirationDate(expirationDate);
                    }
                    mStorage.storeAccessToken(accessToken);
                }).cache();
    }

    /**
     * Returns an Observable emitting an unexpired access token.
     * NOTE: In order to work, Heimdall needs an access token which has a refresh_token and an
     * expires_in field.
     *
     * @param refreshAccessTokenGrant The refresh grant that will be used if the access token is expired.
     * @return - An Observable emitting an unexpired access token.
     */
    public Single<TAccessToken> getValidAccessToken(final OAuth2RefreshAccessTokenGrant<TAccessToken> refreshAccessTokenGrant) {
        if (refreshAccessTokenGrant == null) {
            throw new IllegalArgumentException("RefreshAccessTokenGrant MUST NOT be null.");
        }

        return mStorage.getStoredAccessToken()
                .flatMap(accessToken -> {
                    if (accessToken == null) {
                        return Single.error(new IllegalStateException("No access token found."));
                    } else if (accessToken.isExpired()) {
                        refreshAccessTokenGrant.setRefreshToken(accessToken.getRefreshToken());

                        return grantNewAccessToken(refreshAccessTokenGrant);
                    } else {
                        return Single.just(accessToken);
                    }
                });
    }
}
