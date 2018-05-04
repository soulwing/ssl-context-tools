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

import javax.net.ssl.SSLContext;

/**
 * A wrapper for an {@link SSLContext} that allows configured SSL parameters
 * to be configured on sockets and engines produced by the wrapped context.
 *
 * @author Carl Harris
 */
class SSLContextWrapper extends SSLContext {

  protected SSLContextWrapper(SSLContext delegate,
      SSLParametersConfiguration parameters) {
    super(new DelegatingSSLContextSpi(delegate, parameters),
        delegate.getProvider(),
        delegate.getProtocol());
  }

}
