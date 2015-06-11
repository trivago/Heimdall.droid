package de.rheinfabrik.heimdall.grants

import com.andrewreitz.spock.android.AndroidSpecification
import spock.lang.Title

@Title("Specs for the OAuth2ClientCredentialsGrant")
class OAuth2ClientCredentialsGrantSpecs extends AndroidSpecification {

    // Scenarios

    def "It should have the correct grant type"() {

        expect: "the grant type to be client_credentials"
            OAuth2ClientCredentialsGrant.GRANT_TYPE == "client_credentials"
    }

}
