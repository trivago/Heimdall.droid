package de.rheinfabrik.heimdall2

import de.rheinfabrik.heimdall2.grants.OAuth2Grant
import de.rheinfabrik.heimdall2.grants.OAuth2RefreshAccessTokenGrant
import io.reactivex.Single
import java.util.Calendar

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
     * @return - An Single emitting the granted access token.
     */
    fun grantNewAccessToken(
        grant: OAuth2Grant,
        calendar: Calendar = Calendar.getInstance()
    ): Single<OAuth2AccessToken> =
        grant.grantNewAccessToken()
            .map {
                if (it.expiresIn != null) {
                    val newExpirationDate = (calendar.clone() as Calendar).apply {
                        add(Calendar.SECOND, it.expiresIn)
                    }
                    it.copy(expirationDate = newExpirationDate)
                } else {
                    it
                }
            }
            .doOnSuccess { token ->
                mStorage.storeAccessToken(
                    token = token
                )
            }
            .cache()

    /**
     * Returns an Observable emitting an unexpired access token.
     * NOTE: In order to work, Heimdall needs an access token which has a refresh_token and an
     * expires_in field.
     *
     * @param refreshAccessTokenGrant The refresh grant that will be used if the access token is expired.
     * @return - An Single emitting an unexpired access token.
     */
    fun getValidAccessToken(refreshAccessTokenGrant: OAuth2RefreshAccessTokenGrant): Single<OAuth2AccessToken> =
        mStorage.getStoredAccessToken()
            .flatMap { accessToken ->
                if (accessToken.isExpired()) {
                    refreshAccessTokenGrant.refreshToken = accessToken.refreshToken
                    grantNewAccessToken(refreshAccessTokenGrant)
                } else {
                    Single.just(accessToken)
                }
            }
}
