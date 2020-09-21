package de.rheinfabrik.heimdall2.rxjava

import com.google.gson.annotations.SerializedName
import java.io.Serializable
import java.util.Calendar

data class OAuth2AccessToken(
    /**
     * REQUIRED
     * The type of the token issued as described in https://tools.ietf.org/html/rfc6749#section-7.1.
     * Value is case insensitive.
     */
    @SerializedName("token_type")
    val tokenType: String = "",

    /**
     * REQUIRED
     * The access token issued by the authorization server.
     */
    @SerializedName("access_token")
    val accessToken: String = "",

    /**
     * OPTIONAL
     * The refresh token, which can be used to obtain new
     * access tokens using the same authorization grant as described
     * in https://tools.ietf.org/html/rfc6749#section-6.
     */
    @SerializedName("refresh_token")
    val refreshToken: String? = null,

    /**
     * RECOMMENDED
     * The lifetime in seconds of the access token.  For
     * example, the value "3600" denotes that the access token will
     * expire in one hour from the time the response was generated.
     * If omitted, the authorization server SHOULD provide the
     * expiration time via other means or document the default value.
     */
    @SerializedName("expires_in")
    val expiresIn: Int? = null,

    /**
     * The expiration date used by Heimdall.
     */
    @SerializedName("heimdall_expiration_date")
    val expirationDate: Calendar? = null
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

}
