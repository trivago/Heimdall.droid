package de.rheinfabrik.oauth2;

import rx.Observable;

/**
 * Interface used to define how to generate and how to refresh an access token.
 *
 * @param <TAccessToken> The access token type.
 */
public interface OAuth2AccessTokenProvider<TAccessToken extends OAuth2AccessToken> {

    // Enums

    /**
     * Represents the different grant types in oAuth2.
     */
    public enum GrantType {

        // Enum Values

        /**
         * Used for https://tools.ietf.org/html/rfc6749#section-4.3.
         */
        PASSWORD("password"),

        /**
         * Used for https://tools.ietf.org/html/rfc6749#section-6.
         */
        REFRESH_TOKEN("refresh_token");

        // Members

        private String mOAuth2Name;

        // Constructor

        GrantType(String oAuth2Name) {
            mOAuth2Name = oAuth2Name;
        }

        // Public API

        public String getOAuth2Name() {
            return mOAuth2Name;
        }
    }

    // Public API

    /**
     * This method generates a new access token.
     * The method signature is based to work with the Resource Owner
     * Password Credentials Grant method (https://tools.ietf.org/html/rfc6749#section-4.3).
     *
     * @param username The username to get the access token.
     * @param password The password to get the access token.
     * @return - An observable emitting the new access token.
     */
    public abstract Observable<TAccessToken> getNewAccessToken(String username, String password);

    /**
     * This method is responsible to refresh (https://tools.ietf.org/html/rfc6749#section-6) the
     * access token based on the given refresh token.
     * The method signature is based to work with the Resource Owner
     * Password Credentials Grant method (https://tools.ietf.org/html/rfc6749#section-4.3).
     *
     * @param refreshToken The refresh token which will be used to query a new access token.
     * @return - An observable emitting the refreshed access token.
     */
    public abstract Observable<TAccessToken> refreshAccessToken(String refreshToken);
}
