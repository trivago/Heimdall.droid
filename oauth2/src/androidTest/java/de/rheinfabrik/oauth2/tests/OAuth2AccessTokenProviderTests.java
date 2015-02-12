package de.rheinfabrik.oauth2.tests;

import de.rheinfabrik.oauth2.OAuth2AccessTokenProvider;
import de.rheinfabrik.oauth2.utils.MockitoObservablesTestCase;

import static org.assertj.core.api.Assertions.assertThat;

public class OAuth2AccessTokenProviderTests extends MockitoObservablesTestCase {

    // Tests

    public void testThatPasswordGrantTypesOAuth2NameIsCorrect() {

        // Given
        OAuth2AccessTokenProvider.GrantType grantType = OAuth2AccessTokenProvider.GrantType.PASSWORD;

        // When & Then
        assertThat(grantType.getOAuth2Name()).isEqualTo("password");
    }

    public void testThatRefreshTokenGrantTypesOAuth2NameIsCorrect() {

        // Given
        OAuth2AccessTokenProvider.GrantType grantType = OAuth2AccessTokenProvider.GrantType.REFRESH_TOKEN;

        // When & Then
        assertThat(grantType.getOAuth2Name()).isEqualTo("refresh_token");
    }
}
