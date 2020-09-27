package de.rheinfabrik.heimdalldroid.network.oauth2.rxjava;

import android.net.Uri;

import de.rheinfabrik.heimdall2.rxjava.grants.OAuth2AuthorizationCodeGrant;
import io.reactivex.Observable;
import java.net.URL;

import de.rheinfabrik.heimdall2.accesstoken.OAuth2AccessToken;
import de.rheinfabrik.heimdalldroid.network.TraktTvApiFactory;
import de.rheinfabrik.heimdalldroid.network.models.AccessTokenRequestBody;

/**
 * TraktTv authorization code grant as described in http://docs.trakt.apiary.io/#reference/authentication-oauth.
 */
public class TraktTvAuthorizationCodeGrant extends OAuth2AuthorizationCodeGrant {

    // Properties

    public String clientSecret;

    // OAuth2AuthorizationCodeGrant

    @Override
    public URL buildAuthorizationUrl() {
        try {
            return new URL(
                    Uri.parse("https://trakt.tv/oauth/authorize")
                            .buildUpon()
                            .appendQueryParameter("client_id", getClientId())
                            .appendQueryParameter("redirect_uri", getRedirectUri())
                            .appendQueryParameter("response_type", OAuth2AuthorizationCodeGrant.RESPONSE_TYPE)
                            .build()
                            .toString()
            );
        } catch (Exception ignored) {
            return null;
        }
    }

    @Override
    public Observable<OAuth2AccessToken> exchangeTokenUsingCode(String code) {
        AccessTokenRequestBody body = new AccessTokenRequestBody(
                code, getClientId(), getRedirectUri(), clientSecret, GRANT_TYPE
        );
        return TraktTvApiFactory.newApiServiceRxJava().grantNewAccessToken(body);
    }
}
