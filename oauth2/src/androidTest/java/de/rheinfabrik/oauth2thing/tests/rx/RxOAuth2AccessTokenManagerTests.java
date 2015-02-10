package de.rheinfabrik.oauth2thing.tests.rx;

import org.mockito.Mock;

import java.util.Calendar;

import de.rheinfabrik.oauth2thing.RxOAuth2AccessTokenManager;
import de.rheinfabrik.oauth2thing.RxOAuth2AccessTokenProvider;
import de.rheinfabrik.oauth2thing.RxOAuth2AccessTokenStorage;
import de.rheinfabrik.oauth2thing.tests.DummyOAuth2AccessToken;
import de.rheinfabrik.oauth2thing.utils.MockitoObservablesTestCase;
import de.rheinfabrik.oauth2thing.utils.ValueHolder;
import rx.Observable;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class RxOAuth2AccessTokenManagerTests extends MockitoObservablesTestCase {

    // Members

    @Mock
    RxOAuth2AccessTokenStorage<DummyOAuth2AccessToken> mStorage;

    @Mock
    RxOAuth2AccessTokenProvider<DummyOAuth2AccessToken> mProvider;

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

        RxOAuth2AccessTokenManager<DummyOAuth2AccessToken> manager = new RxOAuth2AccessTokenManager<>("id", mStorage, mProvider);

        ValueHolder<Boolean> booleanValueHolder = new ValueHolder<>();

        // When
        subscribe(manager.hasAccessToken(), hasAccessToken -> booleanValueHolder.value = hasAccessToken);

        // Then
        assertThat(booleanValueHolder.value).isTrue();
    }

    public void testThatHasAccessTokenEmitsFalseIfThereIsNoAccessTokenStored() {

        // Given
        when(mStorage.hasAccessToken()).thenReturn(Observable.just(false));

        RxOAuth2AccessTokenManager<DummyOAuth2AccessToken> manager = new RxOAuth2AccessTokenManager<>("id", mStorage, mProvider);

        ValueHolder<Boolean> booleanValueHolder = new ValueHolder<>();

        // When
        subscribe(manager.hasAccessToken(), hasAccessToken -> booleanValueHolder.value = hasAccessToken);

        // Then
        assertThat(booleanValueHolder.value).isFalse();
    }

    public void testGetNewAccessTokenEmitsReceivedAccessToken() {

        // Given
        DummyOAuth2AccessToken accessToken = new DummyOAuth2AccessToken();
        accessToken.something = "something";
        accessToken.expirationDate = Calendar.getInstance();
        accessToken.refreshToken = "refresh token";
        when(mProvider.getNewAccessToken(eq("username"), eq("password"), eq("id"))).thenReturn(Observable.just(accessToken));

        RxOAuth2AccessTokenManager<DummyOAuth2AccessToken> manager = new RxOAuth2AccessTokenManager<>("id", mStorage, mProvider);

        ValueHolder<DummyOAuth2AccessToken> accessTokenValueHolder = new ValueHolder<>();

        // When
        subscribe(manager.getNewAccessToken("username", "password"), testOAuth2AccessToken -> accessTokenValueHolder.value = testOAuth2AccessToken);

        // Then
        assertThat(accessTokenValueHolder.value).isEqualTo(accessToken);
    }

    public void testGetNewAccessTokenStoresReceivedAccessToken() {

        // Given
        DummyOAuth2AccessToken accessToken = new DummyOAuth2AccessToken();
        accessToken.something = "something";
        accessToken.expirationDate = Calendar.getInstance();
        accessToken.refreshToken = "refresh token";
        when(mProvider.getNewAccessToken(eq("username"), eq("password"), eq("id"))).thenReturn(Observable.just(accessToken));

        RxOAuth2AccessTokenManager<DummyOAuth2AccessToken> manager = new RxOAuth2AccessTokenManager<>("id", mStorage, mProvider);

        ValueHolder<DummyOAuth2AccessToken> accessTokenValueHolder = new ValueHolder<>();

        // When
        subscribe(manager.getNewAccessToken("username", "password"), testOAuth2AccessToken -> accessTokenValueHolder.value = testOAuth2AccessToken);

        // Then
        verify(mStorage).storeAccessToken(eq(accessTokenValueHolder.value));
    }

    public void testGetNewAccessTokenPropagatesError() {

        // Given
        when(mProvider.getNewAccessToken(eq("username"), eq("password"), eq("id"))).thenReturn(Observable.error(new Throwable("Error")));

        RxOAuth2AccessTokenManager<DummyOAuth2AccessToken> manager = new RxOAuth2AccessTokenManager<>("id", mStorage, mProvider);

        ValueHolder<Throwable> throwableValueHolder = new ValueHolder<>();

        // When
        subscribe(manager.getNewAccessToken("username", "password"), null, throwable -> throwableValueHolder.value = throwable);

        // Then
        assertThat(throwableValueHolder.value).hasMessage("Error");
    }

    public void testGetValidAccessTokenThrowsExceptionIfThereIsNoAccessToken() {

        // Given
        when(mStorage.hasAccessToken()).thenReturn(Observable.just(false));

        RxOAuth2AccessTokenManager<DummyOAuth2AccessToken> manager = new RxOAuth2AccessTokenManager<>("id", mStorage, mProvider);
        ValueHolder<Throwable> throwableValueHolder = new ValueHolder<>();

        // When
        subscribe(manager.getValidAccessToken(), null, throwable -> throwableValueHolder.value = throwable);

        // Then
        assertThat(throwableValueHolder.value).hasMessage("No access token found.");
    }

    public void testGetValidAccessTokenEmitsAccessTokenIfNotExpired() {

        // Given
        DummyOAuth2AccessToken accessToken = new DummyOAuth2AccessToken();
        accessToken.something = "some something";
        accessToken.expirationDate = Calendar.getInstance();
        accessToken.expirationDate.add(Calendar.MONTH, 1);
        accessToken.refreshToken = "1234";

        when(mStorage.hasAccessToken()).thenReturn(Observable.just(true));
        when(mStorage.getStoredAccessToken()).thenReturn(Observable.just(accessToken));

        ValueHolder<DummyOAuth2AccessToken> accessTokenValueHolder = new ValueHolder<>();

        RxOAuth2AccessTokenManager<DummyOAuth2AccessToken> manager = new RxOAuth2AccessTokenManager<>("id", mStorage, mProvider);

        // When
        subscribe(manager.getValidAccessToken(), testOAuth2AccessToken -> accessTokenValueHolder.value = testOAuth2AccessToken);

        // Then
        assertThat(accessTokenValueHolder.value).isEqualTo(accessToken);
    }

    public void testGetValidAccessTokenStoresTheRefreshedAccessToken() {

        // Given
        DummyOAuth2AccessToken accessToken = new DummyOAuth2AccessToken();
        accessToken.something = "some something";
        accessToken.expirationDate = Calendar.getInstance();
        accessToken.expirationDate.setTimeInMillis(0);
        accessToken.refreshToken = "1234";

        DummyOAuth2AccessToken refreshedAccessToken = new DummyOAuth2AccessToken();
        refreshedAccessToken.something = "some something";
        refreshedAccessToken.expirationDate = Calendar.getInstance();
        refreshedAccessToken.refreshToken = "1234";

        when(mStorage.hasAccessToken()).thenReturn(Observable.just(true));
        when(mStorage.getStoredAccessToken()).thenReturn(Observable.just(accessToken));
        when(mProvider.refreshAccessToken(eq("1234"))).thenReturn(Observable.just(refreshedAccessToken));

        RxOAuth2AccessTokenManager<DummyOAuth2AccessToken> manager = new RxOAuth2AccessTokenManager<>("id", mStorage, mProvider);

        // When
        subscribe(manager.getValidAccessToken());

        // Then
        verify(mStorage).storeAccessToken(eq(refreshedAccessToken));
    }

    public void testGetValidAccessTokenRefreshesAccessTokenIfItIsExpired() {

        // Given
        DummyOAuth2AccessToken accessToken = new DummyOAuth2AccessToken();
        accessToken.something = "some something";
        accessToken.expirationDate = Calendar.getInstance();
        accessToken.expirationDate.add(Calendar.MONTH, -1);
        accessToken.refreshToken = "1234";

        DummyOAuth2AccessToken refreshedAccessToken = new DummyOAuth2AccessToken();
        refreshedAccessToken.something = "some something";
        refreshedAccessToken.expirationDate = Calendar.getInstance();
        refreshedAccessToken.refreshToken = "1234";

        when(mStorage.hasAccessToken()).thenReturn(Observable.just(true));
        when(mStorage.getStoredAccessToken()).thenReturn(Observable.just(accessToken));
        when(mProvider.refreshAccessToken(eq("1234"))).thenReturn(Observable.just(refreshedAccessToken));

        ValueHolder<DummyOAuth2AccessToken> accessTokenValueHolder = new ValueHolder<>();

        RxOAuth2AccessTokenManager<DummyOAuth2AccessToken> manager = new RxOAuth2AccessTokenManager<>("id", mStorage, mProvider);

        // When
        subscribe(manager.getValidAccessToken(), testOAuth2AccessToken -> accessTokenValueHolder.value = testOAuth2AccessToken);

        // Then
        assertThat(accessTokenValueHolder.value).isEqualTo(refreshedAccessToken);
    }

    public void testGetValidAccessTokenDoesNotRefreshesIfThereIsNoRefreshToken() {

        // Given
        DummyOAuth2AccessToken accessToken = new DummyOAuth2AccessToken();
        accessToken.something = "some something";
        accessToken.expirationDate = Calendar.getInstance();
        accessToken.expirationDate.add(Calendar.MONTH, -1);
        accessToken.refreshToken = null;

        when(mStorage.hasAccessToken()).thenReturn(Observable.just(true));
        when(mStorage.getStoredAccessToken()).thenReturn(Observable.just(accessToken));

        ValueHolder<DummyOAuth2AccessToken> accessTokenValueHolder = new ValueHolder<>();

        RxOAuth2AccessTokenManager<DummyOAuth2AccessToken> manager = new RxOAuth2AccessTokenManager<>("id", mStorage, mProvider);

        // When
        subscribe(manager.getValidAccessToken(), testOAuth2AccessToken -> accessTokenValueHolder.value = testOAuth2AccessToken);

        // Then
        assertThat(accessTokenValueHolder.value).isEqualTo(accessToken);
    }

    public void testGetValidAccessTokenDoesNotRefreshesIfTheExpirationDateIsNull() {

        // Given
        DummyOAuth2AccessToken accessToken = new DummyOAuth2AccessToken();
        accessToken.something = "some something";
        accessToken.expirationDate = null;
        accessToken.refreshToken = "1234";

        when(mStorage.hasAccessToken()).thenReturn(Observable.just(true));
        when(mStorage.getStoredAccessToken()).thenReturn(Observable.just(accessToken));

        ValueHolder<DummyOAuth2AccessToken> accessTokenValueHolder = new ValueHolder<>();

        RxOAuth2AccessTokenManager<DummyOAuth2AccessToken> manager = new RxOAuth2AccessTokenManager<>("id", mStorage, mProvider);

        // When
        subscribe(manager.getValidAccessToken(), testOAuth2AccessToken -> accessTokenValueHolder.value = testOAuth2AccessToken);

        // Then
        assertThat(accessTokenValueHolder.value).isEqualTo(accessToken);
    }
}