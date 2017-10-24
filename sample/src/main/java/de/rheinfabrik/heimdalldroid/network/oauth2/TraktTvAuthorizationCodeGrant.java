package de.rheinfabrik.heimdalldroid.network.oauth2;

import android.net.Uri;

import java.net.URL;

import de.rheinfabrik.heimdall.OAuth2AccessToken;
import de.rheinfabrik.heimdall.grants.OAuth2AuthorizationCodeGrant;
import de.rheinfabrik.heimdalldroid.network.TraktTvApiFactory;
import de.rheinfabrik.heimdalldroid.network.models.AccessTokenRequestBody;
import rx.Observable;

/**
 * TraktTv authorization code grant as described in http://docs.trakt.apiary.io/#reference/authentication-oauth.
 */
public class TraktTvAuthorizationCodeGrant extends OAuth2AuthorizationCodeGrant<OAuth2AccessToken> {

    // Properties

    public String clientSecret;

    // OAuth2AuthorizationCodeGrant

    @Override
    public URL buildAuthorizationUrl() {
        try {
            return new URL(
                    Uri.parse("https://trakt.tv/oauth/authorize")
                            .buildUpon()
                            .appendQueryParameter("client_id", clientId)
                            .appendQueryParameter("redirect_uri", redirectUri)
                            .appendQueryParameter("response_type", RESPONSE_TYPE)
                            .build()
                            .toString()
            );
        } catch (Exception ignored) {
            return null;
        }
    }

    @Override
    public Observable<OAuth2AccessToken> exchangeTokenUsingCode(String code) {
        AccessTokenRequestBody body = new AccessTokenRequestBody(code, clientId, redirectUri, clientSecret, GRANT_TYPE);
        return TraktTvApiFactory.newApiService().grantNewAccessToken(body);
    }
}
