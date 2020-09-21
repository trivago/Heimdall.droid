package de.rheinfabrik.heimdalldroid.utils;

import android.content.SharedPreferences;

import com.google.gson.Gson;

import de.rheinfabrik.heimdall2.rxjava.OAuth2AccessToken;
import de.rheinfabrik.heimdall2.rxjava.OAuth2AccessTokenStorage;
import io.reactivex.Single;

/**
 * A simple storage that saves the access token as plain text in the passed shared preferences.
 * It is recommend to set the access mode to MODE_PRIVATE.
 *
 * @param <TAccessToken> The access token type.
 */
public class SharedPreferencesOAuth2AccessTokenStorage<TAccessToken extends OAuth2AccessToken> implements OAuth2AccessTokenStorage {

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

    @Override
    public Single<OAuth2AccessToken> getStoredAccessToken() {
        return Single
                .just(mSharedPreferences.getString(ACCESS_TOKEN_PREFERENCES_KEY, null))
                .map(json -> (TAccessToken) new Gson().fromJson(json, mTokenClass));
    }

    @Override
    public void storeAccessToken(OAuth2AccessToken token) {
        mSharedPreferences
                .edit()
                .putString(ACCESS_TOKEN_PREFERENCES_KEY, new Gson().toJson(token))
                .apply();
    }

    @Override
    public Single<Boolean> hasAccessToken() {
        return Single.just(mSharedPreferences.contains(ACCESS_TOKEN_PREFERENCES_KEY));
    }

    @Override
    public void removeAccessToken() {
        mSharedPreferences
                .edit()
                .remove(ACCESS_TOKEN_PREFERENCES_KEY)
                .apply();
    }
}
