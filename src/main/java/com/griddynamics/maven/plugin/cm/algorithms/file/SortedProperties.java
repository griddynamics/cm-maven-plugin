/*
 * Copyright (c) 2014 Grid Dynamics International, Inc.
 * Classification level: Public
 * Licensed under the Apache License, Version 2.0.
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
