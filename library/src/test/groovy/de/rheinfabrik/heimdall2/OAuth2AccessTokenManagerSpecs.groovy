package de.rheinfabrik.heimdall2

import de.rheinfabrik.heimdall2.grants.OAuth2Grant
import de.rheinfabrik.heimdall2.grants.OAuth2RefreshAccessTokenGrant
import io.reactivex.Single
import spock.lang.Specification
import spock.lang.Title

@Title("Tests for the constructor of the OAuth2AccessTokenManager class")
class OAuth2AccessTokenManagerConstructorSpecs extends Specification {

    // Scenarios

    @SuppressWarnings(["GroovyResultOfObjectAllocationIgnored", "GroovyAssignabilityCheck"])
    def "it should throw an exception if the storage argument is null"() {

        given: "A null storage"
            OAuth2AccessTokenStorage storage = null

        when: "I initialize an OAuth2AccessTokenManager with a that storage"
            new OAuth2AccessTokenManager(storage)

        then: "An IllegalArgumentException is thrown"
            thrown(IllegalArgumentException)
    }
}

@Title("Tests for the grantNewAccessToken() function of the OAuth2AccessTokenManager class")
class OAuth2AccessTokenManagerGrantNewAccessTokenSpecs extends Specification {

    // Scenarios

    def "it should throw an IllegalArgumentException when the grant parameter is null"() {

        given: "A null grant"
            OAuth2Grant grant = null

        and: "An OAuth2AccessTokenManager"
            OAuth2AccessTokenManager tokenManager = new OAuth2AccessTokenManager<OAuth2AccessToken>(Mock(OAuth2AccessTokenStorage))

        when: "I ask for a new access token"
            tokenManager.grantNewAccessToken(grant)

        then: "An IllegalArgumentException is thrown"
            thrown(IllegalArgumentException)
    }

    def "it should generate and set the correct expiration date"() {

        given: "An OAuth2AccessToken"
            OAuth2AccessToken accessToken = new OAuth2AccessToken(expirationDate: null)
            accessToken.expiresIn = 3

        and: "A grant emitting that token"
            OAuth2Grant grant = Mock(OAuth2Grant)
            grant.grantNewAccessToken() >> Single.just(accessToken)

        and: "An OAuth2AccessTokenManager"
            OAuth2AccessTokenManager tokenManager = new OAuth2AccessTokenManager<OAuth2AccessToken>(Mock(OAuth2AccessTokenStorage))

        and: "A calendar instance"
            Calendar calendar = Calendar.getInstance()

        when: "I ask for a new access token"
            OAuth2AccessToken newToken = tokenManager.grantNewAccessToken(grant, calendar).blockingGet()

        then: "The access token should have the correct expiration date"
            newToken.expirationDate.timeInMillis == calendar.getTimeInMillis() + 3000
    }

    def "it should NOT generate and set the correct expiration date if expiresIn is null"() {

        given: "An OAuth2AccessToken"
            OAuth2AccessToken accessToken = new OAuth2AccessToken(expirationDate: null)
            accessToken.expiresIn = null

        and: "A grant emitting that token"
            OAuth2Grant grant = Mock(OAuth2Grant)
            grant.grantNewAccessToken() >> Single.just(accessToken)

        and: "An OAuth2AccessTokenManager"
            OAuth2AccessTokenManager tokenManager = new OAuth2AccessTokenManager<OAuth2AccessToken>(Mock(OAuth2AccessTokenStorage))

        when: "I ask for a new access token"
            OAuth2AccessToken newToken = tokenManager.grantNewAccessToken(grant).blockingGet()

        then: "The access token should have the NO expiration date"
            newToken.expirationDate == null
    }

    def "it should store the access token"() {

        given: "An OAuth2AccessToken"
            OAuth2AccessToken accessToken = new OAuth2AccessToken()

        and: "A grant emitting that token"
            OAuth2Grant grant = Mock(OAuth2Grant)
            grant.grantNewAccessToken() >> Single.just(accessToken)

        and: "A mock storage"
            OAuth2AccessTokenStorage storage = Mock(OAuth2AccessTokenStorage)

        and: "An OAuth2AccessTokenManager"
            OAuth2AccessTokenManager tokenManager = new OAuth2AccessTokenManager<OAuth2AccessToken>(storage)

        when: "I ask for a new access token"
            tokenManager.grantNewAccessToken(grant).blockingGet()

        then: "The storage is asked to save the token"
            1 * storage.storeAccessToken(accessToken)
    }
}

@Title("Tests for the getStorage() function of the OAuth2AccessTokenManager class")
class OAuth2AccessTokenManagerGetStorageSpecs extends Specification {

    // Scenarios

    def "it should return the correct storage"() {

        given: "A mock storage"
            OAuth2AccessTokenStorage storage = Mock(OAuth2AccessTokenStorage)

        and: "An OAuth2AccessTokenManager"
            OAuth2AccessTokenManager tokenManager = new OAuth2AccessTokenManager<OAuth2AccessToken>(storage)

        when: "I ask for the storage"
            OAuth2AccessTokenStorage receivedStorage = tokenManager.getStorage()

        then: "I have the storage I once passed via the constructor"
            receivedStorage == storage
    }
}
