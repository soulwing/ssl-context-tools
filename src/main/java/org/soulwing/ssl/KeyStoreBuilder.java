/*
 * File created on Feb 14, 2016
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
 * A builder that produces a {@link KeyStore}.
 *
 * @author Carl Harris
 */
public interface KeyStoreBuilder extends KeyStoreConfiguration<KeyStoreBuilder> {

  /**
   * Builds a key store according to the configuration of this builder.
   * @return key store
   * @throws SSLRuntimeException if the underlying JCA provider throws a checked 
   *    exception occurs while creating or loading the key store
   */
  KeyStore build() throws SSLRuntimeException;

}
