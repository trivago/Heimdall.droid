package de.rheinfabrik.heimdall2

import com.google.gson.annotations.SerializedName
import java.io.Serializable
import java.util.Calendar

class OAuth2AccessToken(
    /**
     * REQUIRED
     * The type of the token issued as described in https://tools.ietf.org/html/rfc6749#section-7.1.
     * Value is case insensitive.
     */
    @SerializedName("token_type")
    var tokenType: String? = null,

    /**
     * REQUIRED
     * The access token issued by the authorization server.
     */
    @SerializedName("access_token")
    var accessToken: String? = null,

    /**
     * OPTIONAL
     * The refresh token, which can be used to obtain new
     * access tokens using the same authorization grant as described
     * in https://tools.ietf.org/html/rfc6749#section-6.
     */
    @SerializedName("refresh_token")
    var refreshToken: String? = null,

    /**
     * RECOMMENDED
     * The lifetime in seconds of the access token.  For
     * example, the value "3600" denotes that the access token will
     * expire in one hour from the time the response was generated.
     * If omitted, the authorization server SHOULD provide the
     * expiration time via other means or document the default value.
     */
    @SerializedName("expires_in")
    var expiresIn: Int? = null,

    /**
     * The expiration date used by Heimdall.
     */
    @SerializedName("heimdall_expiration_date")
    var expirationDate: Calendar? = null
) : Serializable {

    // Public API

    /**
     * Returns whether the access token expired or not.
     *
     * @return True if expired. Otherwise false.
     */
    fun isExpired(): Boolean =
        expirationDate != null &&
            Calendar.getInstance().after(expirationDate)


    override fun equals(other: Any?): Boolean =
        when {
            this === other -> true
            other !is OAuth2AccessToken -> false
            else -> {
                accessToken.equals(other.accessToken) && tokenType.equals(other.accessToken)
            }
        }


    override fun hashCode(): Int =
        tokenType.hashCode().let {
            31 * it + accessToken.hashCode()
        }
}
