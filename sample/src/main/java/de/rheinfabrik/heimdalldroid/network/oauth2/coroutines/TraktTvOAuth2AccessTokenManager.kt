package de.rheinfabrik.heimdalldroid.network.oauth2.coroutines

import android.content.Context
import de.rheinfabrik.heimdall2.accesstoken.OAuth2AccessToken
import de.rheinfabrik.heimdall2.coroutines.OAuth2AccessTokenManager
import de.rheinfabrik.heimdall2.coroutines.OAuth2AccessTokenStorage
import de.rheinfabrik.heimdalldroid.TraktTvAPIConfiguration
import de.rheinfabrik.heimdalldroid.network.TraktTvApiFactory
import de.rheinfabrik.heimdalldroid.network.models.RevokeAccessTokenBody
import de.rheinfabrik.heimdalldroid.utils.SharedPreferencesOAuth2AccessTokenStorage

/**
 * Token manager used to handle all your access token needs with the TraktTv API (http://docs.trakt.apiary.io/#).
 */
class TraktTvOAuth2AccessTokenManager(
    storage: OAuth2AccessTokenStorage
) : OAuth2AccessTokenManager(mStorage = storage) {
    // Public Api
    /**
     * Creates a new preconfigured TraktTvAuthorizationCodeGrant.
     */
    fun newAuthorizationCodeGrant(): TraktTvAuthorizationCodeGrant =
        TraktTvAuthorizationCodeGrant(
            clientSecret = TraktTvAPIConfiguration.CLIENT_SECRET
        )

    /**
     * Returns a valid authorization header string using a preconfigured TraktTvRefreshAccessTokenGrant.
     */
    suspend fun validAccessToken(): String =
        getValidAccessToken(
            refreshAccessTokenGrant = TraktTvRefreshAccessTokenGrant(
                clientId = TraktTvAPIConfiguration.CLIENT_ID,
                clientSecret = TraktTvAPIConfiguration.CLIENT_SECRET,
                redirectUri = TraktTvAPIConfiguration.REDIRECT_URI
            )
        ).let { oauth2AccessToken ->
            "${oauth2AccessToken.tokenType} ${oauth2AccessToken.accessToken}"
        }

    /**
     * Logs out the user if he is logged in.
     */
    suspend fun logout() {
        val oAuth2AccessToken = getStorage().getStoredOAuth2AccessToken()
        TraktTvApiFactory.newApiServiceCoroutines().revokeAccessToken(
            body = RevokeAccessTokenBody(oAuth2AccessToken.accessToken)
        )
        getStorage().removeOAuth2AccessToken()
    }

    companion object {
        // Factory methods
        /**
         * Creates a new preconfigured TraktTvOauth2AccessTokenManager based of a context.
         */
        fun from(context: Context): TraktTvOAuth2AccessTokenManager {

            // Define the shared preferences where we will save the access token
            val sharedPreferences = context.getSharedPreferences("TraktTvAccessTokenStorage", Context.MODE_PRIVATE)

            // Define the storage using the the previously defined preferences
            val tokenStorage = SharedPreferencesOAuth2AccessTokenStorage(sharedPreferences, OAuth2AccessToken::class.java)

            // Create the new TraktTvOauth2AccessTokenManager
            return TraktTvOAuth2AccessTokenManager(
                tokenStorage
            )
        }
    }
}