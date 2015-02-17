package de.rheinfabrik.heimdall.tests.grants;

import de.rheinfabrik.heimdall.grants.OAuth2ResourceOwnerPasswordCredentialsGrant;
import de.rheinfabrik.heimdall.utils.MockitoObservablesTestCase;

import static org.assertj.core.api.Assertions.assertThat;

public class OAuth2ResourceOwnerPasswordCredentialsGrantTests extends MockitoObservablesTestCase {

    // Tests

    public void testGrantType() {

        // Given

        // When
        String grantType = OAuth2ResourceOwnerPasswordCredentialsGrant.GRANT_TYPE;

        // Then
        assertThat(grantType).isEqualTo("password");
    }

}
