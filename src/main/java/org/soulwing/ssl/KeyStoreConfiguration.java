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
 * A configuration for a {@link KeyStore} builder.
 *
 * @param <T> builder type
 * @author Carl Harris
 */
public interface KeyStoreConfiguration<T> {

  /**
   * Specifies the key store type.
   * @param type key store type (e.g. JKS)
   * @return this builder
   * @see <a target="_top" href="http://docs.oracle.com/javase/7/docs/technotes/guides/security/StandardNames.html#KeyStore">JCA Standard Algorithm Names for KeyStore types</a>
   */
  T type(String type);

  /**
   * Specifies the name of the JCA provider that will produce the key store.
   * @param providerName provider name
   * @return this builder
   */
  T provider(String providerName);

  /**
   * Specifies the JCA provider that will produce the key store.
   * @param provider JCA provider
   * @return this builder
   */
  T provider(Provider provider);

  /**
   * Specifies the key store password.
   * @param password password
   * @return this builder
   */
  T password(char[] password);

  /**
   * Specifies the key store password.
   * @param password password
   * @return this builder
   */
  T password(String password);

  /**
   * Specifies the key store location.
   * @param location URL that specifies the location of the key store
   * @return this builder
   */
  T location(URL location);

  /**
   * Specifies the key store location.
   * @param location URI that specifies the location of the key store; may use 
   *    the {@code classpath:} pseudo-scheme to specify the location of the key 
   *    store relative to the root of the thread context class loader
   * @return this builder
   */
  T location(URI location);

  /**
   * Specifies the key store location.
   * @param location URI or path that specifies the location of the key store;
   *    may use the {@code classpath:} pseudo-scheme to specify the location of
   *    the key store relative to the root of the thread context class loader
   * @return this builder
   */
  T location(String location);

  /**
   * Specifies the key store location.
   * @param location path to the key store resource relative to
   *    {@code relativeToClass}
   * @param relativeToClass base class path location; its class loader will be
   *    used to access the resource
   * @return this builder
   */
  T location(String location, Class<?> relativeToClass);

  /**
   * Specifies the key store location.
   * @param location path to the key store resource relative to the root of
   *    {@code classLoader}
   * @param classLoader that will be used to access the resource
   * @return this builder
   */
  T location(String location, ClassLoader classLoader);

}
