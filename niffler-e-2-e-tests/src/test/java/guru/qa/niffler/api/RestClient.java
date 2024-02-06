package guru.qa.niffler.api;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Converter;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public abstract class RestClient {

  protected final OkHttpClient okHttpClient;
  protected final Retrofit retrofit;

  public RestClient(@Nonnull String baseUri) {
    this(
        baseUri,
        false,
        JacksonConverterFactory.create(),
        null
    );
  }

  public RestClient(@Nonnull String baseUri, boolean followRedirect) {
    this(
        baseUri,
        followRedirect,
        JacksonConverterFactory.create(),
        null
    );
  }

  public RestClient(@Nonnull String baseUri, boolean followRedirect, @Nonnull Converter.Factory converter) {
    this(
        baseUri,
        followRedirect,
        converter,
        null
    );
  }

  public RestClient(@Nonnull String baseUri,
                    boolean followRedirect,
                    @Nonnull Converter.Factory converter,
                    @Nullable Interceptor... interceptors) {
    OkHttpClient.Builder builder = new OkHttpClient.Builder();
    builder.followRedirects(followRedirect);
    if (interceptors != null) {
      for (Interceptor interceptor : interceptors) {
        builder.addNetworkInterceptor(interceptor);
      }
    }
    builder.addNetworkInterceptor(
        new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
    );
    this.okHttpClient = builder.build();
    this.retrofit = new Retrofit.Builder()
        .baseUrl(baseUri)
        .addConverterFactory(converter)
        .build();
  }
}
