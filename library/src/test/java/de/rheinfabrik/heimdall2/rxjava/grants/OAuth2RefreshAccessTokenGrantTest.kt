package de.rheinfabrik.heimdall2.grants

import de.rheinfabrik.heimdall2.rxjava.grants.OAuth2RefreshAccessTokenGrant
import org.junit.Assert.assertEquals
import org.junit.Test

class OAuth2RefreshAccessTokenGrantTest {

    // Specifications for https://tools.ietf.org/html/rfc6749#section-6

    @Test
    fun `when an refresh access token grant is created, the grant type is refresh_token`() {
        // the grant type is the correct
        assertEquals(OAuth2RefreshAccessTokenGrant.GRANT_TYPE, "refresh_token")
    }
}
