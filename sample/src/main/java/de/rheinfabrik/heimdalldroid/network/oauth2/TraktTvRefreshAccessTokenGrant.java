package de.rheinfabrik.heimdalldroid.network.oauth2;

import de.rheinfabrik.heimdall.OAuth2AccessToken;
import de.rheinfabrik.heimdall.grants.OAuth2RefreshAccessTokenGrant;
import de.rheinfabrik.heimdalldroid.network.TraktTvApiFactory;
import de.rheinfabrik.heimdalldroid.network.models.RefreshTokenRequestBody;
import rx.Observable;

/**
 * TraktTv refresh token grant as described in http://docs.trakt.apiary.io/#reference/authentication-oauth/token/exchange-refresh_token-for-access_token.
 */
public class TraktTvRefreshAccessTokenGrant extends OAuth2RefreshAccessTokenGrant<OAuth2AccessToken> {

    // Properties

    public String clientSecret;
    public String clientId;
    public String redirectUri;

    // OAuth2RefreshAccessTokenGrant

    @Override
    public Observable<OAuth2AccessToken> grantNewAccessToken() {
        RefreshTokenRequestBody body = new RefreshTokenRequestBody(refreshToken, clientId, clientSecret, redirectUri, GRANT_TYPE);
        return TraktTvApiFactory.newApiService().refreshAccessToken(body);
    }
}
