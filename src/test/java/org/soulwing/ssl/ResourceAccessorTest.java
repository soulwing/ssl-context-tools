/*
 * File created on Feb 15, 2016
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

import java.io.IOException;
import java.io.InputStream;

import org.junit.After;
import org.junit.Test;

/**
 * Tests for {@link ResourceAccessor}.
 *
 * @author Carl Harris
 */
public class ResourceAccessorTest {

  private InputStream inputStream;
  
  @After
  public void tearDown() throws Exception {
    if (inputStream != null) {
      try {
        inputStream.close();
      }
      catch (IOException ex) {
        assert true;  // ignore error
      }
    }
  }
  
  @Test
  public void testGetResourceWithUriContainingPathOnly() throws Exception {
    String uri = Constants.TEST_PROPERTIES_PATH;
    assertThat(ResourceAccessor.getResource(uri), is(not(nullValue())));
  }

  @Test
  public void testGetResourceWithClasspathUri() throws Exception {
    String uri = "classpath:" + Constants.TEST_PROPERTIES_PATH;
    assertThat(ResourceAccessor.getResource(uri), is(not(nullValue())));
  }

  @Test
  public void testGetResourceWithClasspathUriHavingLeadingSlash() 
      throws Exception {
    String uri = "classpath:/" + Constants.TEST_PROPERTIES_PATH;
    assertThat(ResourceAccessor.getResource(uri), is(not(nullValue())));
  }

  @Test
  public void testGetResourceWithSupportedUrl() throws Exception {
    String uri = getClass().getResource(Constants.TEST_PROPERTIES).toString();
    assertThat(ResourceAccessor.getResource(uri), is(not(nullValue())));
  }

  @Test
  public void testGetResourceRelativeToClass() throws Exception {
    assertThat(ResourceAccessor.getResource(Constants.TEST_PROPERTIES, 
        getClass()), is(not(nullValue())));
  }
  
  @Test
  public void testGetResourceRelativeToRoot() throws Exception {
    String path = Constants.TEST_PROPERTIES_PATH;
    assertThat(ResourceAccessor.getResource(path, getClass().getClassLoader()),
        is(not(nullValue())));
  }
  
  @Test
  public void testGetResourceAsStream() throws Exception {
    String uri = Constants.TEST_PROPERTIES_PATH;
    assertThat(ResourceAccessor.getResourceAsStream(uri),
        is(not(nullValue())));
  }

  @Test(expected = IOException.class)
  public void testGetResourceAsStreamWhenNotFound() throws Exception {
    String uri = "DOES_NOT_EXIST";
    ResourceAccessor.getResourceAsStream(uri);
  }
}
