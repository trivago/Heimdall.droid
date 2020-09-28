package de.rheinfabrik.heimdall2.grants

import de.rheinfabrik.heimdall2.rxjava.grants.OAuth2AuthorizationCodeGrant
import org.junit.Assert.assertEquals
import org.junit.Test

class OAuth2AuthorizationCodeGrantTest {

    // Specifications for https://tools.ietf.org/html/rfc6749#section-4.1

    @Test
    fun `when an authorization code grant is created, the grant type is code`() {
        // the grant type is the correct
        assertEquals(OAuth2AuthorizationCodeGrant.GRANT_TYPE, "authorization_code")
    }

    @Test
    fun `when an authorization code grant is created, the response type is code`() {
        //  the grant type is the correct
        assertEquals(OAuth2AuthorizationCodeGrant.RESPONSE_TYPE, "code")
    }
}
