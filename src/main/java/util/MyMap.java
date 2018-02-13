package util;

import java.util.*;
import java.util.function.Consumer;

public class MyMap<K, V> implements Map<K, V> {
    private int size = 0;
    private Entry<K, V> root;

    private EntrySet entrySet;

    public MyMap() {
        // TODO: Constructor implementation
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

    private V replaceValue(Map.Entry<K, V> entry, V value) {
        V oldValue = entry.getValue();
        entry.setValue(value);
        return oldValue;
    }

    @Override
    public V put(K key, V value) throws NullPointerException {
        if (root == null) {
            root = new Entry<>(key, value);
            size = 1;
        }
        if (root.key.equals(key)) {
            return replaceValue(root, value);
        } else {
            Entry<K, V> entry = root;
            while (entry.next != null) {
                if (entry.key.equals(key)) {
                    return replaceValue(entry, value);
                }
                entry = entry.next;
            }
            entry.next = new Entry<>(key, value);
            size++;
        }
        return null;
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
        // FIXME
        return Collections.emptySet();
    }

    @Override
    public Collection<V> values() {
        // FIXME
        return Collections.emptyList();
    }

    @Override
    public Set<Map.Entry<K, V>> entrySet() {
        return entrySet;
    }

    final class Values extends AbstractCollection<V> {
        public final int size() {
            return size;
        }

        public final void clear() {
            MyMap.this.clear();
        }

        public final Iterator<V> iterator() {
            return new MyMap.ValueIterator();
        }

        public final boolean contains(Object o) {
            return containsValue(o);
        }

        public final Spliterator<V> spliterator() {
            return null;
        }

        public final void forEach(Consumer<? super V> action) {
            // TODO
        }
    }

    class MySpliterator<K,V> {

    }

    class EntrySpliterator<K,V> extends MySpliterator implements Spliterator<Map.Entry<K,V>> {

        @Override
        public boolean tryAdvance(Consumer<? super Map.Entry<K, V>> action) {
            return false;
        }

        @Override
        public Spliterator<Map.Entry<K, V>> trySplit() {
            return null;
        }

        @Override
        public long estimateSize() {
            return 0;
        }

        @Override
        public int characteristics() {
            return 0;
        }
    }

    abstract class MyIterator implements Iterator<Map.Entry<K, V>>{
        Entry<K,V> current;
        Entry<K,V> next;
        Entry<K,V> previous;

        @Override
        public boolean hasNext() {
            return next != null;
        }

        @Override
        public Map.Entry<K, V> next() {
            return next;
        }

        @Override
        public void remove() {
            if (previous != null) {
                current = null;
            }
            if (next == null) {
                current.next = null;
            }
        }

    }

    private class EntryIterator extends MyIterator {

    }

    private class KeyIterator extends MyIterator {

    }

    private class ValueIterator extends MyIterator {

    }

    private class KeySet extends AbstractSet<K> {
        // TODO
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
        // TODO
        @Override
        public Iterator<V> iterator() {
            return null;
        }

        @Override
        public int size() {
            return 0;
        }
    }

    private class EntrySet extends AbstractSet<Map.Entry<K, V>> {
        // TODO
        @Override
        public Iterator<Map.Entry<K, V>> iterator() {
            return new EntryIterator();
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
            int keyHash = key.hashCode();
            int valueHash =value.hashCode();
            return keyHash ^ valueHash;
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
