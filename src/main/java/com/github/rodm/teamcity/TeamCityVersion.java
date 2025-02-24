/*
 * Copyright 2018 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.github.rodm.teamcity;

import org.gradle.api.GradleException;

import java.io.Serializable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TeamCityVersion implements Comparable<TeamCityVersion>, Serializable {

    private static final Pattern RELEASE_VERSION_PATTERN = Pattern.compile("^((\\d+)(\\.\\d+)+)");
    private static final Pattern SNAPSHOT_VERSION_PATTERN = Pattern.compile("^((\\d+)(\\.\\d+)+)-SNAPSHOT");
    private static final Pattern DATA_VERSION_PATTERN = Pattern.compile("^(\\d+\\.\\d+).*");

    private static final String INVALID_RELEASE_MESSAGE = "'%s' is not a valid TeamCity version string (examples: '9.0', '10.0.5', '2018.1')";
    private static final String INVALID_SNAPSHOT_MESSAGE = "'%s' is not a valid TeamCity version string (examples: '10.0-SNAPSHOT', '2021.1' '2021.2.1-SNAPSHOT')";

    private static final String SNAPSHOT = "SNAPSHOT";

    public static final TeamCityVersion VERSION_9_0 = version("9.0");
    public static final TeamCityVersion VERSION_2018_2 = version("2018.2");
    public static final TeamCityVersion VERSION_2020_1 = version("2020.1");

    public static TeamCityVersion version(String version) throws IllegalArgumentException {
        return TeamCityVersion.version(version, false);
    }

    public static TeamCityVersion version(String version, boolean allowSnapshots) throws IllegalArgumentException {
        if (!version.equals(SNAPSHOT)) {
            Matcher releaseMatcher = RELEASE_VERSION_PATTERN.matcher(version);
            if (allowSnapshots) {
                Matcher snapshotMatcher = SNAPSHOT_VERSION_PATTERN.matcher(version);
                if (!snapshotMatcher.matches() && !releaseMatcher.matches()) {
                    throw new IllegalArgumentException(String.format(INVALID_SNAPSHOT_MESSAGE, version));
                }
            } else {
                if (!releaseMatcher.matches()) {
                    throw new IllegalArgumentException(String.format(INVALID_RELEASE_MESSAGE, version));
                }
            }
        }
        return new TeamCityVersion(version);
    }

    private final String version;

    private TeamCityVersion(String version) {
        this.version = version;
    }

    public String toString() {
        return this.version;
    }

    public int compareTo(TeamCityVersion teamcityVersion) {
        if (version.equals(SNAPSHOT) && !teamcityVersion.version.equals(SNAPSHOT)) {
            return 1;
        } else if (teamcityVersion.version.equals(SNAPSHOT) && !version.equals(SNAPSHOT)) {
            return -1;
        } else if (version.equals(teamcityVersion.version)) {
            return 0;
        }
        String[] versionParts = version.split("[.|-]");
        String[] otherVersionParts = teamcityVersion.version.split("[.|-]");

        for (int diff = 0; diff < versionParts.length && diff < otherVersionParts.length; ++diff){
            if (versionParts[diff].equals(SNAPSHOT)) continue;
            if (otherVersionParts[diff].equals(SNAPSHOT)) continue;
            int part = Integer.parseInt(versionParts[diff]);
            int otherPart = Integer.parseInt(otherVersionParts[diff]);
            if (part > otherPart) {
                return 1;
            }
            if (otherPart > part) {
                return -1;
            }
        }
        return (versionParts.length > otherVersionParts.length) ? 1 : -1;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TeamCityVersion that = (TeamCityVersion) o;
        return version.equals(that.version);
    }

    public int hashCode() {
        return version.hashCode();
    }

    public boolean lessThan(TeamCityVersion teamcityVersion) {
        return compareTo(teamcityVersion) < 0;
    }

    public boolean equalOrGreaterThan(TeamCityVersion teamcityVersion) {
        return compareTo(teamcityVersion) >= 0;
    }

    public String getDataVersion() {
        Matcher matcher = DATA_VERSION_PATTERN.matcher(version);
        if (matcher.find()) {
            return matcher.group(1);
        }
        throw new GradleException("Invalid version");
    }
}
