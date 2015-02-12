package de.rheinfabrik.oauth2.tests.implementation;

import com.google.gson.Gson;

import org.mockito.Mockito;

import java.util.Calendar;

import de.rheinfabrik.oauth2.implementation.StandardOAuth2AccessToken;
import de.rheinfabrik.oauth2.utils.MockitoObservablesTestCase;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doCallRealMethod;
import static org.mockito.Mockito.when;

public class StandardOAuth2AccessTokenTests extends MockitoObservablesTestCase {

    // Tests

    public void testGenerateExpirationDateBuildsCorrectDate() {

        // Given
        StandardOAuth2AccessToken token = Mockito.mock(StandardOAuth2AccessToken.class);
        when(token.getExpiresIn()).thenReturn(2000);
        when(token.getExpirationDate()).thenCallRealMethod();
        doCallRealMethod().when(token).generateExpirationDate(any());

        Calendar calendar = Calendar.getInstance();

        // When
        token.generateExpirationDate(calendar);

        // Then
        calendar.add(Calendar.SECOND, 2000);

        assertThat(token.getExpirationDate()).isEqualTo(calendar);
    }

    public void testGenerateExpirationDoesNotCrashIfNowIsNull() {

        // Given
        StandardOAuth2AccessToken token = Mockito.mock(StandardOAuth2AccessToken.class);

        // When & Then NOT
        try {
            token.generateExpirationDate(null);
        } catch (Exception exception) {
            fail("Crash while passing null to generateExpirationDate.");
        }
    }

    public void testThatAccessTokenIsBuildCorrectlyFromGSON() {

        // Given
        String json = "{\"access_token\":\"2YotnFZFEjr1zCsicMWpAA\", \"token_type\":\"example\",\"expires_in\":3600,\"refresh_token\":\"tGzv3JOkF0XG5Qx2TlKWIA\"}";

        // When
        StandardOAuth2AccessToken readAccessToken = new Gson().fromJson(json, StandardOAuth2AccessToken.class);

        // Then
        assertThat(readAccessToken.getAccessToken()).isEqualTo("2YotnFZFEjr1zCsicMWpAA");
        assertThat(readAccessToken.getTokenType()).isEqualTo("example");
        assertThat(readAccessToken.getExpiresIn()).isEqualTo(3600);
        assertThat(readAccessToken.getRefreshToken()).isEqualTo("tGzv3JOkF0XG5Qx2TlKWIA");
    }

}
