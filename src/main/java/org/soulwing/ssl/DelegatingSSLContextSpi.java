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

import java.security.KeyManagementException;
import java.security.SecureRandom;

import javax.net.ssl.KeyManager;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLContextSpi;
import javax.net.ssl.SSLEngine;
import javax.net.ssl.SSLParameters;
import javax.net.ssl.SSLServerSocketFactory;
import javax.net.ssl.SSLSessionContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;

/**
 * An {@link SSLContextSpi} that delegates to an {@link SSLContext}.
 *
 * @author Carl Harris
 */
class DelegatingSSLContextSpi extends SSLContextSpi {

  private final SSLContext delegate;

  private final SSLParametersConfiguration parameters;

  DelegatingSSLContextSpi(SSLContext delegate,
      SSLParametersConfiguration parameters) {
    this.delegate = delegate;
    this.parameters = parameters;
  }

  @Override
  protected void engineInit(KeyManager[] keyManagers,
      TrustManager[] trustManagers, SecureRandom secureRandom)
      throws KeyManagementException {
    delegate.init(keyManagers, trustManagers, secureRandom);
  }

  @Override
  protected SSLSocketFactory engineGetSocketFactory() {
    return new SSLSocketFactoryWrapper(delegate.getSocketFactory(),
        getParameters());
  }

  @Override
  protected SSLServerSocketFactory engineGetServerSocketFactory() {
    return new SSLServerSocketFactoryWrapper(delegate.getServerSocketFactory(),
        getParameters());
  }

  @Override
  protected SSLEngine engineCreateSSLEngine() {
    final SSLEngine engine = delegate.createSSLEngine();
    engine.setSSLParameters(getParameters());
    return engine;
  }

  @Override
  protected SSLEngine engineCreateSSLEngine(String s, int i) {
    final SSLEngine engine = delegate.createSSLEngine();
    engine.setSSLParameters(getParameters());
    return engine;
  }

  @Override
  protected SSLSessionContext engineGetServerSessionContext() {
    return delegate.getServerSessionContext();
  }

  @Override
  protected SSLSessionContext engineGetClientSessionContext() {
    return delegate.getClientSessionContext();
  }

  @Override
  protected SSLParameters engineGetDefaultSSLParameters() {
    return delegate.getDefaultSSLParameters();
  }

  @Override
  protected SSLParameters engineGetSupportedSSLParameters() {
    return delegate.getSupportedSSLParameters();
  }

  private SSLParameters getParameters() {
    return parameters.createParameters(
        delegate.getSupportedSSLParameters(),
        delegate.getDefaultSSLParameters());
  }

}
