package util;

import java.util.*;

public class MyMap<K, V> implements Map<K, V> {
    private int size = 0;
    private Entry<K, V> root;

    public MyMap() {
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public boolean containsKey(Object key) {
        return get(key) != null;
    }

    @Override
    public boolean containsValue(Object value) {
        Entry<K, V> entry = root;
        do {
            if (entry.getValue().equals(value)) {
                return true;
            }
            entry = entry.next;
        } while (entry != null);
        return false;
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return this == obj || obj instanceof Map && entrySet().equals(((Map) obj).entrySet());
    }

    @Override
    public String toString() {
        return super.toString();
    }

    @Override
    public V get(Object key) throws ClassCastException, NullPointerException {
        if (root == null) {
            return null;
        }
        Entry<K, V> entry = root;
        while (entry != null) {
            if (entry.getKey().equals(key)) {
                return entry.getValue();
            }
            entry = entry.next;
        }
        return null;
    }

    @Override
    public V put(Object key, Object value) throws ClassCastException, NullPointerException {
        K newKey = (K) key;
        V newValue = (V) value;
        Entry<K, V> entry = root;
        if (entry == null) {
            root = new Entry<K, V>(newKey, newValue);
            size = 1;
        } else {
            while (entry.next != null) {
                if (entry.key.equals(key)) {
                    V oldValue = entry.value;
                    entry.value = newValue;
                    return oldValue;
                }
                entry = entry.next;
            }
            entry.next = new Entry<>(newKey, newValue);
            size++;
        }
        return null;
    }

    @Override
    public V remove(Object key) throws ClassCastException, NullPointerException {

        Entry<K, V> entry = root;
        while (entry != null) {
            if (entry.key.equals(key)) {
                //TODO
            }
        }
        return null;
    }

    @Override
    public void putAll(Map m) throws ClassCastException, NullPointerException {

    }

    @Override
    public void clear() {
        size = 0;
        root = null;
    }

    @Override
    public Set<K> keySet() {
        Set<K> keys = new HashSet<>();
        Entry<K, V> entry = root;
        while (entry != null) {
            keys.add(entry.key);
            entry = entry.next;
        }
        return keys;
    }

    @Override
    public Collection<V> values() {
        List<V> values = new ArrayList<>();
        Entry<K, V> entry = root;
        while (entry != null) {
            values.add(entry.value);
            entry = entry.next;
        }
        return values;
    }

    @Override
    public Set<Map.Entry<K, V>> entrySet() {
        Set<Map.Entry<K, V>> entries = new HashSet<>();
        Entry<K, V> entry = root;
        while (entry != null) {
            entries.add(entry);
            entry = entry.next;
        }
        return entries;
    }

    private class Entry<K, V> implements Map.Entry<K, V> {
        final K key;
        V value;
        Entry<K, V> next;

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
        public V setValue(V value) {
            V oldValue = this.value;
            this.value = value;
            return oldValue;
        }

        @Override
        public int hashCode() {
            return super.hashCode();
        }

        @Override
        public String toString() {
            return "{ " + key + ": " + value + " }";
        }

        @Override
        public boolean equals(Object o) {
            return this == o || o instanceof Entry && key == ((Entry) o).getKey() && value == ((Entry) o).getValue();
        }
    }

}
