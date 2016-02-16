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

/**
 * Support for tests.
 *
 * @author Carl Harris
 */
class Constants {

  public static final String PACKAGE_NAME = 
      Constants.class.getPackage().getName().replace('.', '/');
  
  public static final String TEST_PROPERTIES = "test.properties";

  public static final String TEST_PROPERTIES_PATH = 
      PACKAGE_NAME + "/" + TEST_PROPERTIES;

  public static final String TEST_VALUE = "test.value";

  public static final String TEST_NAME = "test.name";

}
