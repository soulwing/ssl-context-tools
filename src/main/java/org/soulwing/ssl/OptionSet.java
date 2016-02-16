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

import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.regex.Pattern;

/**
 * A set of strings that represent options.
 *
 * @author Carl Harris
 */
class OptionSet {

  private final Set<String> excludedOptions = new LinkedHashSet<>();
  private final Set<String> includedOptions = new LinkedHashSet<>();

  public void excludeOptions(String... options) {
    excludedOptions.addAll(Arrays.asList(options));
  }

  public void includeOptions(String... options) {
    includedOptions.addAll(Arrays.asList(options));
  }

  public String[] enabledOptions(String[] supportedOptions,
      String[] defaultOptions) {
    if (excludedOptions.isEmpty() && includedOptions.isEmpty()) {
      return defaultOptions;
    }
    final Collection<String> enabledOptions = enabledOptions(
        Arrays.asList(supportedOptions), Arrays.asList(defaultOptions));

    return enabledOptions.toArray(new String[enabledOptions.size()]);
  }

  public Collection<String> enabledOptions(
      Collection<String> supportedOptions,
      Collection<String> defaultOptions) {
    if (excludedOptions.isEmpty() && includedOptions.isEmpty()) {
      return defaultOptions;
    }
    final Collection<String> supportedAndIncludedOptions =
        retainMatching(supportedOptions, includedOptions);
    return removeMatching(
        supportedAndIncludedOptions, excludedOptions);
  }

  private Collection<String> retainMatching(Collection<String> strings,
      Set<String> patterns) {
    if (patterns.isEmpty()) return strings;
    return findMatches(strings, patterns);
  }

  private Collection<String> removeMatching(Collection<String> strings,
      Set<String> patterns) {
    if (patterns.isEmpty()) return strings;
    final Set<String> matches = findMatches(strings, patterns);
    final Set<String> result = new LinkedHashSet<>();
    result.addAll(strings);
    result.removeAll(matches);
    return result;
  }

  private Set<String> findMatches(Collection<String> strings,
      Set<String> patterns) {
    Set<String> matches = new LinkedHashSet<>();
    for (final String p : patterns) {
      final Pattern pattern = Pattern.compile(p);
      for (final String s : strings) {
        if (pattern.matcher(s).matches()) {
          matches.add(s);
        }
      }
    }
    return matches;
  }

}
