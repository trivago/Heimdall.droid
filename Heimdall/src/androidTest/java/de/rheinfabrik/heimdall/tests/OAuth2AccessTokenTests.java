package de.rheinfabrik.heimdall.tests;

import com.google.gson.Gson;

import java.util.Calendar;

import de.rheinfabrik.heimdall.OAuth2AccessToken;
import de.rheinfabrik.heimdall.utils.MockitoObservablesTestCase;

import static org.assertj.core.api.Assertions.assertThat;

public class OAuth2AccessTokenTests extends MockitoObservablesTestCase {

    // Tests

    public void testIsExpiredReturnsFalseIfExpiresDateInIsNull() {

        // Given
        OAuth2AccessToken accessToken = new OAuth2AccessToken();
        accessToken.expirationDate = null;

        // When
        boolean isExpired = accessToken.isExpired();

        // Then
        assertThat(isExpired).isFalse();
    }

    public void testIsExpiredReturnsFalseIfTheExpirationDateIsAfterNow() {

        // Given
        Calendar now = Calendar.getInstance();
        now.add(Calendar.YEAR, 1);

        OAuth2AccessToken accessToken = new OAuth2AccessToken();
        accessToken.expirationDate = now;

        // When
        boolean isExpired = accessToken.isExpired();

        // Then
        assertThat(isExpired).isFalse();
    }

    public void testIsExpiredReturnsTrueIfTheExpirationDateIsBeforeNow() {

        // Given
        Calendar now = Calendar.getInstance();
        now.add(Calendar.YEAR, -1);

        OAuth2AccessToken accessToken = new OAuth2AccessToken();
        accessToken.expirationDate = now;

        // When
        boolean isExpired = accessToken.isExpired();

        // Then
        assertThat(isExpired).isTrue();
    }

    public void testSerialization() {

        // Given
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(1000);

        OAuth2AccessToken accessToken = new OAuth2AccessToken();
        accessToken.refreshToken = "12qewr23rwfsdw";
        accessToken.expiresIn = 3600;
        accessToken.accessToken = "32sdmkaüüassmfoewopr";
        accessToken.tokenType = "bearer";
        accessToken.expirationDate = calendar;

        // When
        String json = new Gson().toJson(accessToken);

        // Then
        assertThat(json).isEqualTo("{\"access_token\":\"32sdmkaüüassmfoewopr\",\"heimdall_expiration_date\":{\"year\":1970,\"month\":0,\"dayOfMonth\":1,\"hourOfDay\":1,\"minute\":0,\"second\":1},\"expires_in\":3600,\"refresh_token\":\"12qewr23rwfsdw\",\"token_type\":\"bearer\"}");
    }

    public void testDeserialization() {

        // Given
        String json = "{\"access_token\":\"32sdmkaüüassmfoewopr\",\"heimdall_expiration_date\":{\"year\":1970,\"month\":0,\"dayOfMonth\":1,\"hourOfDay\":1,\"minute\":0,\"second\":1},\"expires_in\":3600,\"refresh_token\":\"12qewr23rwfsdw\",\"token_type\":\"bearer\"}";

        // When
        OAuth2AccessToken accessToken = new Gson().fromJson(json, OAuth2AccessToken.class);

        // Then
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(1000);

        assertThat(accessToken.refreshToken).isEqualTo("12qewr23rwfsdw");
        assertThat(accessToken.expiresIn).isEqualTo(3600);
        assertThat(accessToken.accessToken).isEqualTo("32sdmkaüüassmfoewopr");
        assertThat(accessToken.tokenType).isEqualTo("bearer");
        assertThat(accessToken.expirationDate).isEqualTo(calendar);
    }
}
