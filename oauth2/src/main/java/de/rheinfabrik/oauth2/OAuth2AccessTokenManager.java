package de.rheinfabrik.oauth2;

import java.util.Calendar;

import rx.Observable;

/**
 * This class is used to manage one access token bound to one client identifier.
 * Features:
 * * Generate and store a new access token based on a username and password.
 * * Check whether there is a stored access token.
 * * Ensure that there is a valid access token by refreshing it if it is expired.
 *
 * @param <TAccessToken> The access token type.
 */
public class OAuth2AccessTokenManager<TAccessToken extends OAuth2AccessToken> {

    // Members

    private final OAuth2AccessTokenStorage<TAccessToken> mStorage;
    private final OAuth2AccessTokenProvider<TAccessToken> mProvider;

    // Constructor

    /**
     * The designated constructor.
     *
     * @param storage  The storage used to store/read the access token.
     * @param provider The provider used to generate or refresh access token.
     */
    public OAuth2AccessTokenManager(OAuth2AccessTokenStorage<TAccessToken> storage, OAuth2AccessTokenProvider<TAccessToken> provider) {
        super();

        mStorage = storage;
        mProvider = provider;
    }

    // Public API

    /**
     * Returns an observable that either emits true if there is an access token or false if there is not.
     *
     * @return - A TToken Observable emitting a boolean representing whether there is or is not an access token.
     */
    public Observable<Boolean> hasAccessToken() {
        return mStorage.hasAccessToken();
    }

    /**
     * Returns an observable that emits the new access token.
     *
     * @param username The username used to get the access token.
     * @param password The password used to get the access token.
     * @return - A TToken Observable emitting the new access token.
     */
    public Observable<TAccessToken> getNewAccessToken(String username, String password) {
        return Observable.create(subscriber -> mProvider.getNewAccessToken(username, password)
                .subscribe(newToken -> {
                    newToken.generateExpirationDate(Calendar.getInstance());
                    mStorage.storeAccessToken(newToken);

                    subscriber.onNext(newToken);
                    subscriber.onCompleted();
                }, throwable -> {
                    subscriber.onError(throwable);
                    subscriber.onCompleted();
                }));
    }

    /**
     * Returns an observable that emits a valid (non-expired) access token. This method refreshes
     * the access token if it is expired.
     *
     * @return - A TToken Observable emitting a valid access token.
     */
    public Observable<TAccessToken> getValidAccessToken() {
        return Observable.create(subscriber -> {
            hasAccessToken()
                    .concatMap(hasToken -> {
                        if (hasToken) {
                            return mStorage.getStoredAccessToken();
                        }

                        return Observable.error(new RuntimeException("No access token found."));
                    })
                    .concatMap(accessToken -> {
                        if (accessToken.getRefreshToken() == null || accessToken.getExpirationDate() == null || Calendar.getInstance().before(accessToken.getExpirationDate())) {
                            return Observable.just(accessToken);
                        }

                        return mProvider
                                .refreshAccessToken(accessToken.getRefreshToken())
                                .doOnNext(refreshedAccessToken -> {
                                    refreshedAccessToken.generateExpirationDate(Calendar.getInstance());
                                    mStorage.storeAccessToken(refreshedAccessToken);
                                });
                    })
                    .subscribe(accessToken -> {
                        subscriber.onNext(accessToken);
                        subscriber.onCompleted();
                    }, throwable -> {
                        subscriber.onError(throwable);
                        subscriber.onCompleted();
                    });
        });
    }
}
