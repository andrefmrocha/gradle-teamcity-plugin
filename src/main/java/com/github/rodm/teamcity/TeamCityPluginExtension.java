/*
 * Copyright 2015 Rod MacKenzie
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

import org.gradle.api.Action;
import org.gradle.api.file.CopySpec;

import java.util.Map;

/**
 * TeamCity Plugin extension.
 */
public interface TeamCityPluginExtension {

    /**
     * The version of the TeamCity API.
     *
     * @param version The API version.
     */
    void setVersion(String version);
    String getVersion();

    /**
     * Use default repositories to resolve dependencies.
     *
     * @param useDefaultRepositories Configure default repositories
     */
    void setDefaultRepositories(boolean useDefaultRepositories);
    boolean getDefaultRepositories();

    /**
     * Allow version to include snapshot versions.
     *
     * @param allowSnapshots Allow snapshot versions
     */
    void setAllowSnapshotVersions(boolean allowSnapshots);
    boolean getAllowSnapshotVersions();

    /**
     * Set the validation mode for validating plugin bean definition files
     *
     * @param mode The validation mode
     */
    void setValidateBeanDefinition(ValidationMode mode);
    void setValidateBeanDefinition(String mode);
    ValidationMode getValidateBeanDefinition();

    /**
     * Configures the agent-side plugin.
     *
     * <p>The given action is executed to configure the agent-side plugin configuration.</p>
     *
     * @param configuration The action.
     */
    void agent(Action<AgentPluginConfiguration> configuration);
    AgentPluginConfiguration getAgent();

    /**
     * Configures the server-side plugin.
     *
     * <p>The given action is executed to configure the server-side plugin configuration.</p>
     *
     * @param configuration The action.
     */
    void server(Action<ServerPluginConfiguration> configuration);
    ServerPluginConfiguration getServer();

    void setDescriptor(Object descriptor);
    void descriptor(Action<?> configuration);

    CopySpec files(Action<CopySpec> configuration);

    void setTokens(Map<String, Object> tokens);
    void tokens(Map<String, Object> tokens);

    /**
     * Configures the TeamCity environments.
     *
     * <p>The given action is executed to configure the environments.</p>
     *
     * @param configuration The action.
     */
    void environments(Action<TeamCityEnvironments> configuration);
    TeamCityEnvironments getEnvironments();
}
