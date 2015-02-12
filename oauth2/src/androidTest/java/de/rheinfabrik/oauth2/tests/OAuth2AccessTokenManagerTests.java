package de.rheinfabrik.oauth2.tests;

import org.mockito.Mock;

import java.util.Calendar;

import de.rheinfabrik.oauth2.OAuth2AccessTokenManager;
import de.rheinfabrik.oauth2.OAuth2AccessTokenProvider;
import de.rheinfabrik.oauth2.OAuth2AccessTokenStorage;
import de.rheinfabrik.oauth2.implementation.StandardOAuth2AccessToken;
import de.rheinfabrik.oauth2.utils.MockitoObservablesTestCase;
import de.rheinfabrik.oauth2.utils.ValueHolder;
import rx.Observable;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class OAuth2AccessTokenManagerTests extends MockitoObservablesTestCase {

    // Members

    @Mock
    OAuth2AccessTokenStorage<StandardOAuth2AccessToken> mStorage;

    @Mock
    OAuth2AccessTokenProvider<StandardOAuth2AccessToken> mProvider;

    // Test lifecycle

    @Override
    protected void tearDown() throws Exception {
        reset(mStorage, mProvider);

        super.tearDown();
    }

    // Tests

    public void testThatHasAccessTokenEmitsTrueIfThereIsAnAccessTokenStored() {

        // Given
        when(mStorage.hasAccessToken()).thenReturn(Observable.just(true));

        OAuth2AccessTokenManager<StandardOAuth2AccessToken> manager = new OAuth2AccessTokenManager<>(mStorage, mProvider);

        ValueHolder<Boolean> booleanValueHolder = new ValueHolder<>();

        // When
        subscribe(manager.hasAccessToken(), hasAccessToken -> booleanValueHolder.value = hasAccessToken);

        // Then
        assertThat(booleanValueHolder.value).isTrue();
    }

    public void testThatHasAccessTokenEmitsFalseIfThereIsNoAccessTokenStored() {

        // Given
        when(mStorage.hasAccessToken()).thenReturn(Observable.just(false));

        OAuth2AccessTokenManager<StandardOAuth2AccessToken> manager = new OAuth2AccessTokenManager<>(mStorage, mProvider);

        ValueHolder<Boolean> booleanValueHolder = new ValueHolder<>();

        // When
        subscribe(manager.hasAccessToken(), hasAccessToken -> booleanValueHolder.value = hasAccessToken);

        // Then
        assertThat(booleanValueHolder.value).isFalse();
    }

    public void testGetNewAccessTokenEmitsReceivedAccessToken() {

        // Given
        StandardOAuth2AccessToken accessToken = mock(StandardOAuth2AccessToken.class);
        when(accessToken.getExpirationDate()).thenReturn(Calendar.getInstance());
        when(accessToken.getRefreshToken()).thenReturn("refresh token");

        when(mProvider.getNewAccessToken(eq("username"), eq("password"))).thenReturn(Observable.just(accessToken));

        OAuth2AccessTokenManager<StandardOAuth2AccessToken> manager = new OAuth2AccessTokenManager<>(mStorage, mProvider);

        ValueHolder<StandardOAuth2AccessToken> accessTokenValueHolder = new ValueHolder<>();

        // When
        subscribe(manager.getNewAccessToken("username", "password"), testOAuth2AccessToken -> accessTokenValueHolder.value = testOAuth2AccessToken);

        // Then
        assertThat(accessTokenValueHolder.value).isEqualTo(accessToken);
    }

    public void testGetNewAccessTokenStoresReceivedAccessToken() {

        // Given
        StandardOAuth2AccessToken accessToken = mock(StandardOAuth2AccessToken.class);
        when(accessToken.getExpirationDate()).thenReturn(Calendar.getInstance());
        when(accessToken.getRefreshToken()).thenReturn("refresh token");

        when(mProvider.getNewAccessToken(eq("username"), eq("password"))).thenReturn(Observable.just(accessToken));

        OAuth2AccessTokenManager<StandardOAuth2AccessToken> manager = new OAuth2AccessTokenManager<>(mStorage, mProvider);

        ValueHolder<StandardOAuth2AccessToken> accessTokenValueHolder = new ValueHolder<>();

        // When
        subscribe(manager.getNewAccessToken("username", "password"), testOAuth2AccessToken -> accessTokenValueHolder.value = testOAuth2AccessToken);

        // Then
        verify(mStorage).storeAccessToken(eq(accessTokenValueHolder.value));
    }

    public void testGetNewAccessTokenGeneratesExpirationDate() {

        // Given
        StandardOAuth2AccessToken accessToken = mock(StandardOAuth2AccessToken.class);
        when(accessToken.getExpirationDate()).thenReturn(Calendar.getInstance());
        when(accessToken.getRefreshToken()).thenReturn("refresh token");

        when(mProvider.getNewAccessToken(eq("username"), eq("password"))).thenReturn(Observable.just(accessToken));

        OAuth2AccessTokenManager<StandardOAuth2AccessToken> manager = new OAuth2AccessTokenManager<>(mStorage, mProvider);

        ValueHolder<StandardOAuth2AccessToken> accessTokenValueHolder = new ValueHolder<>();

        // When
        subscribe(manager.getNewAccessToken("username", "password"), testOAuth2AccessToken -> accessTokenValueHolder.value = testOAuth2AccessToken);

        // Then
        verify(accessToken).generateExpirationDate(any());
    }

    public void testGetNewAccessTokenPropagatesError() {

        // Given
        when(mProvider.getNewAccessToken(eq("username"), eq("password"))).thenReturn(Observable.error(new Throwable("Error")));

        OAuth2AccessTokenManager<StandardOAuth2AccessToken> manager = new OAuth2AccessTokenManager<>(mStorage, mProvider);

        ValueHolder<Throwable> throwableValueHolder = new ValueHolder<>();

        // When
        subscribe(manager.getNewAccessToken("username", "password"), null, throwable -> throwableValueHolder.value = throwable);

        // Then
        assertThat(throwableValueHolder.value).hasMessage("Error");
    }

    public void testGetValidAccessTokenThrowsExceptionIfThereIsNoAccessToken() {

        // Given
        when(mStorage.hasAccessToken()).thenReturn(Observable.just(false));

        OAuth2AccessTokenManager<StandardOAuth2AccessToken> manager = new OAuth2AccessTokenManager<>(mStorage, mProvider);
        ValueHolder<Throwable> throwableValueHolder = new ValueHolder<>();

        // When
        subscribe(manager.getValidAccessToken(), null, throwable -> throwableValueHolder.value = throwable);

        // Then
        assertThat(throwableValueHolder.value).hasMessage("No access token found.");
    }

    public void testGetValidAccessTokenEmitsAccessTokenIfNotExpired() {

        // Given
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.YEAR, 1);

        StandardOAuth2AccessToken accessToken = mock(StandardOAuth2AccessToken.class);
        when(accessToken.getExpirationDate()).thenReturn(calendar);
        when(accessToken.getRefreshToken()).thenReturn("refresh token");

        when(mStorage.hasAccessToken()).thenReturn(Observable.just(true));
        when(mStorage.getStoredAccessToken()).thenReturn(Observable.just(accessToken));

        ValueHolder<StandardOAuth2AccessToken> accessTokenValueHolder = new ValueHolder<>();

        OAuth2AccessTokenManager<StandardOAuth2AccessToken> manager = new OAuth2AccessTokenManager<>(mStorage, mProvider);

        // When
        subscribe(manager.getValidAccessToken(), testOAuth2AccessToken -> accessTokenValueHolder.value = testOAuth2AccessToken);

        // Then
        assertThat(accessTokenValueHolder.value).isEqualTo(accessToken);
    }

    public void testGetValidAccessTokenStoresTheRefreshedAccessToken() {

        // Given
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.YEAR, -1);

        StandardOAuth2AccessToken accessToken = mock(StandardOAuth2AccessToken.class);
        when(accessToken.getExpirationDate()).thenReturn(calendar);
        when(accessToken.getRefreshToken()).thenReturn("refresh token");

        StandardOAuth2AccessToken refreshedAccessToken = mock(StandardOAuth2AccessToken.class);
        when(accessToken.getAccessToken()).thenReturn("access token");

        when(mStorage.hasAccessToken()).thenReturn(Observable.just(true));
        when(mStorage.getStoredAccessToken()).thenReturn(Observable.just(accessToken));
        when(mProvider.refreshAccessToken(eq("refresh token"))).thenReturn(Observable.just(refreshedAccessToken));

        OAuth2AccessTokenManager<StandardOAuth2AccessToken> manager = new OAuth2AccessTokenManager<>(mStorage, mProvider);

        // When
        subscribe(manager.getValidAccessToken());

        // Then
        verify(mStorage).storeAccessToken(eq(refreshedAccessToken));
    }


    public void testGetValidAccessTokenRefreshesAccessTokenIfItIsExpired() {

        // Given
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.YEAR, -1);

        StandardOAuth2AccessToken accessToken = mock(StandardOAuth2AccessToken.class);
        when(accessToken.getExpirationDate()).thenReturn(calendar);
        when(accessToken.getRefreshToken()).thenReturn("refresh token");

        StandardOAuth2AccessToken refreshedAccessToken = mock(StandardOAuth2AccessToken.class);
        when(accessToken.getAccessToken()).thenReturn("access token");

        when(mStorage.hasAccessToken()).thenReturn(Observable.just(true));
        when(mStorage.getStoredAccessToken()).thenReturn(Observable.just(accessToken));
        when(mProvider.refreshAccessToken(eq("refresh token"))).thenReturn(Observable.just(refreshedAccessToken));

        ValueHolder<StandardOAuth2AccessToken> accessTokenValueHolder = new ValueHolder<>();

        OAuth2AccessTokenManager<StandardOAuth2AccessToken> manager = new OAuth2AccessTokenManager<>(mStorage, mProvider);

        // When
        subscribe(manager.getValidAccessToken(), testOAuth2AccessToken -> accessTokenValueHolder.value = testOAuth2AccessToken);

        // Then
        assertThat(accessTokenValueHolder.value).isEqualTo(refreshedAccessToken);
    }

    public void testGetValidAccessTokenDoesNotRefreshesIfThereIsNoRefreshToken() {

        // Given
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.YEAR, -1);

        StandardOAuth2AccessToken accessToken = mock(StandardOAuth2AccessToken.class);
        when(accessToken.getExpirationDate()).thenReturn(calendar);
        when(accessToken.getRefreshToken()).thenReturn(null);

        when(mStorage.hasAccessToken()).thenReturn(Observable.just(true));
        when(mStorage.getStoredAccessToken()).thenReturn(Observable.just(accessToken));

        ValueHolder<StandardOAuth2AccessToken> accessTokenValueHolder = new ValueHolder<>();

        OAuth2AccessTokenManager<StandardOAuth2AccessToken> manager = new OAuth2AccessTokenManager<>(mStorage, mProvider);

        // When
        subscribe(manager.getValidAccessToken(), testOAuth2AccessToken -> accessTokenValueHolder.value = testOAuth2AccessToken);

        // Then
        assertThat(accessTokenValueHolder.value).isEqualTo(accessToken);
    }

    public void testGetValidAccessTokenDoesNotRefreshesIfTheExpirationDateIsNull() {

        // Given
        StandardOAuth2AccessToken accessToken = mock(StandardOAuth2AccessToken.class);
        when(accessToken.getExpirationDate()).thenReturn(null);
        when(accessToken.getRefreshToken()).thenReturn("lê token");

        when(mStorage.hasAccessToken()).thenReturn(Observable.just(true));
        when(mStorage.getStoredAccessToken()).thenReturn(Observable.just(accessToken));

        ValueHolder<StandardOAuth2AccessToken> accessTokenValueHolder = new ValueHolder<>();

        OAuth2AccessTokenManager<StandardOAuth2AccessToken> manager = new OAuth2AccessTokenManager<>(mStorage, mProvider);

        // When
        subscribe(manager.getValidAccessToken(), testOAuth2AccessToken -> accessTokenValueHolder.value = testOAuth2AccessToken);

        // Then
        assertThat(accessTokenValueHolder.value).isEqualTo(accessToken);
    }
}