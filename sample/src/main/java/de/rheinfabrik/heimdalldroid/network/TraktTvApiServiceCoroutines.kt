package de.rheinfabrik.heimdalldroid.network

import de.rheinfabrik.heimdall2.accesstoken.OAuth2AccessToken
import de.rheinfabrik.heimdalldroid.network.models.AccessTokenRequestBody
import de.rheinfabrik.heimdalldroid.network.models.RefreshTokenRequestBody
import de.rheinfabrik.heimdalldroid.network.models.RevokeAccessTokenBody
import de.rheinfabrik.heimdalldroid.network.models.TraktTvList
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

interface TraktTvApiServiceCoroutines {

    @POST(value = "/oauth/token")
    suspend fun grantNewAccessToken(@Body body: AccessTokenRequestBody): OAuth2AccessToken

    @POST(value = "/oauth/token")
    suspend fun refreshAccessToken(@Body body: RefreshTokenRequestBody): OAuth2AccessToken

    @POST(value = "/oauth/revoke")
    suspend fun revokeAccessToken(@Body body: RevokeAccessTokenBody)

    @GET(value = "/users/me/lists")
    suspend fun getLists(@Header("Authorization") authorizationHeader: String): List<TraktTvList>
}
