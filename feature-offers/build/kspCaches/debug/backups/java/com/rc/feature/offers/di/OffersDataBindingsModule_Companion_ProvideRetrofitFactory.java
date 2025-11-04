package com.rc.feature.offers.di;

import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import dagger.internal.Provider;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;

@ScopeMetadata("javax.inject.Singleton")
@QualifierMetadata
@DaggerGenerated
@Generated(
    value = "dagger.internal.codegen.ComponentProcessor",
    comments = "https://dagger.dev"
)
@SuppressWarnings({
    "unchecked",
    "rawtypes",
    "KotlinInternal",
    "KotlinInternalInJava",
    "cast",
    "deprecation",
    "nullness:initialization.field.uninitialized"
})
public final class OffersDataBindingsModule_Companion_ProvideRetrofitFactory implements Factory<Retrofit> {
  private final Provider<OkHttpClient> okHttpClientProvider;

  private OffersDataBindingsModule_Companion_ProvideRetrofitFactory(
      Provider<OkHttpClient> okHttpClientProvider) {
    this.okHttpClientProvider = okHttpClientProvider;
  }

  @Override
  public Retrofit get() {
    return provideRetrofit(okHttpClientProvider.get());
  }

  public static OffersDataBindingsModule_Companion_ProvideRetrofitFactory create(
      Provider<OkHttpClient> okHttpClientProvider) {
    return new OffersDataBindingsModule_Companion_ProvideRetrofitFactory(okHttpClientProvider);
  }

  public static Retrofit provideRetrofit(OkHttpClient okHttpClient) {
    return Preconditions.checkNotNullFromProvides(OffersDataBindingsModule.Companion.provideRetrofit(okHttpClient));
  }
}
