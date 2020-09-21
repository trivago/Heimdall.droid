package de.rheinfabrik.heimdall2.coroutines

import de.rheinfabrik.heimdall2.coroutines.grants.OAuth2Grant
import de.rheinfabrik.heimdall2.coroutines.grants.OAuth2RefreshAccessTokenGrant
import de.rheinfabrik.heimdall2.accesstoken.OAuth2AccessToken
import java.util.*

open class OAuth2AccessTokenManager(
    private val mStorage: OAuth2AccessTokenStorage
) {

    // Public API

    /**
     * Returns the underlying storage.
     *
     * @return - An OAuth2AccessTokenStorage.
     */
    fun getStorage(): OAuth2AccessTokenStorage = mStorage

    /**
     * Grants a new access token using the given OAuth2 grant.
     *
     * @param grant A class implementing the OAuth2Grant interface.
     * @return - A granted access token.
     */
    suspend fun grantNewAccessToken(
        grant: OAuth2Grant,
        calendar: Calendar = Calendar.getInstance()
    ): OAuth2AccessToken {
        val accessToken = grant.grantNewAccessToken()
        val newAccessToken = if (accessToken.expiresIn != null) {
            val newExpirationDate = (calendar.clone() as Calendar).apply {
                add(Calendar.SECOND, accessToken.expiresIn)
            }
            accessToken.copy(expirationDate = newExpirationDate)
        } else {
            accessToken
        }

        mStorage.storeAccessToken(
            token = newAccessToken
        )
        return newAccessToken
    }

    /**
     * Returns an Observable emitting an unexpired access token.
     * NOTE: In order to work, Heimdall needs an access token which has a refresh_token and an
     * expires_in field.
     *
     * @param refreshAccessTokenGrant The refresh grant that will be used if the access token is expired.
     * @return - An unexpired access token.
     */
    suspend fun getValidAccessToken(refreshAccessTokenGrant: OAuth2RefreshAccessTokenGrant): OAuth2AccessToken {
        val accessToken = mStorage.getStoredAccessToken()

        return if (accessToken.isExpired()) {
            refreshAccessTokenGrant.refreshToken = accessToken.refreshToken
            grantNewAccessToken(
                grant = refreshAccessTokenGrant
            )
        } else {
            accessToken
        }
    }
}
