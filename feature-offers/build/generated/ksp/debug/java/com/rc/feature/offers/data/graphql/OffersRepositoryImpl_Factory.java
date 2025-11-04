package com.rc.feature.offers.data.graphql;

import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Provider;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;

@ScopeMetadata
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
public final class OffersRepositoryImpl_Factory implements Factory<OffersRepositoryImpl> {
  private final Provider<OffersGraphQLService> serviceProvider;

  private OffersRepositoryImpl_Factory(Provider<OffersGraphQLService> serviceProvider) {
    this.serviceProvider = serviceProvider;
  }

  @Override
  public OffersRepositoryImpl get() {
    return newInstance(serviceProvider.get());
  }

  public static OffersRepositoryImpl_Factory create(
      Provider<OffersGraphQLService> serviceProvider) {
    return new OffersRepositoryImpl_Factory(serviceProvider);
  }

  public static OffersRepositoryImpl newInstance(OffersGraphQLService service) {
    return new OffersRepositoryImpl(service);
  }
}
