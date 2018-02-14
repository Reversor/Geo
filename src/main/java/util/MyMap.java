package util;

import java.util.*;
import java.util.function.Consumer;

public class MyMap<K, V> implements Map<K, V> {
    private int size = 0;
    private Entry<K, V> root;

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
        if (isEmpty()) {
            root = new Entry<>(key, value);
            size = 1;
            return null;
        }
        if (root.key.equals(key)) {
            return replaceValue(root, value);
        } else {
            Entry<K, V> previousEntry = root;
            Entry<K, V> currentEntry = root.next;
            while (currentEntry != null) {
                if (currentEntry.key.equals(key)) {
                    return replaceValue(currentEntry, value);
                }
                previousEntry = currentEntry;
                currentEntry = currentEntry.next;
            }
            previousEntry.next = new Entry<>(key, value);
            size++;
        }
        return null;
    }

    @Override
    public V remove(Object key) throws ClassCastException {
        if (root.key.equals(key)) {
            V oldValue = root.value;
            root = root.next;
            size--;
            return oldValue;
        }
        Entry<K, V> previousEntry = root;
        Entry<K, V> currentEntry = root.next;
        while (currentEntry != null) {
            if (currentEntry.key.equals(key)) {
                previousEntry.next = currentEntry.next;
                size--;
                return currentEntry.value;
            }
            currentEntry = currentEntry.next;
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
        return new KeySet();
    }

    @Override
    public Collection<V> values() {
        // FIXME
        return new Values();
    }

    @Override
    public Set<Map.Entry<K, V>> entrySet() {
        return new EntrySet();
    }

    private class EntrySet extends AbstractSet<Map.Entry<K, V>> {
        // TODO
        public Iterator<Map.Entry<K, V>> iterator() {
            return new EntryIterator();
        }

        public int size() {
            return MyMap.this.size;
        }

        public boolean remove(Object o) {
            return MyMap.this.remove(o) != null;
        }
    }

    class MySpliterator<K, V> {

    }

    class EntrySpliterator<K, V> extends MySpliterator implements Spliterator<Map.Entry<K, V>> {

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

    abstract class MyIterator<T> implements Iterator<T> {
        // FIXME: Игнорирует первый элемент
        Entry<K, V> current;
        Entry<K, V> next;

        MyIterator(Entry<K, V> first) {
            next = first;
        }

        public boolean hasNext() {
            return next != null;
        }

        Map.Entry<K, V> nextEntry() {
            if (hasNext()) {
                current = next;
                next = next.next;
                return current;
            }
            throw new NoSuchElementException();
        }

        public void remove() {
            MyMap.this.remove(current);
        }
    }

    class EntryIterator extends MyIterator<Map.Entry<K, V>> {
        EntryIterator() {
            super(root);
        }

        @Override
        public Map.Entry<K, V> next() {
            return nextEntry();
        }
    }

    class KeyIterator extends MyIterator<K> {
        KeyIterator() {
            super(root);
        }

        public K next() {
            return nextEntry().getKey();
        }
    }

    class ValueIterator extends MyIterator<V> {
        ValueIterator() {
            super(root);
        }

        @Override
        public V next() {
            return nextEntry().getValue();
        }
    }

    private class KeySet extends AbstractSet<K> {

        @Override
        public boolean removeAll(Collection<?> c) {
            // TODO
            return false;
        }

        @Override
        public boolean remove(Object o) {
            return MyMap.this.remove(o) != null;
        }

        @Override
        public boolean retainAll(Collection<?> c) {
            c.forEach( e -> {

            });
            return super.retainAll(c);
        }

        @Override
        public void clear() {
            super.clear();
        }

        @Override
        public Iterator<K> iterator() {
            return new KeyIterator();
        }

        @Override
        public int size() {
            return 0;
        }

    }

    final class Values extends AbstractCollection<V> {
        public final int size() {
            return size;
        }

        public final void clear() {
            MyMap.this.clear();
        }

        public final Iterator<V> iterator() {
            return new ValueIterator();
        }

        public final boolean contains(Object o) {
            return containsValue(o);
        }

        public final Spliterator<V> spliterator() {
            // TODO
            return null;
        }

        public final void forEach(Consumer<? super V> action) {
            // TODO
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
            int valueHash = value.hashCode();
            return keyHash ^ valueHash;
        }

        @Override
        public String toString() {
            return "{ " + key + ": " + value + " }";
        }

        @Override
        public boolean equals(Object o) {
            return this == o || o instanceof Entry
                    && key.equals(((Entry) o).getKey())
                    && value.equals(((Entry) o).getValue());
        }
    }

}
