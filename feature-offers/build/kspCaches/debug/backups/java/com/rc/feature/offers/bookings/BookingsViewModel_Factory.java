package com.rc.feature.offers.bookings;

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
public final class BookingsViewModel_Factory implements Factory<BookingsViewModel> {
  private final Provider<BookingsRepository> repoProvider;

  private BookingsViewModel_Factory(Provider<BookingsRepository> repoProvider) {
    this.repoProvider = repoProvider;
  }

  @Override
  public BookingsViewModel get() {
    return newInstance(repoProvider.get());
  }

  public static BookingsViewModel_Factory create(Provider<BookingsRepository> repoProvider) {
    return new BookingsViewModel_Factory(repoProvider);
  }

  public static BookingsViewModel newInstance(BookingsRepository repo) {
    return new BookingsViewModel(repo);
  }
}
