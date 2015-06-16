package de.rheinfabrik.heimdalldroid.network.oauth2;

import android.content.Context;
import android.content.SharedPreferences;

import de.rheinfabrik.heimdall.OAuth2AccessToken;
import de.rheinfabrik.heimdall.OAuth2AccessTokenManager;
import de.rheinfabrik.heimdall.OAuth2AccessTokenStorage;
import de.rheinfabrik.heimdall.extras.SharedPreferencesOAuth2AccessTokenStorage;
import de.rheinfabrik.heimdalldroid.network.TraktTvAPIConfiguration;
import rx.Observable;

/**
 * Token manger used to handle all your access token needs with the TraktTv API (http://docs.trakt.apiary.io/#).
 */
public final class TraktTvOauth2AccessTokenManager extends OAuth2AccessTokenManager<OAuth2AccessToken> {

    // Factory methods

    /**
     * Creates a new preconfigured TraktTvOauth2AccessTokenManager based of a context.
     */
    public static TraktTvOauth2AccessTokenManager from(Context context) {

        // Define the shared preferences where we will save the access token
        SharedPreferences sharedPreferences = context.getSharedPreferences("GitHubAccessTokenStorage", Context.MODE_PRIVATE);

        // Define the storage using the the previously defined preferences
        SharedPreferencesOAuth2AccessTokenStorage<OAuth2AccessToken> tokenStorage = new SharedPreferencesOAuth2AccessTokenStorage<>(sharedPreferences, OAuth2AccessToken.class);

        // Create the new TraktTvOauth2AccessTokenManager
        return new TraktTvOauth2AccessTokenManager(tokenStorage);
    }

    // Constructor

    public TraktTvOauth2AccessTokenManager(OAuth2AccessTokenStorage<OAuth2AccessToken> storage) {
        super(storage);
    }

    // Public Api

    /**
     * Creates a new preconfigured TraktTvAuthorizationCodeGrant.
     */
    public TraktTvAuthorizationCodeGrant newAuthorizationCodeGrant() {
        TraktTvAuthorizationCodeGrant grant = new TraktTvAuthorizationCodeGrant();
        grant.clientId = TraktTvAPIConfiguration.CLIENT_ID;
        grant.clientSecret = TraktTvAPIConfiguration.CLIENT_SECRET;
        grant.redirectUri = TraktTvAPIConfiguration.REDIRECT_URI;

        return grant;
    }

    /**
     * Returns a valid authorization header string using a preconfigured TraktTvRefreshAccessTokenGrant.
     */
    public Observable<String> getValidAccessToken() {
        TraktTvRefreshAccessTokenGrant grant = new TraktTvRefreshAccessTokenGrant();
        grant.clientId = TraktTvAPIConfiguration.CLIENT_ID;
        grant.clientSecret = TraktTvAPIConfiguration.CLIENT_SECRET;
        grant.redirectUri = TraktTvAPIConfiguration.REDIRECT_URI;

        return super.getValidAccessToken(grant).map(token -> token.tokenType + " " + token.accessToken);
    }
}
