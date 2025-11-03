package com.rc.feature.offers.di;

import com.rc.feature.offers.data.graphql.OffersGraphQLService;
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
public final class OffersNetworkModule_ProvideServiceFactory implements Factory<OffersGraphQLService> {
  private final Provider<Retrofit> retrofitProvider;

  private OffersNetworkModule_ProvideServiceFactory(Provider<Retrofit> retrofitProvider) {
    this.retrofitProvider = retrofitProvider;
  }

  @Override
  public OffersGraphQLService get() {
    return provideService(retrofitProvider.get());
  }

  public static OffersNetworkModule_ProvideServiceFactory create(
      Provider<Retrofit> retrofitProvider) {
    return new OffersNetworkModule_ProvideServiceFactory(retrofitProvider);
  }

  public static OffersGraphQLService provideService(Retrofit retrofit) {
    return Preconditions.checkNotNullFromProvides(OffersNetworkModule.INSTANCE.provideService(retrofit));
  }
}
