package de.rheinfabrik.heimdall2.grants

import de.rheinfabrik.heimdall2.rxjava.grants.OAuth2ImplicitGrant
import org.junit.Assert.assertEquals
import org.junit.Test

class OAuth2ImplicitGrantTest {

    // Specifications for https://tools.ietf.org/html/rfc6749#section-4.2

    @Test
    fun `when an implicit grant is created, the response type is token`() {
        // when an authorization code grant is created
        val grant = OAuth2ImplicitGrant

        // then the grant type is the correct
        assertEquals(grant.RESPONSE_TYPE, "token")
    }
}
