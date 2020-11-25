/*
 * Copyright 2020 Rod MacKenzie
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
package com.github.rodm.teamcity.internal

import org.gradle.api.Action
import org.gradle.api.file.FileCopyDetails

class FileCollectorAction implements Action<FileCopyDetails> {

    private Set<FileCopyDetails> files

    FileCollectorAction(Set<FileCopyDetails> files) {
        this.files = files
    }

    @Override
    void execute(FileCopyDetails fileCopyDetails) {
        files << fileCopyDetails
    }
}
