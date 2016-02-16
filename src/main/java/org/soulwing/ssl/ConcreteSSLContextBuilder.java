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

import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.Provider;
import java.security.SecureRandom;
import java.security.UnrecoverableKeyException;

import javax.net.ssl.KeyManager;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;

/**
 * A concrete {@link SSLContextBuilder}.
 *
 * @author Carl Harris
 */
class ConcreteSSLContextBuilder implements SSLContextBuilder {

  private final SSLParametersConfiguration parameters =
      new SSLParametersConfiguration();

  private String protocol = "TLS";
  private String providerName;
  private Provider provider;
  private KeyStore keyStore;
  private char[] keyPassword;
  private KeyStore trustStore;
  private SecureRandom secureRandom;

  @Override
  public SSLContextBuilder protocol(String protocol) {
    this.protocol = protocol;
    return this;
  }

  @Override
  public SSLContextBuilder provider(String providerName) {
    this.providerName = providerName;
    return this;
  }

  @Override
  public SSLContextBuilder provider(Provider provider) {
    this.provider = provider;
    return this;
  }

  @Override
  public SSLContextBuilder excludeProtocol(String protocol) {
    parameters.excludeProtocols(protocol);
    return this;
  }

  @Override
  public SSLContextBuilder excludeProtocols(String... protocols) {
    parameters.excludeProtocols(protocols);
    return this;
  }

  @Override
  public SSLContextBuilder includeProtocol(String protocol) {
    parameters.includeProtocols(protocol);
    return this;
  }

  @Override
  public SSLContextBuilder includeProtocols(String... protocols) {
    parameters.includeProtocols(protocols);
    return this;
  }

  @Override
  public SSLContextBuilder excludeCipherSuite(String cipherSuite) {
    parameters.excludeCipherSuites(cipherSuite);
    return this;
  }

  @Override
  public SSLContextBuilder excludeCipherSuites(String... cipherSuites) {
    parameters.excludeCipherSuites(cipherSuites);
    return this;
  }

  @Override
  public SSLContextBuilder includeCipherSuite(String cipherSuite) {
    parameters.includeCipherSuites(cipherSuite);
    return this;
  }

  @Override
  public SSLContextBuilder includeCipherSuites(String... cipherSuites) {
    parameters.includeCipherSuites(cipherSuites);
    return this;
  }

  @Override
  public SSLContextBuilder clientAuthentication(ClientAuthentication type) {
    switch (type) {
      case NONE:
        parameters.setWantClientAuth(false);
        parameters.setNeedClientAuth(false);
        break;
      case REQUESTED:
        parameters.setWantClientAuth(true);
        parameters.setNeedClientAuth(false);
        break;
      case REQUIRED:
        parameters.setWantClientAuth(true);
        parameters.setNeedClientAuth(true);
        break;
      default:
        throw new IllegalArgumentException(
            "unrecognized client authentication type");
    }
    return this;
  }

  @Override
  public SSLContextBuilder credential(KeyStore keyStore, char[] password) {
    this.keyStore = keyStore;
    this.keyPassword = password;
    return this;
  }

  @Override
  public SSLContextBuilder credential(KeyStore keyStore, String password) {
    return credential(keyStore,
        password != null ? password.toCharArray() : null);
  }

  @Override
  public KeyStoreSubBuilder credential() {
    return new ConcreteKeyStoreSubBuilder(new ConcreteKeyStoreSubBuilder.EndHandler() {
      @Override
      public SSLContextBuilder handleEnd(KeyStore keyStore, char[] password) {
        ConcreteSSLContextBuilder.this.keyStore = keyStore;
        ConcreteSSLContextBuilder.this.keyPassword = password;
        return ConcreteSSLContextBuilder.this;
      }
    });
  }

  @Override
  public KeyStoreSubBuilder credential(char[] keyPassword) {
    this.keyPassword = keyPassword;
    return new ConcreteKeyStoreSubBuilder(new ConcreteKeyStoreSubBuilder.EndHandler() {
      @Override
      public SSLContextBuilder handleEnd(KeyStore keyStore, char[] password) {
        ConcreteSSLContextBuilder.this.keyStore = keyStore;
        return ConcreteSSLContextBuilder.this;
      }
    });
  }

  @Override
  public KeyStoreSubBuilder credential(String keyPassword) {
    return credential(keyPassword != null ? keyPassword.toCharArray() : null);
  }

  @Override
  public SSLContextBuilder peerTrust(KeyStore trustStore) {
    this.trustStore = trustStore;
    return null;
  }

  @Override
  public KeyStoreSubBuilder peerTrust() {
    return new ConcreteKeyStoreSubBuilder(new ConcreteKeyStoreSubBuilder.EndHandler() {
        @Override
        public SSLContextBuilder handleEnd(KeyStore trustStore, char[] password) {
          ConcreteSSLContextBuilder.this.trustStore = trustStore;
          return ConcreteSSLContextBuilder.this;
        }
    });
  }

  @Override
  public SSLContextBuilder secureRandom(SecureRandom secureRandom) {
    this.secureRandom = secureRandom;
    return this;
  }

  public SSLContext build() throws SSLRuntimeException {
    try {
      SSLContext context = newSSLContext();
      SSLContextWrapper wrapper = new SSLContextWrapper(context, parameters);
      wrapper.init(createKeyManagers(), createTrustManagers(), secureRandom);
      return wrapper;
    }
    catch (RuntimeException ex) {
      throw ex;
    }
    catch (Exception ex) {
      throw new SSLRuntimeException(ex);
    }
  }

  private SSLContext newSSLContext()
      throws NoSuchAlgorithmException, NoSuchProviderException {
    if (protocol == null) {
      throw new SSLRuntimeException("protocol is required");
    }
    if (provider != null) {
      return SSLContext.getInstance(protocol, provider);
    }
    if (providerName != null) {
      return SSLContext.getInstance(protocol, providerName);
    }

    return SSLContext.getInstance(protocol);
  }

  private KeyManager[] createKeyManagers()
      throws NoSuchAlgorithmException, KeyStoreException,
      UnrecoverableKeyException {
    if (keyStore == null) return null;
    if (keyPassword == null) {
      throw new SSLRuntimeException("key password is required");
    }
    final KeyManagerFactory kmf = KeyManagerFactory.getInstance(
        KeyManagerFactory.getDefaultAlgorithm());
    kmf.init(keyStore, keyPassword);
    return kmf.getKeyManagers();
  }

  private TrustManager[] createTrustManagers()
      throws NoSuchAlgorithmException, KeyStoreException {
    if (trustStore == null) return null;
    final TrustManagerFactory tmf = TrustManagerFactory.getInstance(
        TrustManagerFactory.getDefaultAlgorithm());
    tmf.init(trustStore);
    return tmf.getTrustManagers();
  }

}
