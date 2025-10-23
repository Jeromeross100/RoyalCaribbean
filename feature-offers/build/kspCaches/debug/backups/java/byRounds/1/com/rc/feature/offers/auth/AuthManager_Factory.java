package com.rc.feature.offers.auth;

import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
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
    "deprecation"
})
public final class AuthManager_Factory implements Factory<AuthManager> {
  @Override
  public AuthManager get() {
    return newInstance();
  }

  public static AuthManager_Factory create() {
    return InstanceHolder.INSTANCE;
  }

  public static AuthManager newInstance() {
    return new AuthManager();
  }

  private static final class InstanceHolder {
    private static final AuthManager_Factory INSTANCE = new AuthManager_Factory();
  }
}
