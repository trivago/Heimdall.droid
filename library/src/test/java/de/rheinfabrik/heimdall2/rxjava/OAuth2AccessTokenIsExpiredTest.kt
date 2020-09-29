package de.rheinfabrik.heimdall2.rxjava

import de.rheinfabrik.heimdall2.accesstoken.OAuth2AccessToken
import org.junit.Assert.assertEquals
import org.junit.Test
import java.util.*

class OAuth2AccessTokenIsExpiredTest {

    @Test
    fun `when the expirationDate is null, isExpired method should return false`() {
        // given an accessToken
        val accessToken = OAuth2AccessToken(expirationDate = null)

        // when the isExpired method is called
        val value = accessToken.isExpired()

        // then false is returned
        assertEquals(value, false)

    }

    @Test
    fun `when the expirationDate is in the past, isExpired method should return false`() {
        // given a date in the past
        val pastCalendar = Calendar.getInstance()
        pastCalendar.add(Calendar.YEAR, 1)

        // and a token with a past date
        val accessToken = OAuth2AccessToken(expirationDate = pastCalendar)

        // when the isExpired method is called
        val value = accessToken.isExpired()

        // then true is returned
        assertEquals(value, false)
    }

    @Test
    fun `when the expirationDate is in the future, isExpired method should return false`() {
        // given a date in the past
        val futureDate = Calendar.getInstance()
        futureDate.add(Calendar.YEAR, 1)

        // and a token with a past date
        val accessToken = OAuth2AccessToken(expirationDate = futureDate)

        // when the isExpired method is called
        val value = accessToken.isExpired()

        // then true is returned
        assertEquals(value, false)
    }
}
