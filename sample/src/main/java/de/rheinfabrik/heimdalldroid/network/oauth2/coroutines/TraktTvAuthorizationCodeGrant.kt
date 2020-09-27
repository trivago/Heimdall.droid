package de.rheinfabrik.heimdalldroid.network.oauth2.coroutines

import android.net.Uri
import de.rheinfabrik.heimdall2.accesstoken.OAuth2AccessToken
import de.rheinfabrik.heimdall2.coroutines.grants.OAuth2AuthorizationCodeGrant
import de.rheinfabrik.heimdalldroid.network.TraktTvApiFactory
import de.rheinfabrik.heimdalldroid.network.models.AccessTokenRequestBody
import java.net.URL

class TraktTvAuthorizationCodeGrant(
    val clientSecret: String
) : OAuth2AuthorizationCodeGrant() {

    // OAuth2AuthorizationCodeGrant
    override suspend fun buildAuthorizationUrl(): URL =
        URL(
            Uri.parse("https://trakt.tv/oauth/authorize")
                .buildUpon()
                .appendQueryParameter("client_id", clientId)
                .appendQueryParameter("redirect_uri", redirectUri)
                .appendQueryParameter("response_type", RESPONSE_TYPE)
                .build()
                .toString()
        )

    override suspend fun exchangeTokenUsingCode(code: String): OAuth2AccessToken =
        TraktTvApiFactory.newApiServiceCoroutines().grantNewAccessToken(
            body = AccessTokenRequestBody(
                code, clientId, redirectUri, clientSecret, GRANT_TYPE
            )
        )
}
