package de.rheinfabrik.oauth2thing.tests.rx;

import android.content.SharedPreferences;

import org.mockito.Mock;
import org.mockito.Mockito;

import java.util.Calendar;

import de.rheinfabrik.oauth2thing.RxSharedPreferencesOAuth2AccessTokenStorage;
import de.rheinfabrik.oauth2thing.tests.DummyOAuth2AccessToken;
import de.rheinfabrik.oauth2thing.utils.MockitoObservablesTestCase;
import de.rheinfabrik.oauth2thing.utils.ValueHolder;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

public class RxSharedPreferencesOAuth2AccessTokenStorageTests extends MockitoObservablesTestCase {

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
        RxSharedPreferencesOAuth2AccessTokenStorage<DummyOAuth2AccessToken> storage = new RxSharedPreferencesOAuth2AccessTokenStorage<>(mSharedPreferences, DummyOAuth2AccessToken.class);
        when(mSharedPreferences.getString(eq("OAuth2AccessToken"), any())).thenReturn(null);

        ValueHolder<Throwable> throwableValueHolder = new ValueHolder<>();

        // When
        subscribe(storage.getStoredAccessToken(), null, throwable -> throwableValueHolder.value = throwable);

        // Then
        assertThat(throwableValueHolder.value).hasMessage("No access token found.");
    }

    public void testSavingAccessTokenInvokesEditorWithCorrectJSON() {

        // Given
        RxSharedPreferencesOAuth2AccessTokenStorage<DummyOAuth2AccessToken> storage = new RxSharedPreferencesOAuth2AccessTokenStorage<>(mSharedPreferences, DummyOAuth2AccessToken.class);

        DummyOAuth2AccessToken accessToken = new DummyOAuth2AccessToken();
        accessToken.something = "some something";
        accessToken.expirationDate = Calendar.getInstance();
        accessToken.expirationDate.setTimeInMillis(1000);
        accessToken.refreshToken = "1234";

        // When
        storage.storeAccessToken(accessToken);

        // Then
        Mockito.verify(mEditor).putString(eq("OAuth2AccessToken"), eq("{\"expirationDate\":{\"year\":1970,\"month\":0,\"dayOfMonth\":1,\"hourOfDay\":1,\"minute\":0,\"second\":1},\"refreshToken\":\"1234\",\"something\":\"some something\"}"));
        Mockito.verify(mEditor).apply();
    }

    public void testGetStoredAccessTokenReturnsTheCorrectTokenIfThereIsOne() {

        // Given
        when(mSharedPreferences.getString(eq("OAuth2AccessToken"), any())).thenReturn("{\"expirationDate\":{\"year\":1970,\"month\":0,\"dayOfMonth\":1,\"hourOfDay\":1,\"minute\":0,\"second\":1},\"refreshToken\":\"1234\",\"something\":\"some something\"}");

        RxSharedPreferencesOAuth2AccessTokenStorage<DummyOAuth2AccessToken> storage = new RxSharedPreferencesOAuth2AccessTokenStorage<>(mSharedPreferences, DummyOAuth2AccessToken.class);
        ValueHolder<DummyOAuth2AccessToken> accessTokenValueHolder = new ValueHolder<>();

        // When
        subscribe(storage.getStoredAccessToken(), testOAuth2AccessToken -> accessTokenValueHolder.value = testOAuth2AccessToken);

        // Then -> use equals
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(1000);

        assertThat(accessTokenValueHolder.value).isNotNull();
        assertThat(accessTokenValueHolder.value.getExpirationDate().compareTo(calendar)).isEqualTo(0);
        assertThat(accessTokenValueHolder.value.getRefreshToken()).isEqualTo("1234");
        assertThat(accessTokenValueHolder.value.something).isEqualTo("some something");
    }

    public void testThatHasTokenReturnsTrueIfATokenExists() {

        // Given
        when(mSharedPreferences.contains(eq("OAuth2AccessToken"))).thenReturn(true);

        RxSharedPreferencesOAuth2AccessTokenStorage<DummyOAuth2AccessToken> storage = new RxSharedPreferencesOAuth2AccessTokenStorage<>(mSharedPreferences, DummyOAuth2AccessToken.class);
        ValueHolder<Boolean> booleanValueHolder = new ValueHolder<>();

        // When
        subscribe(storage.hasAccessToken(), hasToken -> booleanValueHolder.value = hasToken);

        // Then
        assertThat(booleanValueHolder.value).isTrue();
    }

    public void testThatHasTokenReturnsFalseIfNoTokenExists() {

        // Given
        when(mSharedPreferences.contains(eq("OAuth2AccessToken"))).thenReturn(false);

        RxSharedPreferencesOAuth2AccessTokenStorage<DummyOAuth2AccessToken> storage = new RxSharedPreferencesOAuth2AccessTokenStorage<>(mSharedPreferences, DummyOAuth2AccessToken.class);
        ValueHolder<Boolean> booleanValueHolder = new ValueHolder<>();

        // When
        subscribe(storage.hasAccessToken(), hasToken -> booleanValueHolder.value = hasToken);

        // Then
        assertThat(booleanValueHolder.value).isFalse();
    }

}