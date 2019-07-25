package de.rheinfabrik.heimdall2

import com.google.gson.Gson
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import java.util.*


class OAuth2AccessTokenSerializationTest {

    @Before
    fun setup() {
        // Set default locale and time zone
        Locale.setDefault(Locale.GERMANY)
        TimeZone.setDefault(TimeZone.getTimeZone("CEST"))
    }

    @Test
    fun `when an OAuth2AccessToken is transformed to a JSON string, it should be created`() {

        // given an OAuth2AccessToken
        val accessToken = OAuth2AccessToken(
            refreshToken = "rt",
            expiresIn = 3600,
            accessToken = "at",
            tokenType = "bearer"
        )
        val expirationDate = Calendar.getInstance()
        expirationDate.timeInMillis = 0
        accessToken.expirationDate = expirationDate

        // when it gets serialized with Gson
        val json = Gson().toJson(accessToken)

        // then the json should be written correctly
        assertEquals(
            "{\"token_type\":\"bearer\",\"access_token\":\"at\",\"refresh_token\":\"rt\",\"expires_in\":3600,\"heimdall_expiration_date\":{\"year\":1970,\"month\":0,\"dayOfMonth\":1,\"hourOfDay\":0,\"minute\":0,\"second\":0}}",
            json
        )
    }

    @Test
    fun `when a token is created from a JSON string, it should be created`() {

        // given a json string representing a OAuth2AccessToken
        val jsonString =
            "{\"access_token\":\"at\",\"heimdall_expiration_date\":{\"year\":1970,\"month\":0,\"dayOfMonth\":1,\"hourOfDay\":0,\"minute\":0,\"second\":0},\"expires_in\":3600,\"refresh_token\":\"rt\",\"token_type\":\"bearer\"}"

        // and an expiration date
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = 0

        // when it gets deserialized with Gson
        val accessToken = Gson().fromJson(jsonString, OAuth2AccessToken::class.java)

        // then the token should be the same
        assertEquals("rt", accessToken.refreshToken)
        assertEquals(3600, accessToken.expiresIn)
        assertEquals("at", accessToken.accessToken)
        assertEquals("bearer", accessToken.tokenType)
        assertEquals(calendar.timeInMillis, accessToken.expirationDate?.timeInMillis)
    }
}