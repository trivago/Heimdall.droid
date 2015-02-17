package de.rheinfabrik.heimdall.tests.grants;

import de.rheinfabrik.heimdall.grants.OAuth2ClientCredentialsGrant;
import de.rheinfabrik.heimdall.utils.MockitoObservablesTestCase;

import static org.assertj.core.api.Assertions.assertThat;

public class OAuth2ClientCredentialsGrantTests extends MockitoObservablesTestCase {

    // Tests

    public void testGrantType() {

        // Given

        // When
        String grantType = OAuth2ClientCredentialsGrant.GRANT_TYPE;

        // Then
        assertThat(grantType).isEqualTo("client_credentials");
    }

}
