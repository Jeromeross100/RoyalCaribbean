package com.rc.feature.offers.ui.auth;

import com.rc.feature.offers.auth.AuthRepository;
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
public final class LoginViewModel_Factory implements Factory<LoginViewModel> {
  private final Provider<AuthRepository> repoProvider;

  public LoginViewModel_Factory(Provider<AuthRepository> repoProvider) {
    this.repoProvider = repoProvider;
  }

  @Override
  public LoginViewModel get() {
    return newInstance(repoProvider.get());
  }

  public static LoginViewModel_Factory create(Provider<AuthRepository> repoProvider) {
    return new LoginViewModel_Factory(repoProvider);
  }

  public static LoginViewModel newInstance(AuthRepository repo) {
    return new LoginViewModel(repo);
  }
}
