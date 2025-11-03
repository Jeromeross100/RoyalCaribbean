package com.rc.feature.offers.di;

import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import okhttp3.OkHttpClient;

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
public final class OffersNetworkModule_ProvideOkHttpFactory implements Factory<OkHttpClient> {
  @Override
  public OkHttpClient get() {
    return provideOkHttp();
  }

  public static OffersNetworkModule_ProvideOkHttpFactory create() {
    return InstanceHolder.INSTANCE;
  }

  public static OkHttpClient provideOkHttp() {
    return Preconditions.checkNotNullFromProvides(OffersNetworkModule.INSTANCE.provideOkHttp());
  }

  private static final class InstanceHolder {
    static final OffersNetworkModule_ProvideOkHttpFactory INSTANCE = new OffersNetworkModule_ProvideOkHttpFactory();
  }
}
