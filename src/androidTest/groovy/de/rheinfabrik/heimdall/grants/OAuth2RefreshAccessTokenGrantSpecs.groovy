package de.rheinfabrik.heimdall.grants

import com.andrewreitz.spock.android.AndroidSpecification
import spock.lang.Title

@Title("Specs for the OAuth2RefreshAccessTokenGrant")
class OAuth2RefreshAccessTokenGrantSpecs extends AndroidSpecification {

    // Scenarios

    def "It should have the correct grant type as described in https://tools.ietf.org/html/rfc6749#section-6"() {

        expect: "the grant type to be refresh_token"
            OAuth2RefreshAccessTokenGrant.GRANT_TYPE == "refresh_token"
    }

}
