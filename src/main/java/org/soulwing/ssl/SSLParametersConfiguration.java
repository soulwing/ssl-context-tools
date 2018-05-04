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

import javax.net.ssl.SSLParameters;

/**
 * A configuration to use in deriving an {@link SSLParameters} instance from
 * supported and default SSL parameters.
 *
 * @author Carl Harris
 */
class SSLParametersConfiguration {

  private final OptionSet protocolOptions = new OptionSet();

  private final OptionSet cipherSuiteOptions = new OptionSet();

  private Boolean wantClientAuth;

  private Boolean needClientAuth;

  public void excludeProtocols(String... protocols) {
    protocolOptions.excludeOptions(protocols);
  }

  public void includeProtocols(String... protocols) {
    protocolOptions.includeOptions(protocols);
  }

  public void excludeCipherSuites(String... cipherSuites) {
    cipherSuiteOptions.excludeOptions(cipherSuites);
  }

  public void includeCipherSuites(String... cipherSuites) {
    cipherSuiteOptions.includeOptions(cipherSuites);
  }

  public void setWantClientAuth(Boolean wantClientAuth) {
    this.wantClientAuth = wantClientAuth;
  }

  public void setNeedClientAuth(Boolean needClientAuth) {
    this.needClientAuth = needClientAuth;
  }

  public SSLParameters createParameters(SSLParameters supportedParameters,
      SSLParameters defaultParameters) {
    SSLParameters parameters = new SSLParameters();
    parameters.setProtocols(protocolOptions.enabledOptions(
        supportedParameters.getProtocols(),
        defaultParameters.getProtocols()));
    parameters.setCipherSuites(cipherSuiteOptions.enabledOptions(
        supportedParameters.getCipherSuites(),
        defaultParameters.getCipherSuites()));
    parameters.setWantClientAuth(wantClientAuth != null ?
        wantClientAuth : defaultParameters.getWantClientAuth());
    parameters.setNeedClientAuth(needClientAuth != null ?
        needClientAuth : defaultParameters.getNeedClientAuth());

    return parameters;
  }

}
