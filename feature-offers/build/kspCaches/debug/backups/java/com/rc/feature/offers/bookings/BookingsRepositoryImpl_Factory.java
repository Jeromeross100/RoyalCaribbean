package com.rc.feature.offers.bookings;

import com.rc.feature.offers.data.graphql.OffersGraphQLService;
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
public final class BookingsRepositoryImpl_Factory implements Factory<BookingsRepositoryImpl> {
  private final Provider<OffersGraphQLService> serviceProvider;

  private BookingsRepositoryImpl_Factory(Provider<OffersGraphQLService> serviceProvider) {
    this.serviceProvider = serviceProvider;
  }

  @Override
  public BookingsRepositoryImpl get() {
    return newInstance(serviceProvider.get());
  }

  public static BookingsRepositoryImpl_Factory create(
      Provider<OffersGraphQLService> serviceProvider) {
    return new BookingsRepositoryImpl_Factory(serviceProvider);
  }

  public static BookingsRepositoryImpl newInstance(OffersGraphQLService service) {
    return new BookingsRepositoryImpl(service);
  }
}
