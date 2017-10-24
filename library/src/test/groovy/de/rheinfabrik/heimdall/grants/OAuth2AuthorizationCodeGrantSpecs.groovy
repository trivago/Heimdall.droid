package de.rheinfabrik.heimdall.grants

import spock.lang.Specification
import spock.lang.Title

@Title("Specs for the OAuth2AuthorizationCodeGrant")
class OAuth2AuthorizationCodeGrantSpecs extends Specification {

    // Scenarios

    def "It should have the correct response type as described in https://tools.ietf.org/html/rfc6749#section-4.1"() {

        expect: "the response type to be code"
            OAuth2AuthorizationCodeGrant.RESPONSE_TYPE == "code"
    }

    def "It should have the correct grant type as described in https://tools.ietf.org/html/rfc6749#section-4.1"() {

        expect: "the response type to be authorization_code"
            OAuth2AuthorizationCodeGrant.GRANT_TYPE == "authorization_code"
    }
}
