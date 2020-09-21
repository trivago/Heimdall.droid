package de.rheinfabrik.heimdall2.rxjava

import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import de.rheinfabrik.heimdall2.accesstoken.OAuth2AccessToken
import de.rheinfabrik.heimdall2.rxjava.grants.OAuth2RefreshAccessTokenGrant
import io.reactivex.Single
import org.junit.Test
import java.util.*

class OAuth2AccessTokenManagerGetValidAccessTokenTest {

    @Test
    fun `when subscribed to getValidAccessToken(), the non-expired token should be emitted`() {

        // given a non expired token
        val accessToken = mock<OAuth2AccessToken>().apply {
            whenever(isExpired()).thenReturn(false)
        }

        // and a token manager with a valid storage and token
        val storage = mock<OAuth2AccessTokenStorage>().apply {
            whenever(getStoredAccessToken()).thenReturn(Single.just(accessToken))
        }
        val grant = mock<OAuth2RefreshAccessTokenGrant>().apply {
            whenever(grantNewAccessToken()).thenReturn(Single.just(accessToken))
        }
        val tokenManager = OAuth2AccessTokenManager(
            mStorage = storage
        )

        // when a valid access token is requested
        val validTokenTest = tokenManager.getValidAccessToken(grant).test()

        // then the non expired token gets received
        validTokenTest.assertValue(accessToken)
    }

    @Test
    fun `when the token expires, the refresh grant should be called to refresh it`(){
        // given an expired accesstoken
        val accessToken = mock<OAuth2AccessToken>().apply {
            whenever(isExpired()).thenReturn(true)
        }

        // and a token manager
        val tokenManager = OAuth2AccessTokenManager(
            mStorage = mock<OAuth2AccessTokenStorage>().apply {
                whenever(getStoredAccessToken()).thenReturn(Single.just(accessToken))
            }
        )

        // and a OAuth2RefreshAccessTokenGrant
        val grant = mock<OAuth2RefreshAccessTokenGrant>()

        // when a valid token is needed
        tokenManager.getValidAccessToken(
            refreshAccessTokenGrant = grant
        ).test()

        // then a refresh grant asks for a new token
        verify(grant).grantNewAccessToken()

    }

    @Test
    fun `when the token expires, the grant should be updated with a new token`() {
        // given an expired OAuthAccessToken
        val pastDate = Calendar.getInstance()
        pastDate.add(Calendar.YEAR, -1)

        val accessToken = OAuth2AccessToken(
            refreshToken = "rt",
            expirationDate = pastDate
        )

        // and a token manager
        val tokenManager = OAuth2AccessTokenManager(
            mStorage = mock<OAuth2AccessTokenStorage>().apply {
                whenever(getStoredAccessToken()).thenReturn(Single.just(accessToken))
            }
        )

        // and a grant
        val grant = mock<OAuth2RefreshAccessTokenGrant>().apply {
            whenever(grantNewAccessToken()).thenReturn(Single.just(accessToken))
        }

        // when a valid access token is needed
        val grantToken = tokenManager.getValidAccessToken(
            refreshAccessTokenGrant = grant
        ).test()

        // then the grants new refreshToken gets updated
        grantToken.assertValue{
            it.refreshToken == accessToken.refreshToken
        }

    }
}
