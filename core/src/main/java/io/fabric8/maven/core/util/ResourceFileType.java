/*
 * Copyright 2016 Red Hat, Inc.
 *
 * Red Hat licenses this file to you under the Apache License, version
 * 2.0 (the "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or
 * implied.  See the License for the specific language governing
 * permissions and limitations under the License.
 */

package io.fabric8.maven.core.util;

import java.io.File;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.fasterxml.jackson.dataformat.yaml.YAMLGenerator;

/**
 * Type of resources supported
 *
 * @author roland
 * @since 07/04/16
 */
public enum ResourceFileType {

    json("json","json") {
        @Override
        public ObjectMapper getObjectMapper() {
            return new ObjectMapper();
        }
    },

    yaml("yml","yml") {
        @Override
        public ObjectMapper getObjectMapper() {
            return new ObjectMapper(new YAMLFactory()
                                        .configure(YAMLGenerator.Feature.MINIMIZE_QUOTES, true)
                                        .configure(YAMLGenerator.Feature.ALWAYS_QUOTE_NUMBERS_AS_STRINGS, true)
                                    );
        }
    };

    private final String extension;
    private String artifactType;

    ResourceFileType(String extension, String artifactType) {
        this.extension = extension;
        this.artifactType = artifactType;
    }

    public abstract ObjectMapper getObjectMapper();

    public File addExtension(File file) {
        String path = file.getAbsolutePath();
        return new File(path + "." + extension);
    }

    public String getArtifactType() {
        return artifactType;
    }

    public static ResourceFileType fromExtension(String ext) {
        try {
            return ResourceFileType.valueOf(ext);
        } catch (IllegalArgumentException exp) {
            // Try extensions, too:
            for (ResourceFileType type : ResourceFileType.values()) {
                if (type.extension.equals(ext)) {
                    return type;
                }
            }
            throw exp;
        }
    }
}
