package de.rheinfabrik.heimdall2.grants

import spock.lang.Specification
import spock.lang.Title

@Title("Specs for the OAuth2RefreshAccessTokenGrant")
class OAuth2RefreshAccessTokenGrantSpecs extends Specification {

    // Scenarios

    def "It should have the correct grant type as described in https://tools.ietf.org/html/rfc6749#section-6"() {

        expect: "the grant type to be refresh_token"
            OAuth2RefreshAccessTokenGrant.GRANT_TYPE == "refresh_token"
    }

}
