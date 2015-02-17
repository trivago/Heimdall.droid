package de.rheinfabrik.heimdall.extras;

import android.content.SharedPreferences;

import com.google.gson.Gson;

import de.rheinfabrik.heimdall.OAuth2AccessToken;
import de.rheinfabrik.heimdall.OAuth2AccessTokenStorage;
import rx.Observable;

/**
 * A simple storage that saves the access token as plain text in the passed shared preferences.
 * It is recommend to set the access mode to MODE_PRIVATE.
 *
 * @param <TAccessToken> The access token type.
 */
public class SharedPreferencesOAuth2AccessTokenStorage<TAccessToken extends OAuth2AccessToken> implements OAuth2AccessTokenStorage<TAccessToken> {

    // Constants

    private static final String ACCESS_TOKEN_PREFERENCES_KEY = "OAuth2AccessToken";

    // Members

    private final SharedPreferences mSharedPreferences;
    private final Class mTokenClass;

    // Constructor

    /**
     * Designated constructor.
     *
     * @param sharedPreferences The shared preferences used for saving the access token.
     * @param tokenClass        The actual class of the access token.
     */
    public SharedPreferencesOAuth2AccessTokenStorage(SharedPreferences sharedPreferences, Class tokenClass) {
        super();

        if (tokenClass == null) {
            throw new RuntimeException("TokenClass MUST NOT be null.");
        }

        if (sharedPreferences == null) {
            throw new RuntimeException("SharedPreferences MUST NOT be null.");
        }

        mTokenClass = tokenClass;
        mSharedPreferences = sharedPreferences;
    }

    // OAuth2AccessTokenStorage

    @SuppressWarnings("unchecked")
    @Override
    public Observable<TAccessToken> getStoredAccessToken() {
        return Observable
                .just(mSharedPreferences.getString(ACCESS_TOKEN_PREFERENCES_KEY, null))
                .filter(accessToken -> {
                    if (accessToken == null) {
                        throw new RuntimeException("No access token found.");
                    }

                    return true;
                })
                .map(json -> (TAccessToken) new Gson().fromJson(json, mTokenClass));
    }

    @Override
    public void storeAccessToken(TAccessToken accessToken) {
        mSharedPreferences
                .edit()
                .putString(ACCESS_TOKEN_PREFERENCES_KEY, new Gson().toJson(accessToken))
                .apply();
    }

    @Override
    public Observable<Boolean> hasAccessToken() {
        return Observable.just(mSharedPreferences.contains(ACCESS_TOKEN_PREFERENCES_KEY));
    }
}
