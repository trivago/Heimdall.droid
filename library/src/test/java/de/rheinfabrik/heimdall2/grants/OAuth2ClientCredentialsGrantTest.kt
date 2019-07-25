package de.rheinfabrik.heimdall2.grants

import org.junit.Assert.assertEquals
import org.junit.Test

class OAuth2ClientCredentialsGrantTest {

    // Specification for https://tools.ietf.org/html/rfc6749#section-4.4

    @Test
    fun `when a client credentials grant is created, the grant type is code`() {
        // when an client credentials grant is created
        val grant = OAuth2ClientCredentialsGrant

        // then the grant type is the correct
        assertEquals(grant.GRANT_TYPE, "client_credentials")
    }
}
