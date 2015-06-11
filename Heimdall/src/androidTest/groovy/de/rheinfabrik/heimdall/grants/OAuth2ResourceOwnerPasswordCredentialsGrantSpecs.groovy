package de.rheinfabrik.heimdall.grants

import com.andrewreitz.spock.android.AndroidSpecification
import spock.lang.Title

@Title("Specs for the OAuth2ResourceOwnerPasswordCredentialsGrant")
class OAuth2ResourceOwnerPasswordCredentialsGrantSpecs extends AndroidSpecification {

    // Scenarios

    def "It should have the correct grant type"() {

        expect: "the grant type to be password"
            OAuth2ResourceOwnerPasswordCredentialsGrant.GRANT_TYPE == "password"
    }

}
