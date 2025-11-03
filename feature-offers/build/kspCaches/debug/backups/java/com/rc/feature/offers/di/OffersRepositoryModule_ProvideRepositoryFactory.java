package com.rc.feature.offers.di;

import com.rc.feature.offers.data.OffersRepository;
import com.rc.feature.offers.data.graphql.OffersGraphQLService;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import dagger.internal.Provider;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;

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
public final class OffersRepositoryModule_ProvideRepositoryFactory implements Factory<OffersRepository> {
  private final Provider<OffersGraphQLService> serviceProvider;

  private OffersRepositoryModule_ProvideRepositoryFactory(
      Provider<OffersGraphQLService> serviceProvider) {
    this.serviceProvider = serviceProvider;
  }

  @Override
  public OffersRepository get() {
    return provideRepository(serviceProvider.get());
  }

  public static OffersRepositoryModule_ProvideRepositoryFactory create(
      Provider<OffersGraphQLService> serviceProvider) {
    return new OffersRepositoryModule_ProvideRepositoryFactory(serviceProvider);
  }

  public static OffersRepository provideRepository(OffersGraphQLService service) {
    return Preconditions.checkNotNullFromProvides(OffersRepositoryModule.INSTANCE.provideRepository(service));
  }
}
