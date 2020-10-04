package de.rheinfabrik.heimdall2.coroutines

import de.rheinfabrik.heimdall2.accesstoken.OAuth2AccessToken
import de.rheinfabrik.heimdall2.coroutines.grants.OAuth2Grant
import de.rheinfabrik.heimdall2.coroutines.grants.OAuth2RefreshAccessTokenGrant
import java.util.Calendar

open class OAuth2AccessTokenManager(
    private val mOAuth2AccessTokenStorage: OAuth2AccessTokenStorage
) {

    // Public API

    /**
     * Returns the underlying storage.
     *
     * @return - An OAuth2AccessTokenStorage.
     */
    fun getStorage(): OAuth2AccessTokenStorage = mOAuth2AccessTokenStorage

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
        val token = grant.grantNewAccessToken()
        val updatedToken = if (token.expiresIn != null) {
            val newExpirationDate = (calendar.clone() as Calendar).apply {
                add(Calendar.SECOND, token.expiresIn)
            }
            token.copy(
                expirationDate = newExpirationDate
            )
        } else {
            token
        }

        mOAuth2AccessTokenStorage.storeAccessToken(
            token = updatedToken
        )
        return updatedToken
    }

    /**
     * Returns an unexpired access token.
     * NOTE: In order to work, Heimdall needs an access token which has a refresh_token and an
     * expires_in field.
     *
     * @param refreshAccessTokenGrant The refresh grant that will be used if the access token is expired.
     * @return - An unexpired access token.
     */
    suspend fun getValidAccessToken(
        refreshAccessTokenGrant: OAuth2RefreshAccessTokenGrant
    ): OAuth2AccessToken {
        val storedAccessToken = mOAuth2AccessTokenStorage.getStoredAccessToken()
        return if (storedAccessToken.isExpired()) {
            storedAccessToken.refreshToken?.let { refreshToken ->
                refreshAccessTokenGrant.refreshToken = refreshToken
                    grantNewAccessToken(
                        grant = refreshAccessTokenGrant
                    )
            } ?: throw IllegalStateException("Refresh Token for Refresh Access Token Grant is required")
        } else {
            storedAccessToken
        }
    }
}
