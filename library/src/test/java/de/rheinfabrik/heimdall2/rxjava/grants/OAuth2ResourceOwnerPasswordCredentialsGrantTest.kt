package de.rheinfabrik.heimdall2.grants

import de.rheinfabrik.heimdall2.rxjava.grants.OAuth2ResourceOwnerPasswordCredentialsGrant
import org.junit.Assert.assertEquals
import org.junit.Test

class OAuth2ResourceOwnerPasswordCredentialsGrantTest {

    // Specifications for https://tools.ietf.org/html/rfc6749#section-4.3

    @Test
    fun `when a access token grant is created, the grant type is refresh_token`() {
        // the grant type is the correct
        assertEquals(OAuth2ResourceOwnerPasswordCredentialsGrant.GRANT_TYPE, "password")
    }
}
