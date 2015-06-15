package de.rheinfabrik.heimdall.grants

import com.andrewreitz.spock.android.AndroidSpecification
import spock.lang.Title

@Title("Specs for the OAuth2ImplicitGrant")
class OAuth2ImplicitGrantSpecs extends AndroidSpecification {

    // Scenarios

    def "It should have the correct response type as described in https://tools.ietf.org/html/rfc6749#section-4.2"() {

        expect: "the response type to be token"
            OAuth2ImplicitGrant.RESPONSE_TYPE == "token"
    }

}
