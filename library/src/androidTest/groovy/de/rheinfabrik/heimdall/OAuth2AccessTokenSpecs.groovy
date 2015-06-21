package de.rheinfabrik.heimdall

import com.andrewreitz.spock.android.AndroidSpecification
import com.google.gson.Gson
import spock.lang.Title

import static java.util.TimeZone.getTimeZone

@Title("Specs for serialization in the OAuth2AccessToken class.")
class OAuth2AccessTokenSerializationSpecs extends AndroidSpecification {

    // Scenarios

    def "It should create the correct JSON for a given OAuth2AccessToken"() {

        given: "An OAuth2AccessToken"
            OAuth2AccessToken accessToken = new OAuth2AccessToken(refreshToken: "rt", expiresIn: 3600, accessToken: "at", tokenType: "bearer")
            accessToken.expirationDate = Calendar.getInstance(getTimeZone("UTC"))
            accessToken.expirationDate.setTimeInMillis(0)

        when: "I serialize it with Gson"
            String json = new Gson().toJson(accessToken)

        then: "The JSON should be as expected"
            json == "{\"access_token\":\"at\",\"heimdall_expiration_date\":{\"year\":1970,\"month\":0,\"dayOfMonth\":1,\"hourOfDay\":1,\"minute\":0,\"second\":0},\"expires_in\":3600,\"refresh_token\":\"rt\",\"token_type\":\"bearer\"}"
    }

    def "It should create the correct OAuth2AccessToken for a given JSON"() {

        given: "Some JSON representing an OAuth2AccessToken"
            String json = "{\"access_token\":\"at\",\"heimdall_expiration_date\":{\"year\":1970,\"month\":0,\"dayOfMonth\":1,\"hourOfDay\":1,\"minute\":0,\"second\":0},\"refresh_token\":\"rt\",\"token_type\":\"bearer\",\"expires_in\":3600}"

        when: "I deserialize it with Gson"
            OAuth2AccessToken accessToken = new Gson().fromJson(json, OAuth2AccessToken.class)

        then: "The OAuth2AccessToken should be as expected"
            with(accessToken, {
                accessToken.refreshToken == "rt"
                accessToken.expiresIn == 3600
                accessToken.accessToken == "at"
                accessToken.tokenType == "bearer"

                Calendar calendar = Calendar.getInstance(getTimeZone("UTC"))
                calendar.setTimeInMillis(0)
                accessToken.expirationDate == calendar
            })
    }
}

@Title("Specs for the isExpired() function in the OAuth2AccessToken class.")
class OAuth2AccessTokenIsExpiredSpecs extends AndroidSpecification {

    // Scenarios

    @SuppressWarnings("GroovyPointlessBoolean")
    def "It should return false if the expirationDate is null"() {

        given: "An OAuth2AccessToken with null as expirationDate"
            OAuth2AccessToken accessToken = new OAuth2AccessToken(expirationDate: null)

        when: "I check if the access token is expired"
            boolean isExpired = accessToken.isExpired()

        then: "It should be true"
            isExpired == false
    }

    @SuppressWarnings("GroovyPointlessBoolean")
    def "It should return false if the expirationDate is in the future"() {

        given: "A date which is in the future"
            Calendar future = Calendar.getInstance()
            future.add(Calendar.YEAR, 1)

        and: "An OAuth2AccessToken with that future as expirationDate"
            OAuth2AccessToken accessToken = new OAuth2AccessToken(expirationDate: future)

        when: "I check if the access token is expired"
            boolean isExpired = accessToken.isExpired()

        then: "It should be false"
            isExpired == false
    }

    @SuppressWarnings("GroovyPointlessBoolean")
    def "It should return true if the expirationDate is in the past"() {

        given: "A date which is in the past"
            Calendar past = Calendar.getInstance()
            past.add(Calendar.YEAR, -1)

        and: "An OAuth2AccessToken with that past as expirationDate"
            OAuth2AccessToken accessToken = new OAuth2AccessToken(expirationDate: past)

        when: "I check if the access token is expired"
            boolean isExpired = accessToken.isExpired()

        then: "It should be true"
            isExpired == true
    }

}
