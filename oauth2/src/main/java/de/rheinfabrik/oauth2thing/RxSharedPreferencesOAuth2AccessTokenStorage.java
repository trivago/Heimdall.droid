package de.rheinfabrik.oauth2thing;

import android.content.SharedPreferences;

import com.google.gson.Gson;

import rx.Observable;

/**
 * A simple storage that saves the access token as plain text in the passed shared preferences.
 * It is recommend to set the access mode to MODE_PRIVATE.
 *
 * @param <TAccessToken> The access token type.
 */
public class RxSharedPreferencesOAuth2AccessTokenStorage<TAccessToken extends OAuth2AccessToken> implements RxOAuth2AccessTokenStorage<TAccessToken> {

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
    public RxSharedPreferencesOAuth2AccessTokenStorage(SharedPreferences sharedPreferences, Class tokenClass) {
        super();

        mTokenClass = tokenClass;
        mSharedPreferences = sharedPreferences;
    }

    // OAuth2AccessTokenStorage

    @Override
    public Observable<Boolean> hasAccessToken() {
        return Observable.just(mSharedPreferences.contains(ACCESS_TOKEN_PREFERENCES_KEY));
    }

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
}
