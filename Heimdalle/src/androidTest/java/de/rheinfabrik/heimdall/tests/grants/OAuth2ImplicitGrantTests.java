package de.rheinfabrik.heimdall.tests.grants;

import de.rheinfabrik.heimdall.grants.OAuth2ImplicitGrant;
import de.rheinfabrik.heimdall.grants.OAuth2RefreshAccessTokenGrant;
import de.rheinfabrik.heimdall.utils.MockitoObservablesTestCase;

import static org.assertj.core.api.Assertions.assertThat;

public class OAuth2ImplicitGrantTests extends MockitoObservablesTestCase {

    // Tests

    public void testResponseType() {

        // Given

        // When
        String grantType = OAuth2ImplicitGrant.RESPONSE_TYPE;

        // Then
        assertThat(grantType).isEqualTo("token");
    }
}
