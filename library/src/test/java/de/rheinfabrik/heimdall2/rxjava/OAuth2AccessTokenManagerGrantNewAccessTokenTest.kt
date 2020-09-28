package de.rheinfabrik.heimdall2.rxjava

import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import de.rheinfabrik.heimdall2.accesstoken.OAuth2AccessToken
import de.rheinfabrik.heimdall2.rxjava.grants.OAuth2Grant
import io.reactivex.Single
import org.junit.Test
import java.util.*

class OAuth2AccessTokenManagerGrantNewAccessTokenTest {

    @Test
    fun `when a new access token has an expiration date, the token manager should generate and set the correct expiration date`() {

        // given an OAuth2AccessToken
        val accessToken = OAuth2AccessToken(
            expirationDate = null
        )
        val changedAccessToken = accessToken.copy(
            expiresIn = 3
        )


        // and a grant that emits that token
        val grant = mock<OAuth2Grant>().apply {
            whenever(grantNewAccessToken()).thenReturn(Single.just(changedAccessToken))
        }

        // and a tokenManager
        val tokenManager = OAuth2AccessTokenManager(
            mStorage = mock()
        )

        // when a new access token is needed
        val calendar = Calendar.getInstance()
        val newToken = tokenManager.grantNewAccessToken(
            grant = grant,
            calendar = calendar
        ).test()

        // then the access token should have the correct expiration date
        newToken.assertValue {
            it.expirationDate?.timeInMillis == calendar.timeInMillis + 3000
        }
    }

    @Test
    fun `when a new access token doesn't have an expiration date, the token manager should not generate and a new  expiration date`() {

        // given an OAuth2AccessToken
        val accessToken = OAuth2AccessToken(
            expirationDate = null
        )
        val changedAccessToken = accessToken.copy(
            expiresIn = null
        )

        // and a grant that emits that token
        val grant = mock<OAuth2Grant>().apply {
            whenever(grantNewAccessToken()).thenReturn(Single.just(changedAccessToken))
        }

        // and a tokenManager
        val tokenManager = OAuth2AccessTokenManager(
            mStorage = mock()
        )

        // when a new access token is needed
        val calendar = Calendar.getInstance()
        val newToken = tokenManager.grantNewAccessToken(
            grant = grant,
            calendar = calendar
        ).test()

        // then the access token should have the correct expiration date
        newToken.assertValue {
            it.expirationDate == null
        }
    }

    @Test
    fun `when a new access token is needed, the storage should be called to store the token`() {
        // given a grant that emits a token
        val accessToken = OAuth2AccessToken()
        val grant = mock<OAuth2Grant>().apply {
            whenever(grantNewAccessToken()).thenReturn(Single.just(accessToken))
        }

        // and a token manager
        val storage = mock<OAuth2AccessTokenStorage>()
        val tokenManager = OAuth2AccessTokenManager(
            mStorage = storage
        )

        // when a new access token is needed
        tokenManager.grantNewAccessToken(grant).test()

        // then the storage is called
        verify(storage).storeAccessToken(accessToken)
    }

}
