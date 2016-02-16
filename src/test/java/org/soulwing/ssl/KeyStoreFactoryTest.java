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

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.nullValue;

import java.security.KeyStore;

import org.junit.Test;

/**
 * Tests for {@link KeyStoreFactory}.
 *
 * @author Carl Harris
 */
public class KeyStoreFactoryTest {

  @Test
  public void testBuildFromResource() throws Exception {
    KeyStoreFactory factory = new KeyStoreFactory();
    factory.setType("JKS");
    factory.setLocation("keystore.jks", getClass());
    factory.setPassword("changeit");
    KeyStore keyStore = factory.newKeyStore();
    assertThat(keyStore, is(not(nullValue())));
  }

  @Test
  public void testBuildFromClasspath() throws Exception {
    KeyStoreFactory factory = new KeyStoreFactory();
    factory.setType("JKS");
    factory.setLocation("classpath:org/soulwing/ssl/keystore.jks");
    factory.setPassword("changeit");
    KeyStore keyStore = factory.newKeyStore();
    assertThat(keyStore, is(not(nullValue())));
  }

  @Test
  public void testBuildWithPKCS12() throws Exception {
    KeyStoreFactory factory = new KeyStoreFactory();
    factory.setType("PKCS12");
    factory.setLocation("keystore.p12", getClass());
    factory.setPassword("changeit");
    KeyStore keyStore = factory.newKeyStore();
    assertThat(keyStore, is(not(nullValue())));
  }


}
