package de.rheinfabrik.heimdalldroid.utils

import android.content.SharedPreferences
import com.google.gson.Gson
import de.rheinfabrik.heimdall2.accesstoken.OAuth2AccessToken
import de.rheinfabrik.heimdall2.rxjava.OAuth2AccessTokenStorage
import io.reactivex.Single
import de.rheinfabrik.heimdall2.coroutines.OAuth2AccessTokenStorage as OAuth2AccessTokenStorageCoroutines

class SharedPreferencesOAuth2AccessTokenStorage(
    private val mSharedPreferences: SharedPreferences,
    private val mUniqueTypeAccessToken: Class<OAuth2AccessToken>
) : OAuth2AccessTokenStorage, OAuth2AccessTokenStorageCoroutines {

    companion object {
        // Constants
        private const val ACCESS_TOKEN_PREFERENCES_KEY = "OAuth2AccessToken"
    }

    // OAuth2AccessTokenStorage Rx
    override fun getStoredAccessToken(): Single<OAuth2AccessToken> =
        Single.just(getOAuth2AccessToken())

    override fun hasAccessToken(): Single<Boolean> = Single.just(containsAccessToken())

    override fun storeAccessToken(token: OAuth2AccessToken) =
        saveAccessToken(token = token)

    override fun removeAccessToken() = removeToken()

    // OAuth2AccessTokenStorage Coroutines

    override suspend fun getStoredOAuth2AccessToken(): OAuth2AccessToken =
        getStoredOAuth2AccessToken()

    override suspend fun storeOAuth2AccessToken(token: OAuth2AccessToken) =
        saveAccessToken(token = token)

    override suspend fun hasOAuth2AccessToken(): Boolean = containsAccessToken()

    override suspend fun removeOAuth2AccessToken() = removeToken()

    // Private Api

    private fun getOAuth2AccessToken(): OAuth2AccessToken? =
        mSharedPreferences.getString(ACCESS_TOKEN_PREFERENCES_KEY, null)?.let { json ->
            Gson().fromJson(json, mUniqueTypeAccessToken)
        }

    private fun containsAccessToken(): Boolean =
        mSharedPreferences.contains(ACCESS_TOKEN_PREFERENCES_KEY)

    private fun removeToken() =
        mSharedPreferences
            .edit()
            .remove(ACCESS_TOKEN_PREFERENCES_KEY)
            .apply()
    
    private fun saveAccessToken(token: OAuth2AccessToken) = mSharedPreferences
        .edit()
        .putString(ACCESS_TOKEN_PREFERENCES_KEY, Gson().toJson(token))
        .apply()
}
