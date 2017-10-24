package de.rheinfabrik.heimdall.grants;

import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import de.rheinfabrik.heimdall.OAuth2AccessToken;
import rx.Observable;
import rx.Single;
import rx.subjects.BehaviorSubject;
import rx.subjects.PublishSubject;

/**
 * Class representing the Authorization Code Grant as described in https://tools.ietf.org/html/rfc6749#section-4.1.
 *
 * @param <TAccessToken> The access token type.
 */
public abstract class OAuth2AuthorizationCodeGrant<TAccessToken extends OAuth2AccessToken> implements OAuth2Grant<TAccessToken> {

    // Constants

    /**
     * REQUIRED
     * The "response_type" which MUST be "code".
     */
    public final static String RESPONSE_TYPE = "code";

    /**
     * REQUIRED
     * The "grant_type" which MUST be "authorization_code".
     */
    public final static String GRANT_TYPE = "authorization_code";

    // Properties

    /**
     * REQUIRED
     * The client identifier as described in https://tools.ietf.org/html/rfc6749#section-2.2.
     */
    public String clientId;

    /**
     * OPTIONAL
     * As described in https://tools.ietf.org/html/rfc6749#section-3.1.2.
     */
    public String redirectUri;

    /**
     * OPTIONAL
     * The scope of the access request as described in https://tools.ietf.org/html/rfc6749#section-3.3.
     */
    public String scope;

    /**
     * RECOMMENDED
     * An opaque value used by the client to maintain
     * state between the request and callback. The authorization
     * server includes this value when redirecting the user-agent back
     * to the client. The parameter SHOULD be used for preventing
     * cross-site request forgery as described in https://tools.ietf.org/html/rfc6749#section-10.12.
     */
    public String state;

    // Public Api

    /**
     * Observable emitting the authorization Uri.
     */
    public final Observable<URL> authorizationUri() {
        return mAuthorizationUrlSubject.asObservable();
    }

    /**
     * Command you should send a value to whenever an url in e.g. your web view has been loaded.
     */
    public final PublishSubject<URL> onUrlLoadedCommand = PublishSubject.create();

    // Abstract Api

    /**
     * Called when the grant needs the authorization url.
     */
    public abstract URL buildAuthorizationUrl();

    /**
     * Called when the grant was able to grab the code and it wants to exchange it for an access token.
     */
    public abstract Observable<TAccessToken> exchangeTokenUsingCode(String code);

    // Members

    private final BehaviorSubject<URL> mAuthorizationUrlSubject = BehaviorSubject.create();

    // OAuth2AccessToken

    @Override
    public Single<TAccessToken> grantNewAccessToken() {
        mAuthorizationUrlSubject.onNext(buildAuthorizationUrl());

        return onUrlLoadedCommand
                .map(uri -> {
                    List<String> values = getQueryParameters(uri).get(RESPONSE_TYPE);
                    if (values != null && values.size() >= 1) {
                        return values.get(0);
                    }

                    return null;
                })
                .filter(code -> code != null)
                .take(1)
                .retry()
                .concatMap(this::exchangeTokenUsingCode)
                .toSingle();
    }

    // Private

    private static Map<String, List<String>> getQueryParameters(URL url) {
        final Map<String, List<String>> query_pairs = new LinkedHashMap<>();
        final String[] pairs = url.getQuery().split("&");
        for (String pair : pairs) {
            final int idx = pair.indexOf("=");

            try {
                final String key = idx > 0 ? URLDecoder.decode(pair.substring(0, idx), "UTF-8") : pair;
                if (!query_pairs.containsKey(key)) {
                    query_pairs.put(key, new LinkedList<>());
                }
                final String value = idx > 0 && pair.length() > idx + 1 ? URLDecoder.decode(pair.substring(idx + 1), "UTF-8") : null;
                query_pairs.get(key).add(value);
            } catch (Exception ignored) {}
        }

        return query_pairs;
    }
}
