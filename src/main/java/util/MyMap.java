package util;

import java.util.*;

public class MyMap<K, V> implements Map<K, V> {
    private int size = 0;
    private Entry<K, V> root;

    public MyMap() {
        // TODO Constructor implementation
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
        // TODO
        return super.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return this == obj || obj instanceof Map && entrySet().equals(((Map) obj).entrySet());
    }

    @Override
    public String toString() {
        // TODO
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
        if (root == null) {
            root = new Entry<K, V>(newKey, newValue);
            size = 1;
        }
        if (root.key.equals(key)) {
            return  replaceValue(root, newValue);
        } else {
            Entry<K,V> entry = root;
            while (entry.next != null) {
                if (entry.key.equals(key)) {
                    return replaceValue(entry, newValue);
                }
                entry = entry.next;
            }
            entry.next = new Entry<>(newKey, newValue);
            size++;
        }
        return null;
    }

    private V replaceValue(Map.Entry<K, V> entry, V value) {
        V oldValue = entry.getValue();
        entry.setValue(value);
        return oldValue;
    }

    @Override
    public V remove(Object key) throws ClassCastException, NullPointerException {
        // FIXME
        if (root.key.equals(key)) {
            V oldValue = root.value;
            root = root.next;
            size--;
            return oldValue;
        }
        Entry<K, V> entry = root;
        while (entry.next != null) {
            if (entry.next.key.equals(key)) {
                Entry<K, V> oldEntry = entry.next;
                entry.next = oldEntry.next;
                size--;
                return oldEntry.value;
            }
        }
        return null;
    }

    @Override
    public void putAll(Map<? extends K, ? extends V> m) throws ClassCastException, NullPointerException {
        if (this != m) {
            m.forEach(this::put);
        }
    }

    @Override
    public void clear() {
        size = 0;
        root = null;
    }

    @Override
    public Set<K> keySet() {
        //fixme
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
        //fixme
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

    private class EntryIterator {
        Entry<K,V> current;
        Entry<K,V> next;
        int index;

        EntryIterator() {
            current = next = null;
            index = 0;
        }

        public boolean hasNext() {
            return false;
        }

        public Object next() {
            return null;
        }
    }


    private class KeySet extends AbstractSet<K> {

        @Override
        public Iterator<K> iterator() {
            return null;
        }

        @Override
        public int size() {
            return 0;
        }
    }

    private class ValueSet extends AbstractSet<V> {

        @Override
        public Iterator<V> iterator() {
            return null;
        }

        @Override
        public int size() {
            return 0;
        }
    }

    private class EntrySet extends AbstractSet<Map.Entry<K,V>> {

        @Override
        public Iterator<Map.Entry<K, V>> iterator() {
            return null;
        }

        @Override
        public int size() {
            return 0;
        }
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
            // TODO
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
