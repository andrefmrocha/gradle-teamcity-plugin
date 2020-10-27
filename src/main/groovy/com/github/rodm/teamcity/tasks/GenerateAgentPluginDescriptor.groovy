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
package com.github.rodm.teamcity.tasks

import com.github.rodm.teamcity.AgentPluginDescriptor
import com.github.rodm.teamcity.AgentPluginDescriptorGenerator
import com.github.rodm.teamcity.TeamCityVersion
import groovy.transform.CompileStatic
import org.gradle.api.DefaultTask
import org.gradle.api.file.RegularFileProperty
import org.gradle.api.provider.Property
import org.gradle.api.tasks.CacheableTask
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.Nested
import org.gradle.api.tasks.OutputFile
import org.gradle.api.tasks.TaskAction

import static com.github.rodm.teamcity.TeamCityVersion.VERSION_9_0

@CompileStatic
@CacheableTask
class GenerateAgentPluginDescriptor extends DefaultTask {

    private Property<String> version

    private Property<AgentPluginDescriptor> descriptor

    private RegularFileProperty destination

    GenerateAgentPluginDescriptor() {
        version = project.objects.property(String)
        descriptor = project.objects.property(AgentPluginDescriptor)
        destination = project.objects.fileProperty()
        onlyIf { descriptor.isPresent() }
    }

    @Input
    Property<String> getVersion() {
        return version
    }

    @Nested
    Property<AgentPluginDescriptor> getDescriptor() {
        return descriptor
    }

    @OutputFile
    RegularFileProperty getDestination() {
        return destination
    }

    @TaskAction
    void generateDescriptor() {
        if (TeamCityVersion.version(version.get()) < VERSION_9_0 && descriptor.get().dependencies.hasDependencies()) {
            project.logger.warn("${path}: Plugin descriptor does not support dependencies for version ${version.get()}")
        }
        AgentPluginDescriptorGenerator generator = new AgentPluginDescriptorGenerator(descriptor.get())
        destination.get().asFile.withPrintWriter('UTF-8') { writer -> generator.writeTo(writer) }
    }
}
