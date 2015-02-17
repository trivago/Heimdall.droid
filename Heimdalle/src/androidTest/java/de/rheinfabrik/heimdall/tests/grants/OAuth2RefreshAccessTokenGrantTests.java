package de.rheinfabrik.heimdall.tests.grants;

import de.rheinfabrik.heimdall.grants.OAuth2RefreshAccessTokenGrant;
import de.rheinfabrik.heimdall.utils.MockitoObservablesTestCase;

import static org.assertj.core.api.Assertions.assertThat;

public abstract class OAuth2RefreshAccessTokenGrantTests extends MockitoObservablesTestCase {

    // Tests

    public void testGrantType() {

        // Given

        // When
        String grantType = OAuth2RefreshAccessTokenGrant.GRANT_TYPE;

        // Then
        assertThat(grantType).isEqualTo("refresh_token");
    }
}
