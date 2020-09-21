package de.rheinfabrik.heimdalldroid.network.oauth2;

import android.content.Context;
import android.content.SharedPreferences;

import de.rheinfabrik.heimdall2.model.OAuth2AccessToken;
import de.rheinfabrik.heimdall2.rxjava.OAuth2AccessTokenManager;
import de.rheinfabrik.heimdall2.rxjava.OAuth2AccessTokenStorage;
import de.rheinfabrik.heimdalldroid.TraktTvAPIConfiguration;
import de.rheinfabrik.heimdalldroid.network.TraktTvApiFactory;
import de.rheinfabrik.heimdalldroid.network.models.RevokeAccessTokenBody;
import de.rheinfabrik.heimdalldroid.utils.SharedPreferencesOAuth2AccessTokenStorage;
import io.reactivex.Single;

/**
 * Token manger used to handle all your access token needs with the TraktTv API (http://docs.trakt.apiary.io/#).
 */
public final class TraktTvOauth2AccessTokenManager extends OAuth2AccessTokenManager {

    // Factory methods

    /**
     * Creates a new preconfigured TraktTvOauth2AccessTokenManager based of a context.
     */
    public static TraktTvOauth2AccessTokenManager from(Context context) {

        // Define the shared preferences where we will save the access token
        SharedPreferences sharedPreferences = context.getSharedPreferences("TraktTvAccessTokenStorage", Context.MODE_PRIVATE);

        // Define the storage using the the previously defined preferences
        SharedPreferencesOAuth2AccessTokenStorage<OAuth2AccessToken> tokenStorage = new SharedPreferencesOAuth2AccessTokenStorage<>(sharedPreferences, OAuth2AccessToken.class);

        // Create the new TraktTvOauth2AccessTokenManager
        return new TraktTvOauth2AccessTokenManager(tokenStorage);
    }

    // Constructor

    public TraktTvOauth2AccessTokenManager(OAuth2AccessTokenStorage storage) {
        super(storage);
    }

    // Public Api

    /**
     * Creates a new preconfigured TraktTvAuthorizationCodeGrant.
     */
    public TraktTvAuthorizationCodeGrant newAuthorizationCodeGrant() {
        TraktTvAuthorizationCodeGrant grant = new TraktTvAuthorizationCodeGrant();
        grant.setClientId(TraktTvAPIConfiguration.CLIENT_ID);
        grant.clientSecret = TraktTvAPIConfiguration.CLIENT_SECRET;
        grant.setRedirectUri(TraktTvAPIConfiguration.REDIRECT_URI);

        return grant;
    }

    /**
     * Returns a valid authorization header string using a preconfigured TraktTvRefreshAccessTokenGrant.
     */
    public Single<String> getValidAccessToken() {
        TraktTvRefreshAccessTokenGrant grant = new TraktTvRefreshAccessTokenGrant();
        grant.clientId = TraktTvAPIConfiguration.CLIENT_ID;
        grant.clientSecret = TraktTvAPIConfiguration.CLIENT_SECRET;
        grant.redirectUri = TraktTvAPIConfiguration.REDIRECT_URI;

        return super.getValidAccessToken(grant).map(token -> token.getTokenType() + " " + token.getAccessToken());
    }

    /**
     * Logs out the user if he is logged in.
     */
    public Single<Void> logout() {
        return getStorage().getStoredAccessToken()
                .toObservable()
                .filter(token -> token != null)
                .concatMap(accessToken -> {
                    RevokeAccessTokenBody body = new RevokeAccessTokenBody(accessToken.getAccessToken());
                    return TraktTvApiFactory.newApiService().revokeAccessToken(body);
                })
                .doOnNext(x -> getStorage().removeAccessToken()).singleOrError();
    }
}
