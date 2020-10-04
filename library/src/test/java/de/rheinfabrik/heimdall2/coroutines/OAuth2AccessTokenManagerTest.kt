package de.rheinfabrik.heimdall2.coroutines

import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import de.rheinfabrik.heimdall2.accesstoken.OAuth2AccessToken
import de.rheinfabrik.heimdall2.coroutines.grants.OAuth2RefreshAccessTokenGrant
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert.assertEquals
import org.junit.Test
import java.util.*

@ExperimentalCoroutinesApi
class OAuth2AccessTokenManagerTest {

    @Test
    fun `when the access token is accessed, and the token is not expired, then the token should be returned`() =
        runBlockingTest {
            // given a stored access token
            val expectedOutcome = mock<OAuth2AccessToken>()
            val accessTokenStorage = mock<OAuth2AccessTokenStorage>()
            whenever(accessTokenStorage.getStoredAccessToken()).thenReturn(expectedOutcome)

            // and a refresh access token grant
            val refreshAccessTokenGrant = mock<OAuth2RefreshAccessTokenGrant>()
            whenever(refreshAccessTokenGrant.grantNewAccessToken()).thenReturn(null)

            // and a token manager
            val tokenManager = OAuth2AccessTokenManager(
                mOAuth2AccessTokenStorage = accessTokenStorage
            )

            // when the token is accessed
            val output = tokenManager.getValidAccessToken(
                refreshAccessTokenGrant = refreshAccessTokenGrant
            )

            // then the token is received
            assertEquals(expectedOutcome, output)
        }

    @Test
    fun `when the access token is accessed, and the token is expired, then a renewed token should be returned`() =
        runBlockingTest {
            // given a stored access token that returns an expired token
            val expiredToken = mock<OAuth2AccessToken>().apply {
                whenever(isExpired()).thenReturn(true)
                whenever(refreshToken).thenReturn("")
            }
            val accessTokenStorage = mock<OAuth2AccessTokenStorage>()
            whenever(accessTokenStorage.getStoredAccessToken()).thenReturn(expiredToken)

            // and a refresh access token grant
            val expectedOutcome = OAuth2AccessToken(
                refreshToken = "refresh_token",
                expirationDate = Calendar.getInstance()
            )
            val refreshAccessTokenGrant = mock<OAuth2RefreshAccessTokenGrant>()
            whenever(refreshAccessTokenGrant.grantNewAccessToken()).thenReturn(expectedOutcome)

            // and a token manager
            val tokenManager = OAuth2AccessTokenManager(
                mOAuth2AccessTokenStorage = accessTokenStorage
            )

            // when the token is retrieved
            val output = tokenManager.getValidAccessToken(
                refreshAccessTokenGrant = refreshAccessTokenGrant
            )

            // then the refreshed token is received
            assertEquals(expectedOutcome, output)
        }

    @Test(expected = IllegalStateException::class)
    fun `when the access token is accessed, and the token is expired and it doesnt have a refresh token, then an exception is returned`() =
        runBlockingTest {
            // given a stored access token that returns an expired token
            val expiredToken = mock<OAuth2AccessToken>().apply {
                whenever(isExpired()).thenReturn(true)
            }
            val accessTokenStorage = mock<OAuth2AccessTokenStorage>()
            whenever(accessTokenStorage.getStoredAccessToken()).thenReturn(expiredToken)

            // and a refresh access token grant
            val expectedOutcome = OAuth2AccessToken(
                refreshToken = "refresh_token",
                expirationDate = Calendar.getInstance()
            )
            val refreshAccessTokenGrant = mock<OAuth2RefreshAccessTokenGrant>()
            whenever(refreshAccessTokenGrant.grantNewAccessToken()).thenReturn(expectedOutcome)

            // and a token manager
            val tokenManager = OAuth2AccessTokenManager(
                mOAuth2AccessTokenStorage = accessTokenStorage
            )

            // when the token is retrieved
            val output = tokenManager.getValidAccessToken(
                refreshAccessTokenGrant = refreshAccessTokenGrant
            )

            // then the exception is thrown
        }
}
