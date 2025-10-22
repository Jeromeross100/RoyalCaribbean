package com.rc.feature.offers.ui.details;

import com.rc.feature.offers.data.OffersRepository;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

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
    "deprecation"
})
public final class OfferDetailsViewModel_Factory implements Factory<OfferDetailsViewModel> {
  private final Provider<OffersRepository> repositoryProvider;

  public OfferDetailsViewModel_Factory(Provider<OffersRepository> repositoryProvider) {
    this.repositoryProvider = repositoryProvider;
  }

  @Override
  public OfferDetailsViewModel get() {
    return newInstance(repositoryProvider.get());
  }

  public static OfferDetailsViewModel_Factory create(
      Provider<OffersRepository> repositoryProvider) {
    return new OfferDetailsViewModel_Factory(repositoryProvider);
  }

  public static OfferDetailsViewModel newInstance(OffersRepository repository) {
    return new OfferDetailsViewModel(repository);
  }
}
