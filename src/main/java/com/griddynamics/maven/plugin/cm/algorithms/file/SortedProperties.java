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
package com.griddynamics.maven.plugin.cm.algorithms.file;

import java.util.*;

/**
 * Wrapper of vanilla Java Properties class.
 * Enhancements: keys are sorted in alphabetical order.
 */
@SuppressWarnings("unchecked")
class SortedProperties extends Properties {
    private final Map map = new TreeMap();

    public SortedProperties() {
    }

    public SortedProperties(Properties properties) {
        map.putAll(properties);
    }

    @Override
    public synchronized Object setProperty(String s, String s2) {
        return map.put(s, s2);
    }

    @Override
    public String getProperty(String s) {
        return (String) map.get(s);
    }

    @Override
    public String getProperty(String s, String s2) {
        return map.containsKey(s) ? (String) map.get(s) : s2;
    }

    @Override
    public Enumeration<?> propertyNames() {
        return Collections.enumeration(map.keySet());
    }

    @Override
    public Set<String> stringPropertyNames() {
        return map.keySet();
    }

    @Override
    public synchronized int size() {
        return map.size();
    }

    @Override
    public synchronized boolean isEmpty() {
        return map.isEmpty();
    }

    @Override
    public synchronized Enumeration<Object> keys() {
        return Collections.enumeration(map.keySet());
    }

    @Override
    public synchronized Enumeration<Object> elements() {
        return Collections.enumeration(map.values());
    }

    @Override
    public synchronized boolean contains(Object o) {
        return map.containsKey(o);
    }

    @Override
    public boolean containsValue(Object o) {
        return map.containsValue(o);
    }

    @Override
    public synchronized boolean containsKey(Object o) {
        return map.containsKey(o);
    }

    @Override
    public synchronized Object get(Object o) {
        return map.get(o);
    }

    @Override
    protected void rehash() {
        throw new UnsupportedOperationException();
    }

    @Override
    public synchronized Object put(Object o, Object o2) {
        return map.put(o, o2);
    }

    @Override
    public synchronized Object remove(Object o) {
        return map.remove(o);
    }

    @Override
    public synchronized void putAll(Map<?, ?> add) {
        map.putAll(add);
    }

    @Override
    public synchronized void clear() {
        map.clear();
    }

    @Override
    public synchronized Object clone() {
        throw new UnsupportedOperationException();
    }

    @Override
    public synchronized String toString() {
        return map.toString();
    }

    @Override
    public Set<Object> keySet() {
        return map.keySet();
    }

    @Override
    public Set<Map.Entry<Object, Object>> entrySet() {
        return map.entrySet();
    }

    @Override
    public Collection<Object> values() {
        return map.values();
    }

    @Override
    public synchronized boolean equals(Object o) {
        return map.equals(o);
    }

    @Override
    public synchronized int hashCode() {
        return map.hashCode();
    }
}
