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
 * A configurable factory that produces {@link KeyStore} objects.
 *
 * @author Carl Harris
 */
class KeyStoreFactory {

  private String type;
  private String providerName;
  private Provider provider;
  private char[] password;
  private URL location;

  public void setType(String type) {
    this.type = type;
  }

  public void setProvider(String providerName) {
    this.providerName = providerName;
  }

  public void setProvider(Provider provider) {
    this.provider = provider;
  }

  public char[] getPassword() {
    return password;
  }

  public void setPassword(char[] password) {
    this.password = password;
  }

  public void setPassword(String password) {
    this.password = password != null ? password.toCharArray() : null;
  }

  public void setLocation(URL location) {
    if (location == null) {
      throw new NullPointerException("URL is required");
    }
    this.location = location;
  }

  public void setLocation(URI location) {
    try {
      final URL url = ResourceAccessor.getResource(location);
      assertResolved(location.toString(), url);
      setLocation(url);
    }
    catch (MalformedURLException ex) {
      throw new SSLRuntimeException(ex);
    }
  }

  public void setLocation(String location) {
    try {
      final URL url = ResourceAccessor.getResource(location);
      assertResolved(location, url);
      setLocation(url);
    }
    catch (URISyntaxException | MalformedURLException ex) {
      throw new SSLRuntimeException(ex);
    }
  }

  public void setLocation(String location, Class<?> relativeToClass) {
    final URL url = ResourceAccessor.getResource(location, relativeToClass);
    assertResolved(location, url);
    setLocation(url);
  }

  public void setLocation(String location, ClassLoader classLoader) {
    final URL url = ResourceAccessor.getResource(location, classLoader);
    assertResolved(location, url);
    setLocation(url);
  }

  private void assertResolved(String location, URL url) {
    if (url == null) {
      throw new SSLRuntimeException(
          "cannot resolve location: '" + location + "'");
    }
  }

  public KeyStore newKeyStore() {
    try (InputStream inputStream = location.openStream()) {
      final KeyStore keyStore = getKeyStoreInstance();
      keyStore.load(inputStream, password);
      return keyStore;
    }
    catch (RuntimeException ex) {
      throw ex;
    }
    catch (Exception ex) {
      throw new SSLRuntimeException(ex);
    }
  }

  private KeyStore getKeyStoreInstance()
      throws KeyStoreException, NoSuchProviderException {
    final String type = this.type == null ? KeyStore.getDefaultType() : this.type;
    if (provider != null) {
      return KeyStore.getInstance(type, provider);
    }
    if (providerName != null) {
      return KeyStore.getInstance(type, providerName);
    }

    return KeyStore.getInstance(type);
  }

}
