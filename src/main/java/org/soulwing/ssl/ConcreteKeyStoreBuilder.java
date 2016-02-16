/*
 * File created on Feb 15, 2016
 *
 * Copyright (c) 2016 Carl Harris, Jr
 * and others as noted
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.soulwing.ssl;

import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchProviderException;
import java.security.Provider;

/**
 * A concrete {@link KeyStoreBuilder} implementation.
 *
 * @author Carl Harris
 */
class ConcreteKeyStoreBuilder implements KeyStoreBuilder {

  private final KeyStoreFactory factory = new KeyStoreFactory();

  @Override
  public KeyStoreBuilder type(String type) {
    factory.setType(type);
    return this;
  }

  @Override
  public KeyStoreBuilder provider(String providerName) {
    factory.setProvider(providerName);
    return this;
  }

  @Override
  public KeyStoreBuilder provider(Provider provider) {
    factory.setProvider(provider);
    return this;
  }

  @Override
  public KeyStoreBuilder password(char[] password) {
    factory.setPassword(password);
    return this;
  }

  @Override
  public KeyStoreBuilder password(String password) {
    factory.setPassword(password);
    return this;
  }

  @Override
  public KeyStoreBuilder location(URL location) {
    factory.setLocation(location);
    return this;
  }

  @Override
  public KeyStoreBuilder location(String location) throws SSLRuntimeException {
    factory.setLocation(location);
    return this;
  }

  @Override
  public KeyStoreBuilder location(URI location) {
    factory.setLocation(location);
    return this;
  }

  @Override
  public KeyStoreBuilder location(String location, Class<?> relativeToClass) {
    factory.setLocation(location);
    return this;
  }

  @Override
  public KeyStoreBuilder location(String location, ClassLoader classLoader) {
    factory.setLocation(location);
    return this;
  }

  @Override
  public KeyStore build() throws SSLRuntimeException {
    return factory.newKeyStore();
  }

}
