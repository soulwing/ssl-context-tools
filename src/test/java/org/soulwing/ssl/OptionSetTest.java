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

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

import org.junit.Test;

/**
 * Unit tests for {@link OptionSet}.
 * @author Carl Harris
 */
public class OptionSetTest {

  private OptionSet optionSet = new OptionSet();

  @Test
  public void testDefaultOptions() throws Exception {
    assertThat(optionSet.enabledOptions(new String[0],
        new String[] { "A" }), is(equalTo(new String[] { "A" })));
  }

  @Test
  public void testExcludeOption() throws Exception {
    optionSet.excludeOptions("B");
    assertThat(optionSet.enabledOptions(new String[] { "A", "B" },
        new String[0]), is(equalTo(new String[] { "A" })));
  }

  @Test
  public void testExcludeOptionPattern() throws Exception {
    optionSet.excludeOptions(".*B");
    assertThat(optionSet.enabledOptions(new String[] { "A", "AB", "B" },
        new String[0]), is(equalTo(new String[] { "A" })));
  }

  @Test
  public void testIncludeOption() throws Exception {
    optionSet.includeOptions("A");
    assertThat(optionSet.enabledOptions(new String[] { "A", "B" },
        new String[0]), is(equalTo(new String[] { "A" })));
  }

  @Test
  public void testIncludeOptionPattern() throws Exception {
    optionSet.includeOptions("A.*");
    assertThat(optionSet.enabledOptions(new String[] { "A", "B" },
        new String[0]), is(equalTo(new String[] { "A" })));
  }

  @Test
  public void testIncludeAndExcludeOptions() throws Exception {
    optionSet.includeOptions("A", "B");
    optionSet.excludeOptions("B");
    assertThat(optionSet.enabledOptions(new String[] { "A", "B", "C" },
        new String[0]), is(equalTo(new String[] { "A" })));
  }

}
