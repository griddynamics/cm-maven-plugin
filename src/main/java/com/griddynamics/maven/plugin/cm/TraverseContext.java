/*
 * Copyright 2014 Grid Dynamics International, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.griddynamics.maven.plugin.cm;

import com.google.common.collect.Lists;

import java.io.File;
import java.util.Iterator;
import java.util.List;

class TraverseContext {

    private final class ContextIterator implements Iterator<TraverseContext> {

        private TraverseContext context;

        protected ContextIterator(TraverseContext context) {
            this.context = context;
        }

        @Override
        public final boolean hasNext() {
            return context != null;
        }

        @Override
        public TraverseContext next() {
            if (!hasNext()) {
                throw new IllegalStateException("Reached the end of iterable sequence");
            }
            TraverseContext context = this.context;
            this.context = context.parent;
            return context;
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException("Not implemented");
        }
    }

    private final TraverseContext parent;

    private final File directory;

    private final String layer;

    private List<String> layerNames = null;

    private List<File> directories = null;

    public TraverseContext(File directory, String layer) {
        this(null, directory, layer);
    }

    public TraverseContext(TraverseContext parent, File directory, String layer) {
        this.parent = parent;
        this.directory = directory;
        this.layer = layer;
    }

    public List<File> getDirectories() {
        if (directories == null) {
            final ContextIterator contextIterator = new ContextIterator(this);
            directories = Lists.reverse(Lists.newArrayList(new Iterator<File>() {
                @Override
                public boolean hasNext() {
                    return contextIterator.hasNext();
                }

                @Override
                public File next() {
                    return contextIterator.next().directory;
                }

                @Override
                public void remove() {

                }
            }));
        }
        return directories;
    }

    public List<String> getLayerNames() {
        if (layerNames == null) {
            final ContextIterator contextIterator = new ContextIterator(this);
            layerNames = Lists.reverse(Lists.newArrayList(new Iterator<String>() {
                @Override
                public boolean hasNext() {
                    return contextIterator.hasNext();
                }

                @Override
                public String next() {
                    return contextIterator.next().layer;
                }

                @Override
                public void remove() {
                    contextIterator.remove();
                }
            }));
        }
        return layerNames;
    }

}
