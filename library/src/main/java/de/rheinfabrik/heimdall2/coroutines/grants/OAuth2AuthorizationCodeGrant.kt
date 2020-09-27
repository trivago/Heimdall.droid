package de.rheinfabrik.heimdall2.coroutines.grants

import de.rheinfabrik.heimdall2.accesstoken.OAuth2AccessToken
import java.net.URL
import java.net.URLDecoder

/**
 * Class representing the Authorization Code Grant as described in https://tools.ietf.org/html/rfc6749#section-4.1.
 *
 * @param <TAccessToken> The access token type.
 */
abstract class OAuth2AuthorizationCodeGrant(
    /**
     * REQUIRED
     * The client identifier as described in https://tools.ietf.org/html/rfc6749#section-2.2.
     */
    var clientId: String = "",

    /**
     * OPTIONAL
     * As described in https://tools.ietf.org/html/rfc6749#section-3.1.2.
     */
    var redirectUri: String? = null,

    /**
     * OPTIONAL
     * The scope of the access request as described in https://tools.ietf.org/html/rfc6749#section-3.3.
     */
    var scope: String? = null,

    /**
     * RECOMMENDED
     * An opaque value used by the client to maintain
     * state between the request and callback. The authorization
     * server includes this value when redirecting the user-agent back
     * to the client. The parameter SHOULD be used for preventing
     * cross-site request forgery as described in https://tools.ietf.org/html/rfc6749#section-10.12.
     */
    var state: String? = null
) : OAuth2Grant {

    // Constants
    companion object {
        @JvmField
        val RESPONSE_TYPE = "code"
        @JvmField
        val GRANT_TYPE = "authorization_code"
        private const val UTF_8 = "UTF-8"
    }

    // Private Members

    private lateinit var mUrl: URL

    // Abstract API

    /**
     * Called when the grant needs the authorization url.
     */
    abstract suspend fun buildAuthorizationUrl(): URL

    /**
     * Called when the grant was able to grab the code and it wants to exchange for an access token.
     */
    abstract suspend fun exchangeTokenUsingCode(code: String): OAuth2AccessToken

    // Public API

    fun setURL(url: URL) {
        mUrl = url
    }

    override suspend fun grantNewAccessToken(): OAuth2AccessToken =
        getQueryParameters(url = mUrl)[RESPONSE_TYPE]?.get(0)?.let { code ->
            exchangeTokenUsingCode(code = code)
        } ?: throw IllegalStateException()

    // Private API

    private fun getQueryParameters(url: URL): LinkedHashMap<String, MutableList<String>> {
        val queryParams = linkedMapOf<String, MutableList<String>>()
        url.query.split("&").forEach {
            val idx = it.indexOf("=")
            try {
                val key = if (idx > 0) {
                    URLDecoder.decode(
                        it.substring(0, idx),
                        UTF_8
                    )
                } else {
                    it
                }
                if (!queryParams.containsKey(key)) {
                    queryParams[key] = mutableListOf()
                }
                val value = if (idx > 0 && it.length > idx + 1) {
                    URLDecoder.decode(
                        it.substring(idx + 1),
                        UTF_8
                    )
                } else ""

                queryParams[key]?.add(value)
            } catch (e: Exception) {
                // Do nothing
            }
        }
        return queryParams
    }
}
