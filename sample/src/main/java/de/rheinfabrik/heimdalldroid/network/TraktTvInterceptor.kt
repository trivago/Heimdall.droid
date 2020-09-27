package de.rheinfabrik.heimdalldroid.network

import de.rheinfabrik.heimdalldroid.TraktTvAPIConfiguration
import okhttp3.Interceptor
import okhttp3.Response

class TraktTvInterceptor: Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val original = chain.request()

        val requestBuilder = original.newBuilder()
            .header("Content-type", "application/json")
            .header("trakt-api-version", "2")
            .header("trakt-api-key", TraktTvAPIConfiguration.CLIENT_ID)

        return chain.proceed(requestBuilder.build())
    }
}