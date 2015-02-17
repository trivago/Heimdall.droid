package de.rheinfabrik.heimdall.tests;

import org.mockito.Mock;

import java.util.Calendar;

import de.rheinfabrik.heimdall.OAuth2AccessToken;
import de.rheinfabrik.heimdall.OAuth2AccessTokenManager;
import de.rheinfabrik.heimdall.OAuth2AccessTokenStorage;
import de.rheinfabrik.heimdall.grants.OAuth2Grant;
import de.rheinfabrik.heimdall.grants.OAuth2RefreshAccessTokenGrant;
import de.rheinfabrik.heimdall.utils.MockitoObservablesTestCase;
import de.rheinfabrik.heimdall.utils.ValueHolder;
import rx.Observable;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class OAuth2AccessTokenManagerTests extends MockitoObservablesTestCase {

    // Members

    @Mock
    OAuth2AccessTokenStorage<OAuth2AccessToken> mStorage;

    @Mock
    OAuth2RefreshAccessTokenGrant<OAuth2AccessToken> mRefreshGrant;

    @Mock
    OAuth2Grant<OAuth2AccessToken> mGrant;

    // Test lifecycle

    @Override
    protected void tearDown() throws Exception {
        reset(mStorage, mGrant, mRefreshGrant);

        mRefreshGrant.refreshToken = null;

        super.tearDown();
    }

    // Tests

    public void testConstructorThrowsExceptionIfStorageIsNull() {

        // Given & When
        try {
            new OAuth2AccessTokenManager<>(null);

            fail("No exception was thrown.");
        } catch (RuntimeException exception) {

            // Then
            assertThat(exception).hasMessage("Storage MUST NOT be null.");
        }
    }

    public void testGrantNewAccessTokenWithNullGrantThrowsException() {

        // Given
        OAuth2AccessTokenManager<OAuth2AccessToken> manager = new OAuth2AccessTokenManager<>(mStorage);

        // When
        try {
            manager.grantNewAccessToken(null);

            fail("No exception was thrown.");
        } catch (RuntimeException exception) {

            // Then
            assertThat(exception).hasMessage("Grant MUST NOT be null.");
        }
    }

    public void testGrantNewAccessTokenDoesNotEmitErrorIfExpiresInIsNull() {

        // Given
        when(mGrant.grantNewAccessToken()).thenReturn(Observable.just(new OAuth2AccessToken()));

        OAuth2AccessTokenManager<OAuth2AccessToken> manager = new OAuth2AccessTokenManager<>(mStorage);

        ValueHolder<Throwable> errorValueHolder = new ValueHolder<>();

        // When
        subscribe(manager.grantNewAccessToken(mGrant), null, throwable -> errorValueHolder.value = throwable);

        // Then
        assertThat(errorValueHolder.value).isNull();
    }

    public void testGrantNewAccessTokenSetExpirationDateIfExpiresInIsNotNull() {

        // Given
        OAuth2AccessToken accessToken = new OAuth2AccessToken();
        accessToken.expiresIn = 3600;

        when(mGrant.grantNewAccessToken()).thenReturn(Observable.just(accessToken));

        OAuth2AccessTokenManager<OAuth2AccessToken> manager = new OAuth2AccessTokenManager<>(mStorage);

        ValueHolder<OAuth2AccessToken> accessTokenValueHolder = new ValueHolder<>();

        // When
        subscribe(manager.grantNewAccessToken(mGrant), receivedAccessToken -> accessTokenValueHolder.value = receivedAccessToken);

        // Then
        Calendar now = Calendar.getInstance();
        now.add(Calendar.SECOND, 3600);

        long delta = now.getTimeInMillis() - accessTokenValueHolder.value.expirationDate.getTimeInMillis();

        assertThat(delta).isLessThan(5000);
    }

    public void testGrantNewAccessTokenStoresAccessToken() {

        // Given
        OAuth2AccessToken accessToken = new OAuth2AccessToken();

        when(mGrant.grantNewAccessToken()).thenReturn(Observable.just(accessToken));

        OAuth2AccessTokenManager<OAuth2AccessToken> manager = new OAuth2AccessTokenManager<>(mStorage);

        // When
        subscribe(manager.grantNewAccessToken(mGrant));

        // Then
        verify(mStorage).storeAccessToken(eq(accessToken));
    }

    public void testGetStorageReturnsCorrectStorage() {

        // Given
        OAuth2AccessTokenManager<OAuth2AccessToken> manager = new OAuth2AccessTokenManager<>(mStorage);

        // When
        OAuth2AccessTokenStorage<OAuth2AccessToken> storage = manager.getStorage();

        // Then
        assertThat(storage).isEqualTo(mStorage);
    }

    public void testGetValidAccessTokenThrowsExceptionWhenGrantIsNull() {

        // Given
        OAuth2AccessTokenManager<OAuth2AccessToken> manager = new OAuth2AccessTokenManager<>(mStorage);

        // When
        try {
            manager.getValidAccessToken(null);

            fail("No exception was thrown.");
        } catch (RuntimeException exception) {

            // Then
            assertThat(exception).hasMessage("RefreshAccessTokenGrant MUST NOT be null.");
        }
    }

    public void testGetValidAccessTokenEmitsErrorWhenNoAccessTokenIsStored() {

        // Given
        when(mStorage.getStoredAccessToken()).thenReturn(Observable.just(null));

        OAuth2AccessTokenManager<OAuth2AccessToken> manager = new OAuth2AccessTokenManager<>(mStorage);

        ValueHolder<Throwable> throwableValueHolder = new ValueHolder<>();

        // When
        subscribe(manager.getValidAccessToken(mRefreshGrant), null, receivedThrowable -> throwableValueHolder.value = receivedThrowable);

        // Then
        assertThat(throwableValueHolder.value).hasMessage("No access token found.");
    }

    public void testGetValidAccessTokenEmitsStoredAccessTokenIfNotExpired() {

        // Given
        OAuth2AccessToken accessToken = mock(OAuth2AccessToken.class);
        when(accessToken.isExpired()).thenReturn(false);

        when(mStorage.getStoredAccessToken()).thenReturn(Observable.just(accessToken));

        OAuth2AccessTokenManager<OAuth2AccessToken> manager = new OAuth2AccessTokenManager<>(mStorage);

        ValueHolder<OAuth2AccessToken> accessTokenHolder = new ValueHolder<>();

        // When
        subscribe(manager.getValidAccessToken(mRefreshGrant), receivedToken -> accessTokenHolder.value = receivedToken);

        // Then
        assertThat(accessTokenHolder.value).isEqualTo(accessToken);
    }

    public void testGetValidAccessTokenSetsCorrectRefreshTokenToGrant() {

        // Given
        OAuth2AccessToken accessToken = mock(OAuth2AccessToken.class);
        when(accessToken.isExpired()).thenReturn(true);
        accessToken.refreshToken = "le token";

        when(mStorage.getStoredAccessToken()).thenReturn(Observable.just(accessToken));

        OAuth2AccessTokenManager<OAuth2AccessToken> manager = new OAuth2AccessTokenManager<>(mStorage);

        // When
        subscribe(manager.getValidAccessToken(mRefreshGrant));

        // Then
        assertThat(mRefreshGrant.refreshToken).isEqualTo("le token");
    }

    public void testGetValidAccessTokenSetExpirationDateIfExpiresInIsNotNull() {

        // Given
        OAuth2AccessToken accessToken = mock(OAuth2AccessToken.class);
        when(accessToken.isExpired()).thenReturn(true);
        accessToken.refreshToken = "le token";
        accessToken.expiresIn = 3600;

        when(mStorage.getStoredAccessToken()).thenReturn(Observable.just(accessToken));
        when(mRefreshGrant.grantNewAccessToken()).thenReturn(Observable.just(accessToken));

        OAuth2AccessTokenManager<OAuth2AccessToken> manager = new OAuth2AccessTokenManager<>(mStorage);

        ValueHolder<OAuth2AccessToken> accessTokenValueHolder = new ValueHolder<>();

        // When
        subscribe(manager.getValidAccessToken(mRefreshGrant), receivedAccessToken -> accessTokenValueHolder.value = receivedAccessToken);

        // Then
        Calendar now = Calendar.getInstance();
        now.add(Calendar.SECOND, 3600);

        long delta = now.getTimeInMillis() - accessTokenValueHolder.value.expirationDate.getTimeInMillis();

        assertThat(delta).isLessThan(5000);
    }

    public void testGrantNewAccessTokenStoresAccessToken2() {

        // Given
        OAuth2AccessToken accessToken = mock(OAuth2AccessToken.class);
        when(accessToken.isExpired()).thenReturn(true);
        accessToken.refreshToken = "le token";
        accessToken.expiresIn = 3600;

        when(mRefreshGrant.grantNewAccessToken()).thenReturn(Observable.just(accessToken));
        when(mStorage.getStoredAccessToken()).thenReturn(Observable.just(accessToken));

        OAuth2AccessTokenManager<OAuth2AccessToken> manager = new OAuth2AccessTokenManager<>(mStorage);

        // When
        subscribe(manager.getValidAccessToken(mRefreshGrant));

        // Then
        verify(mStorage).storeAccessToken(eq(accessToken));
    }

}