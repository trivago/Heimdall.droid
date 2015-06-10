package de.rheinfabrik.heimdall.grants

import com.andrewreitz.spock.android.AndroidSpecification
import spock.lang.Title

@Title("Specs for the OAuth2AuthorizationCodeGrant")
class OAuth2AuthorizationCodeGrantSpecs extends AndroidSpecification {

    // Scenarios

    def "It should have the correct response type"() {

        expect: "the response type to be code"
            OAuth2AuthorizationCodeGrant.RESPONSE_TYPE == "code"
    }

    def "It should have the correct grant type"() {

        expect: "the response type to be authorization_code"
            OAuth2AuthorizationCodeGrant.GRANT_TYPE == "authorization_code"
    }

}
