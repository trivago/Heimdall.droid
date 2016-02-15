package de.rheinfabrik.heimdall.extras

import android.content.SharedPreferences
import com.andrewreitz.spock.android.AndroidSpecification
import com.google.gson.Gson
import de.rheinfabrik.heimdall.OAuth2AccessToken
import spock.lang.Title;

@Title("Tests for removeAccessToken() method in the SharedPreferencesOAuth2AccessTokenStorage class")
class SharedPreferencesOAuth2AccessTokenStorageRemoveAccessTokenSpecs extends AndroidSpecification {

    // Scenarios

    @SuppressWarnings("GroovyAssignabilityCheck")
    def "it should remove the access token from the preferences"() {

        given: "A fake editor"
            SharedPreferences.Editor editor = Mock(SharedPreferences.Editor)

        and: "A fake preferences with that editor"
            SharedPreferences preferences = Mock(SharedPreferences)
            preferences.edit() >> editor

        and: "A SharedPreferencesOAuth2AccessTokenStorage with that preferences"
            SharedPreferencesOAuth2AccessTokenStorage<OAuth2AccessToken> storage = new SharedPreferencesOAuth2AccessTokenStorage<>(preferences, OAuth2AccessToken.class)

        when: "I remove the access token"
            storage.removeAccessToken()

        then: "Remove is called on the editor"
            1 * editor.remove("OAuth2AccessToken") >> editor

        and: "Apply is called on the editor"
            1 * editor.apply() >> {}
    }
}

@Title("Tests for getStoredAccessToken() method in the SharedPreferencesOAuth2AccessTokenStorage class")
class SharedPreferencesOAuth2AccessTokenStorageGetStoredAccessTokenSpecs extends AndroidSpecification {

    // Scenarios

    def "it should load the stored access token"() {

        given: "Some JSON"
            String json = "{\"access_token\":\"2YotnFZFEjr1zCsicMWpAA\"}"

        and: "A fake preferences returning that string"
            SharedPreferences preferences = Mock(SharedPreferences)
            preferences.getString("OAuth2AccessToken", _) >> json

        and: "A SharedPreferencesOAuth2AccessTokenStorage with that preferences"
            SharedPreferencesOAuth2AccessTokenStorage<OAuth2AccessToken> storage = new SharedPreferencesOAuth2AccessTokenStorage<>(preferences, OAuth2AccessToken.class)

        when: "I ask for the access token"
            OAuth2AccessToken token = storage.getStoredAccessToken().toBlocking().value()

        then: "The token should have the correct access token"
            token.accessToken == "2YotnFZFEjr1zCsicMWpAA"
    }
}

@Title("Tests for storeAccessToken() method in the SharedPreferencesOAuth2AccessTokenStorage class")
class SharedPreferencesOAuth2AccessTokenStorageStoreAccessTokenSpecs extends AndroidSpecification {

    // Scenarios

    @SuppressWarnings("GroovyAssignabilityCheck")
    def "it should store the access token in the preferences"() {

        given: "Some JSON"
            String json = "{\"access_token\":\"2YotnFZFEjr1zCsicMWpAA\",\"expires_in\":0}"

        and: "An OAuth2AccessToken based on that JSON"
            OAuth2AccessToken accessToken = new Gson().fromJson(json, OAuth2AccessToken.class);

        and: "A fake editor"
            SharedPreferences.Editor editor = Mock(SharedPreferences.Editor)

        and: "A fake preferences with that editor"
            SharedPreferences preferences = Mock(SharedPreferences)
            preferences.edit() >> editor

        and: "A SharedPreferencesOAuth2AccessTokenStorage with that preferences"
            SharedPreferencesOAuth2AccessTokenStorage<OAuth2AccessToken> storage = new SharedPreferencesOAuth2AccessTokenStorage<>(preferences, OAuth2AccessToken.class)

        when: "I store the access token"
            storage.storeAccessToken(accessToken)

        then: "The editor is asked to save the token"
            1 * editor.putString("OAuth2AccessToken", json) >> editor

        and: "Apply is called on the editor"
            1 * editor.apply() >> {}
    }
}

@Title("Tests for hasAccessToken() method in the SharedPreferencesOAuth2AccessTokenStorage class")
class SharedPreferencesOAuth2AccessTokenStorageHasAccessTokenSpecs extends AndroidSpecification {

    // Scenarios

    @SuppressWarnings("GroovyPointlessBoolean")
    def "it should emit true if there is an access token"() {

        given: "A fake preferences saying there is a token"
            SharedPreferences preferences = Mock(SharedPreferences)
            preferences.contains("OAuth2AccessToken") >> true

        and: "A SharedPreferencesOAuth2AccessTokenStorage with that preferences"
            SharedPreferencesOAuth2AccessTokenStorage<OAuth2AccessToken> storage = new SharedPreferencesOAuth2AccessTokenStorage<>(preferences, OAuth2AccessToken.class)

        when: "I ask if there is a token"
            boolean hasToken = storage.hasAccessToken().toBlocking().value()

        then: "It should be true"
            hasToken == true
    }

    @SuppressWarnings("GroovyPointlessBoolean")
    def "it should emit false if there is NO access token"() {

        given: "A fake preferences saying there is NO token"
            SharedPreferences preferences = Mock(SharedPreferences)
            preferences.contains("OAuth2AccessToken") >> false

        and: "A SharedPreferencesOAuth2AccessTokenStorage with that preferences"
            SharedPreferencesOAuth2AccessTokenStorage<OAuth2AccessToken> storage = new SharedPreferencesOAuth2AccessTokenStorage<>(preferences, OAuth2AccessToken.class)

        when: "I ask if there is a token"
            boolean hasToken = storage.hasAccessToken().toBlocking().value()

        then: "It should be false"
            hasToken == false
    }
}
