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
public final class OffersDataBindingsModule_Companion_ProvideOffersGraphQLServiceFactory implements Factory<OffersGraphQLService> {
  private final Provider<Retrofit> retrofitProvider;

  private OffersDataBindingsModule_Companion_ProvideOffersGraphQLServiceFactory(
      Provider<Retrofit> retrofitProvider) {
    this.retrofitProvider = retrofitProvider;
  }

  @Override
  public OffersGraphQLService get() {
    return provideOffersGraphQLService(retrofitProvider.get());
  }

  public static OffersDataBindingsModule_Companion_ProvideOffersGraphQLServiceFactory create(
      Provider<Retrofit> retrofitProvider) {
    return new OffersDataBindingsModule_Companion_ProvideOffersGraphQLServiceFactory(retrofitProvider);
  }

  public static OffersGraphQLService provideOffersGraphQLService(Retrofit retrofit) {
    return Preconditions.checkNotNullFromProvides(OffersDataBindingsModule.Companion.provideOffersGraphQLService(retrofit));
  }
}
