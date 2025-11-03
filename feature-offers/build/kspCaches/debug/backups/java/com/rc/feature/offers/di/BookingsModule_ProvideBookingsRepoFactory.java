package com.rc.feature.offers.di;

import com.rc.feature.offers.bookings.BookingsRepository;
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
public final class BookingsModule_ProvideBookingsRepoFactory implements Factory<BookingsRepository> {
  private final Provider<OffersGraphQLService> serviceProvider;

  private BookingsModule_ProvideBookingsRepoFactory(
      Provider<OffersGraphQLService> serviceProvider) {
    this.serviceProvider = serviceProvider;
  }

  @Override
  public BookingsRepository get() {
    return provideBookingsRepo(serviceProvider.get());
  }

  public static BookingsModule_ProvideBookingsRepoFactory create(
      Provider<OffersGraphQLService> serviceProvider) {
    return new BookingsModule_ProvideBookingsRepoFactory(serviceProvider);
  }

  public static BookingsRepository provideBookingsRepo(OffersGraphQLService service) {
    return Preconditions.checkNotNullFromProvides(BookingsModule.INSTANCE.provideBookingsRepo(service));
  }
}
