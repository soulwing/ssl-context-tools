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
import java.security.Provider;
import java.security.SecureRandom;

import javax.net.ssl.SSLContext;

/**
 * A builder that produces an {@link SSLContext}.
 *
 * @author Carl Harris
 */
public interface SSLContextBuilder {

  /**
   * A enumeration of client authentication types
   */
  enum ClientAuthentication {
    NONE,
    REQUESTED,
    REQUIRED
  }

  /**
   * Specifies the name of the secure socket protocol (e.g. TLS).
   * @param protocol protocol name
   * @return this builder
   */
  SSLContextBuilder protocol(String protocol);

  /**
   * Specifies the name of the JCA provider that will produce the SSL context.
   * @param providerName JCA provider name
   * @return this builder
   */
  SSLContextBuilder provider(String providerName);

  /**
   * Specifies the JCA provider that will produce the SSL context.
   * @param provider JCA provider
   * @return this builder
   */
  SSLContextBuilder provider(Provider provider);

  /**
   * Specifies a secure socket transport protocol to exclude from the supported
   * protocols of the underlying context.
   * <p>
   * This method may be invoked repeatedly to build up the set of excluded
   * options.
   * @param protocol protocol name or regular expression
   * @return this builder
   */
  SSLContextBuilder excludeProtocol(String protocol);

  /**
   * Specifies secure socket transport protocols to exclude from the supported
   * protocols of the underlying context.
   * <p>
   * This method may be invoked repeatedly to build up the set of excluded
   * options.
   * @param protocols protocol names or regular expressions
   * @return this builder
   */
  SSLContextBuilder excludeProtocols(String... protocols);

  /**
   * Specifies a secure socket transport protocol to include among those
   * supported by the underlying context.
   * <p>
   * This method may be invoked repeatedly to build up the set of included
   * options.
   * @param protocol protocol name or regular expression
   * @return this builder
   */
  SSLContextBuilder includeProtocol(String protocol);

  /**
   * Specifies secure socket transport protocols to include among those
   * supported by the underlying context.
   * <p>
   * This method may be invoked repeatedly to build up the set of included
   * options.
   * @param protocols protocol names or regular expressions
   * @return this builder
   */
  SSLContextBuilder includeProtocols(String... protocols);

  /**
   * Specifies an SSL cipher suite to exclude from the supported cipher suites
   * of the underlying context.
   * <p>
   * This method may be invoked repeatedly to build up the set of excluded
   * options.
   * @param cipherSuite cipher suite name or regular expression
   * @return this builder
   */
  SSLContextBuilder excludeCipherSuite(String cipherSuite);

  /**
   * Specifies SSL cipher suites to exclude from the supported cipher suites
   * of the underlying context.
   * <p>
   * This method may be invoked repeatedly to build up the set of excluded
   * options.
   * @param cipherSuites cipher suite names or regular expressions
   * @return this builder
   */
  SSLContextBuilder excludeCipherSuites(String... cipherSuites);

  /**
   * Specifies an SSL cipher suite to include among those supported by the
   * underlying context.
   * <p>
   * This method may be invoked repeatedly to build up the set of included
   * options.
   * @param cipherSuite cipher suite name or regular expression
   * @return this builder
   */
  SSLContextBuilder includeCipherSuite(String cipherSuite);

  /**
   * Specifies SSL cipher suites to include among those supported by the
   * underlying context.
   * <p>
   * This method may be invoked repeatedly to build up the set of included
   * options.
   * @param cipherSuites cipher suite names or regular expressions
   * @return this builder
   */
  SSLContextBuilder includeCipherSuites(String... cipherSuites);

  /**
   * Specifies whether client authentication should be performed on SSL server
   * sockets produced by a factory created from the resulting context.
   * @param type client authentication type
   * @return
   */
  SSLContextBuilder clientAuthentication(ClientAuthentication type);

  /**
   * Starts a sub-builder that will produce a key store containing
   * a credential to be presented to the the SSL peer when negotiating secure
   * sessions using the resulting context.
   * <p>
   * The password used to protect the credential's private key is assumed to be
   * the same as the password used to protect the key store itself.
   * @return key store sub builder
   */
  KeyStoreSubBuilder credential();

  /**
   * Starts a sub-builder that will produce a key store containing
   * a credential to be presented to the the SSL peer when negotiating secure
   * sessions using the resulting context.
   * @param keyPassword password used to protect the credential's private key
   * @return key store sub builder
   */
  KeyStoreSubBuilder credential(char[] keyPassword);

  /**
   * Starts a sub-builder that will produce a key store containing
   * a credential to be presented to the the SSL peer when negotiating secure
   * sessions using the resulting context.
   * @param keyPassword password used to protect the credential's private key
   * @return key store sub builder
   */
  KeyStoreSubBuilder credential(String keyPassword);

  /**
   * Specifies a credential will be presented to the SSL peer when negotiating
   * secure sessions using the resulting context.
   * @param keyStore key store containing the credential to present
   * @param password password for the private key associated with the credential
   * @return this builder
   */
  SSLContextBuilder credential(KeyStore keyStore, char[] password);

  /**
   * Specifies a credential will be presented to the SSL peer when negotiating
   * secure sessions using the resulting context.
   * @param keyStore key store containing the credential to present
   * @param password password for the private key associated with the credential
   * @return this builder
   */
  SSLContextBuilder credential(KeyStore keyStore, String password);

  /**
   * Starts a sub-builder that will produce a key store containing
   * trusted certificates to be used to validate the identity
   * of the SSL peer when negotiating secure sessions using the resulting
   * context.
   * @return key store sub builder
   */
  KeyStoreSubBuilder peerTrust();

  /**
   * Specifies a store of trusted certificates that will be used to validate
   * the identity of the SSL peer when negotiating secure sessions using the
   * resulting context.
   * @param trustStore store of trusted certificates
   * @return this builder
   */
  SSLContextBuilder peerTrust(KeyStore trustStore);

  /**
   * Specifies a secure random number generator for use by the resulting context.
   * @param secureRandom secure random instance
   * @return this builder
   */
  SSLContextBuilder secureRandom(SecureRandom secureRandom);

  /**
   * Builds an SSL context according to the configuration of this builder.
   * @return SSL context
   * @throws SSLRuntimeException
   */
  SSLContext build() throws SSLRuntimeException;

}
