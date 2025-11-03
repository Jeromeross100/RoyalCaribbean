package com.rc.feature.offers.auth;

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
public final class AuthViewModel_Factory implements Factory<AuthViewModel> {
  private final Provider<AuthManager> authManagerProvider;

  private AuthViewModel_Factory(Provider<AuthManager> authManagerProvider) {
    this.authManagerProvider = authManagerProvider;
  }

  @Override
  public AuthViewModel get() {
    return newInstance(authManagerProvider.get());
  }

  public static AuthViewModel_Factory create(Provider<AuthManager> authManagerProvider) {
    return new AuthViewModel_Factory(authManagerProvider);
  }

  public static AuthViewModel newInstance(AuthManager authManager) {
    return new AuthViewModel(authManager);
  }
}
