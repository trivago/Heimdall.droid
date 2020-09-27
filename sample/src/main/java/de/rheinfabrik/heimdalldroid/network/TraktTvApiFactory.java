package de.rheinfabrik.heimdalldroid.network;

import org.jetbrains.annotations.NotNull;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Factory class to generate new TraktTvApiService instances.
 */
public class TraktTvApiFactory {

    // Constants

    private static final String API_ENDPOINT = "https://api.trakt.tv";

    // Public Api

    /**
     * Creates a new preconfigured TraktTvApiService instance.
     */
    public static TraktTvApiService newApiServiceRxJava() {
        Retrofit.Builder restAdapterBuilder = getRetrofitBuilder();

        // Build raw api service
        return restAdapterBuilder.build().create(TraktTvApiService.class);
    }

    public static TraktTvApiServiceCoroutines newApiServiceCoroutines() {
        Retrofit.Builder builder = getRetrofitBuilder();
        return builder.build().create(TraktTvApiServiceCoroutines.class);
    }

    @NotNull
    private static Retrofit.Builder getRetrofitBuilder() {
        return new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient())
                .baseUrl(API_ENDPOINT);
    }

    private static OkHttpClient okHttpClient() {
        return new OkHttpClient.Builder()
                .addInterceptor(
                        new TraktTvInterceptor()
                ).build();
    }
}
