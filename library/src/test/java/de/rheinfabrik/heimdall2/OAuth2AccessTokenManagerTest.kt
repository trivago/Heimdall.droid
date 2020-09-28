package de.rheinfabrik.heimdall2

import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import de.rheinfabrik.heimdall2.rxjava.grants.OAuth2RefreshAccessTokenGrant
import de.rheinfabrik.heimdall2.rxjava.OAuth2AccessToken
import de.rheinfabrik.heimdall2.rxjava.OAuth2AccessTokenManager
import de.rheinfabrik.heimdall2.rxjava.OAuth2AccessTokenStorage
import io.reactivex.Single
import org.junit.Test


class OAuth2AccessTokenManagerTest {

    @Test
    fun `when the access token is accessed, the token manager should emit a non expired token`(){
        // given a stored access token
        val validToken = mock<OAuth2AccessToken>()
        val accessTokenStorage = mock<OAuth2AccessTokenStorage>()
        whenever(accessTokenStorage.getStoredAccessToken()).thenReturn(Single.just(validToken))

        // and a refresh access token grant
        val refreshAccessTokenGrant = mock<OAuth2RefreshAccessTokenGrant>()
        whenever(refreshAccessTokenGrant.grantNewAccessToken()).thenReturn(Single.just(validToken))

        // and a token manager
        val tokenManager = OAuth2AccessTokenManager(accessTokenStorage)

        // when the token is accessed
        val getValidAccessTokenTest = tokenManager.getValidAccessToken(refreshAccessTokenGrant).test()

        // then the token is received
        getValidAccessTokenTest.assertValue(validToken)
    }
}