package de.rheinfabrik.heimdalldroid.network.oauth2.coroutines

import de.rheinfabrik.heimdall2.accesstoken.OAuth2AccessToken
import de.rheinfabrik.heimdall2.coroutines.grants.OAuth2RefreshAccessTokenGrant
import de.rheinfabrik.heimdalldroid.network.TraktTvApiFactory
import de.rheinfabrik.heimdalldroid.network.models.RefreshTokenRequestBody

class TraktTvRefreshAccessTokenGrant(
    val clientSecret: String,
    val clientId: String,
    val redirectUri: String
) : OAuth2RefreshAccessTokenGrant() {

    // OAuth2RefreshAccessTokenGrant
    override suspend fun grantNewAccessToken(): OAuth2AccessToken =
        TraktTvApiFactory.newApiServiceCoroutines().refreshAccessToken(
            body = RefreshTokenRequestBody(
                refreshToken,
                clientId,
                clientSecret,
                redirectUri,
                GRANT_TYPE
            )
        )
}
