/*
 * Copyright 2018 Rod MacKenzie
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

import java.util.List;

/**
 * Configuration for publishing a plugin to a plugin repository.
 */
public interface PublishConfiguration {
    /**
     * The list of channel names that the plugin will be published to on the plugin repository
     *
     * @return the list of channels
     */
    List<String> getChannels();
    void setChannels(List<String> channels);

    /**
     * The token for uploading the plugin to the plugin repository
     *
     * @return the authentication token
     */
    String getToken();
    void setToken(String token);

    /**
     * The notes describing the changes made to the plugin
     *
     * @return the release notes
     */
    String getNotes();
    void setNotes(String notes);
}
