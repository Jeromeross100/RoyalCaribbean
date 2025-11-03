package com.rc.feature.offers.ui.list;

import com.rc.feature.offers.data.OffersRepository;
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
public final class OffersListViewModel_Factory implements Factory<OffersListViewModel> {
  private final Provider<OffersRepository> repositoryProvider;

  private OffersListViewModel_Factory(Provider<OffersRepository> repositoryProvider) {
    this.repositoryProvider = repositoryProvider;
  }

  @Override
  public OffersListViewModel get() {
    return newInstance(repositoryProvider.get());
  }

  public static OffersListViewModel_Factory create(Provider<OffersRepository> repositoryProvider) {
    return new OffersListViewModel_Factory(repositoryProvider);
  }

  public static OffersListViewModel newInstance(OffersRepository repository) {
    return new OffersListViewModel(repository);
  }
}
