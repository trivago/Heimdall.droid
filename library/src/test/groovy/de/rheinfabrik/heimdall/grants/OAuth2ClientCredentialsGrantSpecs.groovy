package de.rheinfabrik.heimdall.grants

import spock.lang.Specification
import spock.lang.Title

@Title("Specs for the OAuth2ClientCredentialsGrant")
class OAuth2ClientCredentialsGrantSpecs extends Specification {

    // Scenarios

    def "It should have the correct grant type as described in https://tools.ietf.org/html/rfc6749#section-4.4"() {

        expect: "the grant type to be client_credentials"
            OAuth2ClientCredentialsGrant.GRANT_TYPE == "client_credentials"
    }

}
