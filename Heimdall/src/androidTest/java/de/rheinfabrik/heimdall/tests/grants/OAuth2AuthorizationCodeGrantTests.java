package de.rheinfabrik.heimdall.tests.grants;

import de.rheinfabrik.heimdall.grants.OAuth2AuthorizationCodeGrant;
import de.rheinfabrik.heimdall.utils.MockitoObservablesTestCase;

import static org.assertj.core.api.Assertions.assertThat;

public class OAuth2AuthorizationCodeGrantTests extends MockitoObservablesTestCase {

    // Tests

    public void testResponseType() {

        // Given

        // When
        String grantType = OAuth2AuthorizationCodeGrant.RESPONSE_TYPE;

        // Then
        assertThat(grantType).isEqualTo("code");
    }
}