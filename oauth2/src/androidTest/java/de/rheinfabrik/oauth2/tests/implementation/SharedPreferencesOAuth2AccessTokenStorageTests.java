package de.rheinfabrik.oauth2.tests.implementation;

import android.content.SharedPreferences;

import com.google.gson.Gson;

import org.mockito.Mock;
import org.mockito.Mockito;

import de.rheinfabrik.oauth2.implementation.SharedPreferencesOAuth2AccessTokenStorage;
import de.rheinfabrik.oauth2.implementation.StandardOAuth2AccessToken;
import de.rheinfabrik.oauth2.utils.MockitoObservablesTestCase;
import de.rheinfabrik.oauth2.utils.ValueHolder;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

public class SharedPreferencesOAuth2AccessTokenStorageTests extends MockitoObservablesTestCase {

    // Members

    @Mock
    private SharedPreferences mSharedPreferences;

    @Mock
    private SharedPreferences.Editor mEditor;

    // Test lifecycle

    @Override
    protected void setUp() throws Exception {
        super.setUp();

        when(mEditor.commit()).thenReturn(false);
        when(mEditor.putString(anyString(), anyString())).thenReturn(mEditor);
        doNothing().when(mEditor).apply();

        when(mSharedPreferences.edit()).thenReturn(mEditor);
    }

    @Override
    protected void tearDown() throws Exception {
        Mockito.reset(mSharedPreferences, mEditor);

        super.tearDown();
    }

    // Tests

    public void testGetStoredAccessTokenThrowsExceptionIfAccessTokenIsNull() {

        // Given
        SharedPreferencesOAuth2AccessTokenStorage<StandardOAuth2AccessToken> storage = new SharedPreferencesOAuth2AccessTokenStorage<>(mSharedPreferences, StandardOAuth2AccessToken.class);
        when(mSharedPreferences.getString(eq("OAuth2AccessToken"), any())).thenReturn(null);

        ValueHolder<Throwable> throwableValueHolder = new ValueHolder<>();

        // When
        subscribe(storage.getStoredAccessToken(), null, throwable -> throwableValueHolder.value = throwable);

        // Then
        assertThat(throwableValueHolder.value).hasMessage("No access token found.");
    }

    public void testSavingAccessTokenInvokesEditorWithCorrectJSON() {

        // Given
        String json = "{\"access_token\":\"2YotnFZFEjr1zCsicMWpAA\",\"refresh_token\":\"tGzv3JOkF0XG5Qx2TlKWIA\",\"token_type\":\"example\",\"expires_in\":3600}";
        SharedPreferencesOAuth2AccessTokenStorage<StandardOAuth2AccessToken> storage = new SharedPreferencesOAuth2AccessTokenStorage<>(mSharedPreferences, StandardOAuth2AccessToken.class);

        StandardOAuth2AccessToken accessToken = new Gson().fromJson(json, StandardOAuth2AccessToken.class);

        // When
        storage.storeAccessToken(accessToken);

        // Then
        Mockito.verify(mEditor).putString(eq("OAuth2AccessToken"), eq(json));
        Mockito.verify(mEditor).apply();
    }

    public void testGetStoredAccessTokenReturnsTheCorrectTokenIfThereIsOne() {

        // Given
        String json = "{\"access_token\":\"2YotnFZFEjr1zCsicMWpAA\", \"token_type\":\"example\",\"expires_in\":3600,\"refresh_token\":\"tGzv3JOkF0XG5Qx2TlKWIA\"}";
        when(mSharedPreferences.getString(eq("OAuth2AccessToken"), any())).thenReturn(json);

        SharedPreferencesOAuth2AccessTokenStorage<StandardOAuth2AccessToken> storage = new SharedPreferencesOAuth2AccessTokenStorage<>(mSharedPreferences, StandardOAuth2AccessToken.class);
        ValueHolder<StandardOAuth2AccessToken> accessTokenValueHolder = new ValueHolder<>();

        // When
        subscribe(storage.getStoredAccessToken(), testOAuth2AccessToken -> accessTokenValueHolder.value = testOAuth2AccessToken);

        // Then
        assertThat(accessTokenValueHolder.value).isNotNull();
        assertThat(accessTokenValueHolder.value.getRefreshToken()).isEqualTo("tGzv3JOkF0XG5Qx2TlKWIA");
    }

    public void testThatHasTokenReturnsTrueIfATokenExists() {

        // Given
        when(mSharedPreferences.contains(eq("OAuth2AccessToken"))).thenReturn(true);

        SharedPreferencesOAuth2AccessTokenStorage<StandardOAuth2AccessToken> storage = new SharedPreferencesOAuth2AccessTokenStorage<>(mSharedPreferences, StandardOAuth2AccessToken.class);
        ValueHolder<Boolean> booleanValueHolder = new ValueHolder<>();

        // When
        subscribe(storage.hasAccessToken(), hasToken -> booleanValueHolder.value = hasToken);

        // Then
        assertThat(booleanValueHolder.value).isTrue();
    }

    public void testThatHasTokenReturnsFalseIfNoTokenExists() {

        // Given
        when(mSharedPreferences.contains(eq("OAuth2AccessToken"))).thenReturn(false);

        SharedPreferencesOAuth2AccessTokenStorage<StandardOAuth2AccessToken> storage = new SharedPreferencesOAuth2AccessTokenStorage<>(mSharedPreferences, StandardOAuth2AccessToken.class);
        ValueHolder<Boolean> booleanValueHolder = new ValueHolder<>();

        // When
        subscribe(storage.hasAccessToken(), hasToken -> booleanValueHolder.value = hasToken);

        // Then
        assertThat(booleanValueHolder.value).isFalse();
    }

}