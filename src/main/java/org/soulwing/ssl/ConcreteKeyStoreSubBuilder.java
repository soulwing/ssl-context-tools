/*
 * File created on Feb 16, 2016
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

import java.net.URI;
import java.net.URL;
import java.security.KeyStore;
import java.security.Provider;

/**
 * A concrete {@link KeyStoreBuilder} implementation.
 *
 * @author Carl Harris
 */
class ConcreteKeyStoreSubBuilder implements KeyStoreSubBuilder {

  interface EndHandler {
    SSLContextBuilder handleEnd(KeyStore keyStore, char[] password);
  }

  private final KeyStoreFactory factory = new KeyStoreFactory();

  private final EndHandler endHandler;

  public ConcreteKeyStoreSubBuilder(EndHandler endHandler) {
    this.endHandler = endHandler;
  }

  @Override
  public KeyStoreSubBuilder type(String type) {
    factory.setType(type);
    return this;
  }

  @Override
  public KeyStoreSubBuilder provider(String providerName) {
    factory.setProvider(providerName);
    return this;
  }

  @Override
  public KeyStoreSubBuilder provider(Provider provider) {
    factory.setProvider(provider);
    return this;
  }

  @Override
  public KeyStoreSubBuilder password(char[] password) {
    factory.setPassword(password);
    return this;
  }

  @Override
  public KeyStoreSubBuilder password(String password) {
    factory.setPassword(password);
    return this;
  }

  @Override
  public KeyStoreSubBuilder location(URL location) {
    factory.setLocation(location);
    return this;
  }

  @Override
  public KeyStoreSubBuilder location(String location) throws SSLRuntimeException {
    factory.setLocation(location);
    return this;
  }

  @Override
  public KeyStoreSubBuilder location(URI location) {
    factory.setLocation(location);
    return this;
  }

  @Override
  public KeyStoreSubBuilder location(String location, Class<?> relativeToClass) {
    factory.setLocation(location, relativeToClass);
    return this;
  }

  @Override
  public KeyStoreSubBuilder location(String location, ClassLoader classLoader) {
    factory.setLocation(location, classLoader);
    return this;
  }

  @Override
  public SSLContextBuilder end() {
    return endHandler.handleEnd(factory.newKeyStore(), factory.getPassword());
  }

}
