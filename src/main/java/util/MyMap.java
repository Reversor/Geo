package util;

import java.util.*;

public class MyMap<K, V> implements Map<K, V> {
    private int size;
    private Entry<K,V> root;

    public MyMap() {
        entries = new Map.Entry<K,V>[initialCapacity];
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        return size <= 0;
    }

    @Override
    public boolean containsKey(Object key) {
        for (Map.Entry entry : entries) {
            if (entry.getKey().equals(key)) {
                return true;
            }
        }
        return false;
//        return entrySet.stream().anyMatch(e -> e.getKey().equals(key));
    }

    @Override
    public boolean containsValue(Object value) {
        for (Map.Entry<K, V> entry : entrySet) {
            if (entry.getValue().equals(value)) {
                return true;
            }
        }
        return false;
//        return entrySet.stream().anyMatch(e -> e.getValue().equals(value));
    }

    @Override
    public V get(Object key) {
        for (Map.Entry<K, V> e : entrySet) {
            if (e.getKey().equals(key)) {
                return e.getValue();
            }
        }
        return null;
//        return entrySet.stream().filter(e -> e.getKey().equals(key)).findFirst().get().getValue();
    }

    @Override
    public V put(Object key, Object value) throws ClassCastException, NullPointerException {
        try {

        }
        return null;
    }

    @Override
    public V remove(Object key) {
        for (Map.Entry<K, V> e : entrySet) {
            if (e.getKey().equals(key)) {
                entrySet.remove(e);
                return e.getValue();
            }
        }
        /*Map.Entry<K, V> entry = entrySet.stream().filter(e -> e.getKey().equals(key)).findAny().orElse(null);
        if (entry != null) {
            return entry.getValue();
        }*/
        return null;
    }

    @Override
    public void putAll(Map m) throws ClassCastException, NullPointerException {
        entrySet.addAll((Set<Map.Entry<K, V>>) m.entrySet());
    }

    @Override
    public void clear() {
        entrySet.clear();
    }

    @Override
    public Set<K> keySet() {
        Set<K> keys = new HashSet<>();
        for (Map.Entry<K, V> e : entrySet) {
            keys.add(e.getKey());
        }
        return keys;
//        return entrySet.stream().map(Map.Entry::getKey).collect(Collectors.toSet());
    }

    @Override
    public Collection<V> values() {
        List<V> values = new ArrayList<>();
        for (Map.Entry<K, V> e : entrySet) {
            values.add(e.getValue());
        }
        return values;
//        return entrySet.stream().map(Map.Entry::getValue).collect(Collectors.toList());
    }

    @Override
    public Set<Map.Entry<K, V>> entrySet() {
        return entrySet;
    }

    private class Entry<K, V> implements Map.Entry {
        final K key;
        V value;
        Entry<K,V> next;

        Entry(K key, V value) {
            this.key = key;
            this.value = value;
        }

        @Override
        public K getKey() {
            return key;
        }

        @Override
        public V getValue() {
            return value;
        }

        @Override
        public V setValue(Object value) throws ClassCastException, NullPointerException {
            return this.value = (V) value;
        }
    }
}
