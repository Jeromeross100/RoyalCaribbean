package com.rc.feature.offers.di;

import com.rc.feature.offers.data.OffersService;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import dagger.internal.Provider;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
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
public final class OffersDataBindingsModule_Companion_ProvideOffersServiceFactory implements Factory<OffersService> {
  private final Provider<Retrofit> retrofitProvider;

  private OffersDataBindingsModule_Companion_ProvideOffersServiceFactory(
      Provider<Retrofit> retrofitProvider) {
    this.retrofitProvider = retrofitProvider;
  }

  @Override
  public OffersService get() {
    return provideOffersService(retrofitProvider.get());
  }

  public static OffersDataBindingsModule_Companion_ProvideOffersServiceFactory create(
      Provider<Retrofit> retrofitProvider) {
    return new OffersDataBindingsModule_Companion_ProvideOffersServiceFactory(retrofitProvider);
  }

  public static OffersService provideOffersService(Retrofit retrofit) {
    return Preconditions.checkNotNullFromProvides(OffersDataBindingsModule.Companion.provideOffersService(retrofit));
  }
}
